package com.soda.job

import com.soda.common.{HbaseUtil, DistanceUtil, IdentityTypeEnum, ConstantsUtil}
import com.soda.vo.{User, Basic, PointDetail}
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.{Partitioner, SparkConf, SparkContext}

/**
  * 数据整合  提供前端展现使用
  *
  *  spark-submit \
  * --master spark://192.168.20.90:7077 \
  * --deploy-mode client \
  * --jars /home/hadoop/sodatest/lib/hbase-server-1.2.0.jar,/home/hadoop/sodatest/lib/hbase-common-1.2.0.jar,/home/hadoop/sodatest/lib/hbase-protocol-1.2.0.jar,/home/hadoop/sodatest/lib/hbase-client-1.2.0.jar,/home/hadoop/sodatest/lib/guava-11.0.2.jar,/home/hadoop/sodatest/lib/htrace-core-3.1.0-incubating.jar,/home/hadoop/sodatest/lib/metrics-core-2.2.0.jar  \
  * --class com.soda.job.SodaEtlJob /home/hadoop/sodatest/soda-1.0-SNAPSHOT.jar >> soda.log
  *
  * Created by kcao on 2016/9/23.
  */
object SodaEtlJob {

  val distance=2*1000   //1公里=1000米   2公里为一个商圈  下面三个商圈
  val nanJingDong=new Point(121.4910040000,31.2436160000)
  val xuJiaHui=new Point(121.4446320000,31.1993870000)
  val xinZhuang=new Point(121.3914070000,31.1167550000)

  def main(args: Array[String]): Unit = {

//    val rs=computeDistance(new Point(121.81459,31.54292001),nanJingDong);
//    println(rs)

    val conf = new SparkConf().setAppName("SodaEtlJob")
    val sc = new SparkContext(conf)

    val hbaseConf = HBaseConfiguration.create()
    hbaseConf.set("hbase.zookeeper.property.clientPort", "2181")
    hbaseConf.set("hbase.zookeeper.quorum", "zk1")
    hbaseConf.set(TableInputFormat.INPUT_TABLE, ConstantsUtil.POINT_DETAIL)

    //读取hbase所有点数据
    val hbaseTable = sc.newAPIHadoopRDD(hbaseConf, classOf[TableInputFormat], classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable], classOf[org.apache.hadoop.hbase.client.Result])

    println("hbaseTable 33 partitions:"+hbaseTable.partitions.size)

    //hbase每条数据封装成PointDetail对象
    val pointDetail=hbaseTable.map(tuple2=>packagePointDetail(tuple2._1,tuple2._2))

    //对所有的点按照(小时)进行分区
    val pointByDateTime=pointDetail.map(point=>{(point.basic.time,point)}).partitionBy(new TimePartitioner(24))

    println("pointByDateTime 22 pointByDateTime:"+pointByDateTime.partitions.size)

    //计算每个小时内的点跟商圈距离
    val distancePoint=pointByDateTime.map(dateTime=>{
      val point=new Point(dateTime._2.basic.longitude,dateTime._2.basic.latitude)
      (dateTime._2,computeDistance(point,nanJingDong),computeDistance(point,xuJiaHui),computeDistance(point,xinZhuang))
    }).cache()


    val nanJingDongRs=distancePoint.filter(_._2<distance).map(tuple4=>{(getPointMapInfo("nanJingDong",tuple4._1,tuple4._2))})
    nanJingDongRs.saveAsTextFile(ConstantsUtil.HDFS_ADDRESS+"/soda/mysql/nanJingDongRs")

    val xuJiaHuiRs=distancePoint.filter(_._3<distance).map(tuple4=>{(getPointMapInfo("xuJiaHui",tuple4._1,tuple4._3))})
    xuJiaHuiRs.saveAsTextFile(ConstantsUtil.HDFS_ADDRESS+"/soda/mysql/xuJiaHuiRs")

    val xinZhuangRs=distancePoint.filter(_._4<distance).map(tuple4=>{(getPointMapInfo("xinZhuang",tuple4._1,tuple4._4))})
    xinZhuangRs.saveAsTextFile(ConstantsUtil.HDFS_ADDRESS+"/soda/mysql/xinZhuangRs")

    println("SodaEtlJob1 sleep!!!")
    Thread.sleep(3*60*1000)
    println("SodaEtlJob1 end!!!")

    System.exit(0)
  }

  /**
    * 保存到mysql 给前台展现的数据格式
    * @param point
    * @param distance
    * @return
    */
  def getPointMapInfo(name:String,point: PointDetail,distance: Double) = {
    val precursorPoint=packagePointDetail(null,HbaseUtil.getPrecursorResult(point.basic.precursor))
    (point.rowkey,   //点id
      point.basic.date, //点日期
      point.basic.precursor,
      point.basic.longitude,
      point.basic.latitude,
      point.basic.next,
      name,
      distance.toInt,
      if(precursorPoint==null) "" else precursorPoint.basic.longitude,
      if(precursorPoint==null) "" else precursorPoint.basic.latitude,
      if(precursorPoint==null) "" else precursorPoint.basic.next)
  }

  /**
    * 欧氏距离
    * 计算两个点的距离
    * 公式： d = sqrt((x1-x2)^+(y1-y2)^)
    */
  def computeDistance(start:Point,end:Point):Double ={
//    Math.sqrt(Math.pow(start.longitude-end.longitude,2)+Math.pow(start.latitude-end.latitude,2))
    DistanceUtil.GetDistance(start.longitude,start.latitude,end.longitude,end.latitude)
  }

  def packagePointDetail(immutable:ImmutableBytesWritable,result:Result): PointDetail = {
    if(result==null){return null}
    val rowKey = Bytes.toString(result.getRow)
    val precursor = Bytes.toString(result.getValue("basic".getBytes,"precursor".getBytes))
    val longitude = Bytes.toString(result.getValue("basic".getBytes,"longitude".getBytes))
    val latitude = Bytes.toString(result.getValue("basic".getBytes,"latitude".getBytes))
    val next = Bytes.toString(result.getValue("basic".getBytes,"next".getBytes))
    val date = Bytes.toString(result.getValue("basic".getBytes,"date".getBytes))
    val time = Bytes.toString(result.getValue("basic".getBytes,"time".getBytes))
    val valType = Bytes.toString(result.getValue("user".getBytes,"valType".getBytes))
    val value = Bytes.toString(result.getValue("user".getBytes,"value".getBytes))
    return new PointDetail(rowKey,new Basic(precursor,java.lang.Double.parseDouble(longitude),java.lang.Double.parseDouble(latitude),next,date,time),new User(IdentityTypeEnum.convert(valType),value))
  }

  case class Point(longitude:Double,latitude:Double)

}

class TimePartitioner(num: Int) extends Partitioner{
  override def numPartitions: Int = (num+1)

  override def getPartition(key: Any): Int = {
    println("1key:"+key.toString+" 1getClass:"+key.getClass)
    var index=0
    index=Integer.parseInt(key.toString)
    if(index>num){
      index=24
    }
    println("TimePartitioner index="+index)
    index
  }
}
