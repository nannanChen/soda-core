package com.soda.job

import java.util.Date

import com.soda.common.GridDivide
import com.soda.vo.GridDetail
import org.apache.spark.{SparkContext, SparkConf}

import scala.collection.mutable.ArrayBuffer

/**
  * 交通卡数据2016.3  统计刷卡次数任务
  *
  * /home/hadoop/tools/spark-1.6.0-bin-hadoop2.6/bin/spark-submit \
  * --master yarn \
  * --deploy-mode cluster \
  * --driver-memory 5g \
  * --executor-memory 5g \
  * --executor-cores 3 \
  * --num-executors 10 \
  * --jars /home/hadoop/sodatest/lib/mysql-connector-java-5.1.38.jar  \
  * --class com.soda.job.SptccEtlJob /home/hadoop/sodatest/soda-1.0-SNAPSHOT.jar >> soda.log
  *
  * Created by kcao on 2016/10/11.
  */
object SptccEtlJob {

  def main(args: Array[String]) {

    System.setProperty("spark.default.parallelism","60")
    val conf = new SparkConf().setAppName("SptccEtlJob") //创建环境变量
    conf.set("spark.default.parallelism","60")
    val sc = new SparkContext(conf)  //创建环境变量实例

    val sptccData = sc.textFile("hdfs://192.168.20.90:9000/soda/fusai/交通卡数据2016.3/SPTCC-*").map(_.split(",")).filter(_.length==7)

//    val sptccDataCount=sptccData.count()
//    println("partitions:"+sptccData.partitions.size+" sptccDataCount:"+sptccDataCount)

    val sptccDataDistinct=sptccData.map(arr=>{arr(0)+","+arr(1)}).distinct().map(_.split(","))     //按照卡号+日期  进行去重

//    val sptccDataDistinctCount=sptccDataDistinct.count()
//    println("partitions:"+sptccDataDistinct.partitions.size+" sptccDataCount:"+sptccDataDistinctCount)

    val sptccGroupByDate=sptccDataDistinct.map(arr=>{(arr(1).replaceAll("-",""),1d)}).reduceByKey(_+_).coalesce(1)  //按照日期 分组统计卡号
//    sptccGroupByDate.collect().map(sptcc=>{println("sptcc:"+sptcc)})

//    println("#######################################################################################################################################################")

    val unicomData = sc.textFile("hdfs://192.168.20.90:9000/soda/fusai/表一结果.csv").map(_.replaceAll("\"","")).filter(_.indexOf("IMEI,00:00-01:00")==(-1)).map(packageUserDay(_)).flatMap(packageGridDetail(_))

//    val unicomDataCount=unicomData.count()
//    println("partitions:"+unicomData.partitions.size+" unicomDataCount:"+unicomDataCount)

    val unicomDataDistinct=unicomData.map(detail=>{detail.imei+","+detail.date}).distinct().map(_.split(","))     //按照imei+日期  进行去重

//    val unicomDataDistinctCount=unicomDataDistinct.count()
//    println("partitions:"+unicomDataDistinct.partitions.size+" unicomDataDistinctCount:"+unicomDataDistinctCount)

    val unicomGroupByDate=unicomDataDistinct.map(arr=>{(arr(1),1d)}).reduceByKey(_+_).coalesce(1)   //按照日期 分组统计imei
//    unicomGroupByDate.collect().map(unicom=>{println("unicom:"+unicom)})

    val sptccJoinUnicom=sptccGroupByDate.join(unicomGroupByDate)
    sptccJoinUnicom.map(map=>{(map._1,map._2,map._2._1/map._2._2)}).collect().map(sau=>{println("SptccAndUnicom:"+sau)})

  }


  def packageGridDetail(any: Any): Array[GridDetail] ={
    val buffer=new ArrayBuffer[GridDetail]()
    if(any!=null&&any.isInstanceOf[UserDay]){
      val userDay=any.asInstanceOf[UserDay]
      for(i <- 0 until userDay.trajectory.length){
        val fromIndex=userDay.trajectory(i)
        if(fromIndex!=(-1)){
          var toIndex=(-1)
          if((i+1)<userDay.trajectory.length){
            toIndex=userDay.trajectory(i+1)
          }
          val gridDetail=new GridDetail(userDay.date,userDay.imei,i,fromIndex,toIndex)
          buffer.+=(gridDetail)
        }
      }
      println(new Date()+" packageGridDetail buffer="+buffer.size)
    }else{
      println(new Date()+" packageGridDetail any="+any)
    }
    buffer.toArray
  }

  //复赛下标
  def packageUserDay(line:String): UserDay ={
    var userDay: UserDay = null
    val arr=line.split(",");
    if(arr.length>=2){
      val trajectory:Array[Int]=new Array[Int](24)
      for(i <- 0 to 23){
        val index = i * 2 + 2
        if(index>=arr.length||"".equals(arr(index))){
          trajectory(i)=(-1)
        }else{
          val point=new Point(java.lang.Double.parseDouble(arr(index)),java.lang.Double.parseDouble(arr(index+1)))
          if(GridDivide.checkPoint(point.longitude,point.latitude)){
            trajectory(i)=GridDivide.findIndex(point.longitude,point.latitude)
          }else{
            trajectory(i)=(-1)
          }
        }
      }
      userDay=new UserDay(arr(0),arr(1),trajectory)
    }else{
      println(new Date()+" 数据格式不正确！line:"+line)
    }
    userDay
  }

  case class Point(longitude:Double,latitude:Double)   //latitude纬度
  case class UserDay(date:String,imei:String,trajectory:Array[Int])  //用户的一天.


}
