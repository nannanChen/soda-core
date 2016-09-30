package com.soda.job

import java.sql.{DriverManager, PreparedStatement, Connection}

import com.soda.common.{GridDivide, ConstantsUtil}
import org.apache.commons.lang3.StringUtils
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.protobuf.ProtobufUtil
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.hadoop.hbase.util.Base64;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.client.{Result, Scan}
;

/**
  *
  *  /home/hadoop/tools/spark-1.6.0-bin-hadoop2.6/bin/spark-submit \
  * --master yarn \
  * --deploy-mode cluster \
  * --driver-memory 5g \
  * --executor-memory 5g \
  * --executor-cores 3 \
  * --num-executors 10 \
  * --jars /home/hadoop/sodatest/lib/mysql-connector-java-5.1.38.jar,/home/hadoop/sodatest/lib/hbase-server-1.2.0.jar,/home/hadoop/sodatest/lib/hbase-common-1.2.0.jar,/home/hadoop/sodatest/lib/hbase-protocol-1.2.0.jar,/home/hadoop/sodatest/lib/hbase-client-1.2.0.jar,/home/hadoop/sodatest/lib/guava-11.0.2.jar,/home/hadoop/sodatest/lib/htrace-core-3.1.0-incubating.jar,/home/hadoop/sodatest/lib/metrics-core-2.2.0.jar  \
  * --class com.soda.job.GridEtlJob /home/hadoop/sodatest/soda-1.0-SNAPSHOT.jar 0 >> soda.log
  *
  * Created by kcao on 2016/9/29.
  */
object GridEtlJob {

  def main(args: Array[String]): Unit = {

    if(args.length!=1){
      println("args is error[hour?]")
    }

    val hour=args(0)
    println("family:"+"hour"+hour)

    System.setProperty("spark.default.parallelism","60")
    val conf = new SparkConf().setAppName("GridEtlJob")
    conf.set("spark.default.parallelism","60")
    val sc = new SparkContext(conf)

    val hbaseReadConf = HBaseConfiguration.create()
    hbaseReadConf.set("hbase.zookeeper.property.clientPort", "2181")
    hbaseReadConf.set("hbase.zookeeper.quorum", "zk1,zk2,zk3")
    hbaseReadConf.set(TableInputFormat.INPUT_TABLE, ConstantsUtil.POINT_MAP_INFO)
    val scan = new Scan()
    scan.setCaching(10000)
    scan.setCacheBlocks(false)
    scan.addFamily(Bytes.toBytes("hour"+hour)) //指定需要的family或column ，如果没有调用任何addFamily或Column，会返回所有的columns；

    val proto = ProtobufUtil.toScan(scan)
    val ScanToString = Base64.encodeBytes(proto.toByteArray())
    hbaseReadConf.set(TableInputFormat.SCAN, ScanToString)


    val hbasePointMapInfoTable = sc.newAPIHadoopRDD(hbaseReadConf, classOf[TableInputFormat], classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable], classOf[org.apache.hadoop.hbase.client.Result]).cache()
    val hbasePointMapInfoTableCount=hbasePointMapInfoTable.count()
    println("hbasePointMapInfoTableCount="+hbasePointMapInfoTableCount)

    val pointMapInfo=hbasePointMapInfoTable.map(tuple2=>packagePointMapInfo(tuple2._1,tuple2._2,hour))

    val pointMapInfoCount=pointMapInfo.count()
    println("pointMapInfoCount="+pointMapInfoCount)

    //人群划分成块
    val pointGridInfo=pointMapInfo.map(info=>{
      if(StringUtils.isBlank(info.srcLongitude)){
        val x=java.lang.Double.parseDouble(info.targetLongitude)
        val y=java.lang.Double.parseDouble(info.targetLatitude)
        info.gridIndex=GridDivide.findIndex(x,y)
      }else{
        val x=java.lang.Double.parseDouble(info.srcLongitude)
        val y=java.lang.Double.parseDouble(info.srcLatitude)
        info.gridIndex=GridDivide.findIndex(x,y)
      }
      info
    }).map(gridInfo=>(gridInfo.date+"_"+gridInfo.name+"_"+gridInfo.gridIndex,1))

    val pointGridInfoCount=pointGridInfo.count()
    println("pointGridInfoCount="+pointGridInfoCount)

    val groupGrid=pointGridInfo.reduceByKey(_+_)

    val groupGridCount=groupGrid.count()
    println("groupGridCount="+groupGridCount)

    val mysqlRs=groupGrid.map(gridInfo=>{
      val arr=gridInfo._1.split("_")
      val address=GridDivide.addressMap.get(arr(1))
      val index=GridDivide.indexMap.get(arr(2))
      (arr(0),hour,index.x,index.y,gridInfo._2,arr(1),address.x,address.y)
    })

    val mysqlRsCount=mysqlRs.count()
    println("mysqlRsCount="+mysqlRsCount)

    mysqlRs.foreachPartition(toMySQL(_))

  }

  def packagePointMapInfo(immutable: ImmutableBytesWritable, result: Result,hour:String): PointMapInfo = {
    if(result==null){return null}
    val rowKey = Bytes.toString(result.getRow)
    val name = Bytes.toString(result.getValue(("hour"+hour).getBytes,"name".getBytes))
    val date = Bytes.toString(result.getValue(("hour"+hour).getBytes,"date".getBytes))
    val targetLongitude = Bytes.toString(result.getValue(("hour"+hour).getBytes,"target_longitude".getBytes))
    val targetLatitude = Bytes.toString(result.getValue(("hour"+hour).getBytes,"target_latitude".getBytes))
    val distance = Bytes.toString(result.getValue(("hour"+hour).getBytes,"distance".getBytes))
    val srcLongitude = Bytes.toString(result.getValue(("hour"+hour).getBytes,"src_longitude".getBytes))
    val srcLatitude = Bytes.toString(result.getValue(("hour"+hour).getBytes,"src_latitude".getBytes))
    return new PointMapInfo(rowKey,name,date,hour,targetLongitude,targetLatitude,distance,srcLongitude,srcLatitude,-1)
  }

  def toMySQL(iterator: Iterator[(String, String, Double, Double, Int, String, Double, Double)]): Unit = {
    var conn: Connection = null
    var ps: PreparedStatement = null
    val sql ="insert into `soda`.`point_map_info` (`date`, `hour`,`src_longitude`, `src_latitude`,`count`,`name`, `target_precursor`, `target_longitude`) values (?,?,?,?,?,?,?,?)";
    try {
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(ConstantsUtil.DB_URL, ConstantsUtil.DB_USERNAME, ConstantsUtil.DB_PASSWORD)
      iterator.foreach(dataIn => {
        ps = conn.prepareStatement(sql)
        ps.setString(1, dataIn._1)
        ps.setInt(2, Integer.parseInt(dataIn._2))
        ps.setString(3, dataIn._3.toString)
        ps.setString(4, dataIn._4.toString)
        ps.setInt(5, dataIn._5)
        ps.setString(6, dataIn._6)
        ps.setString(7, dataIn._7.toString)
        ps.setString(8, dataIn._8.toString)
        ps.executeUpdate()
      })
    }  finally {
      if (ps != null) {
        ps.close()
      }
      if (conn != null) {
        conn.close()
      }
    }
  }


  case class PointMapInfo(rowKey:String,name:String,date:String,hour:String,targetLongitude:String,targetLatitude:String,distance:String,srcLongitude:String,srcLatitude:String,var gridIndex:Int)


}
