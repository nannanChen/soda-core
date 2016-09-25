package com.soda.job

import com.soda.common.{DistanceUtil, IdentityTypeEnum, ConstantsUtil}
import com.soda.vo.{User, Basic, PointDetail}
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.{SparkConf, SparkContext}

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

    val rs=computeDistance(new Point(121.81459,31.54292001),nanJingDong);
    println(rs)

    val conf = new SparkConf().setAppName("SodaEtlJob")
    val sc = new SparkContext(conf)

    val hbaseConf = HBaseConfiguration.create()
    hbaseConf.set("hbase.zookeeper.property.clientPort", "2181")
    hbaseConf.set("hbase.zookeeper.quorum", "zk1")
    hbaseConf.set(TableInputFormat.INPUT_TABLE, ConstantsUtil.POINT_DETAIL)

    //读取hbase所有点数据
    val hbaseTable = sc.newAPIHadoopRDD(hbaseConf, classOf[TableInputFormat], classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable], classOf[org.apache.hadoop.hbase.client.Result])

    //hbase每条数据封装成PointDetail对象
    val pointDetail=hbaseTable.map(tuple2=>packagePointDetail(tuple2._1,tuple2._2))

    //对所有的点按照(日期+小时)进行分组
    val pointByDateTime=pointDetail.map(point=>{(point.basic.date+point.basic.time,Seq(point))}).reduceByKey(_.union(_))

    //计算每组数据跟商圈距离
    val distancePoint=pointByDateTime.map(dateTime=>{(dateTime._1,dateTime._2.map(detail=>{
      val point=new Point(detail.basic.longitude,detail.basic.latitude)
      (detail,computeDistance(point,nanJingDong),computeDistance(point,xuJiaHui),computeDistance(point,xinZhuang))
    }))})

    distancePoint.foreach(dateTime=>{
      dateTime._2.sortBy(_._2).foreach(t=>{
        println("nanJingDong:"+dateTime._1+"->"+t._1.rowkey+"-> "+t._2+" "+t._3+" "+t._4)
      })
      dateTime._2.sortBy(_._3).foreach(t=>{
        println("xuJiaHui:"+dateTime._1+"->"+t._1.rowkey+"-> "+t._2+" "+t._3+" "+t._4)
      })
      dateTime._2.sortBy(_._4).foreach(t=>{
        println("xinZhuang:"+dateTime._1+"->"+t._1.rowkey+"-> "+t._2+" "+t._3+" "+t._4)
      })
    })

    println("SodaEtlJob1 end!!!")

    System.exit(0)
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
    val rowKey = Bytes.toString(result.getRow)
    val precursor = Bytes.toString(result.getValue("basic".getBytes,"precursor".getBytes))
    val longitude = Bytes.toString(result.getValue("basic".getBytes,"longitude".getBytes))
    val latitude = Bytes.toString(result.getValue("basic".getBytes,"latitude".getBytes))
    val next = Bytes.toString(result.getValue("basic".getBytes,"next".getBytes))
    val date = Bytes.toString(result.getValue("basic".getBytes,"date".getBytes))
    val time = Bytes.toString(result.getValue("basic".getBytes,"time".getBytes))
    val valType = Bytes.toString(result.getValue("user".getBytes,"valType".getBytes))
    val value = Bytes.toString(result.getValue("user".getBytes,"value".getBytes))
    new PointDetail(rowKey,new Basic(precursor,java.lang.Double.parseDouble(longitude),java.lang.Double.parseDouble(latitude),next,date,time),new User(IdentityTypeEnum.convert(valType),value))
  }

  case class Point(longitude:Double,latitude:Double)   //latitude纬度
}
