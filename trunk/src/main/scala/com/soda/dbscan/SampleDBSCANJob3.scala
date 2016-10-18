package com.soda.dbscan

import java.util.Date
import com.soda.common.{ GridDivide, IdentityTypeEnum}
import com.soda.job.UnicomOneEtlJob._
import com.soda.vo.{Basic, PointDetail, User}
import org.apache.spark.{SparkConf, SparkContext}
import org.slf4j.LoggerFactory
import scala.collection.mutable.ArrayBuffer

/**
  * Created by kcao on 2016/10/14.
  *
  * /home/hadoop/tools/spark-1.6.0-bin-hadoop2.6/bin/spark-submit \
  * --master yarn \
  * --deploy-mode cluster \
  * --driver-memory 5g \
  * --executor-memory 5g \
  * --executor-cores 3 \
  * --num-executors 10 \
  * --class com.soda.dbscan.SampleDBSCANJob3 /home/hadoop/sodatest/soda-1.0-SNAPSHOT.jar
  *
  */
object SampleDBSCANJob3 {

  val log = LoggerFactory.getLogger(SampleDBSCANJob.getClass)

  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("SampleDBSCANJob3")
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    val sc = new SparkContext(conf)

    val data = sc.textFile("hdfs://192.168.20.90:9000/soda/test/5.1-位置数据.csv")  //读取数据

    val userDays=data.map(_.replaceAll("\"","")).filter(_.indexOf("IMEI,00:00-01:00")==(-1)).map(packageUserDay(_))

    val allPointDetail=userDays.flatMap(packagePointDetail(_)).map(detail=>{
      detail.basic.longitude+","+detail.basic.latitude
    })

    val allPointDetailCount=allPointDetail.count()
    println("allPointDetailCount2="+allPointDetailCount)

    allPointDetail.coalesce(1).saveAsTextFile("hdfs://192.168.20.90:9000/soda/test/UserLocation1")

    log.info("Stopping Spark Context...")
    sc.stop()

  }

  def packagePointDetail(any: Any): Array[PointDetail] ={
    val buffer=new ArrayBuffer[PointDetail]()
    if(any!=null&&any.isInstanceOf[UserDay]){
      val userDay=any.asInstanceOf[UserDay]
      for(i <- 0 until userDay.trajectory.length){
        val point=userDay.trajectory(i)
        if(point!=null){
          val index=GridDivide.findIndex(point.longitude,point.latitude)
          val rowKey = createNewRowKey(userDay.date,i,index,userDay.imei)
          val pointDetail=new PointDetail(rowKey,new Basic("",point.longitude,point.latitude,"",userDay.date,i,-1),new User(IdentityTypeEnum.IMEI,userDay.imei))
          buffer.+=(pointDetail)
        }
      }
      //      println(new Date()+" packagePointDetail buffer="+buffer.size)
    }else{
      println(new Date()+" packagePointDetail any="+any)
    }
    buffer.toArray
  }

  //初赛下标
  def packageUserDay(line:String): UserDay ={
    var userDay: UserDay = null;
    val arr=line.split(",");
    if(arr.length>=3){
      val trajectory:Array[Point]=new Array[Point](24);
      for(i <- 0 to 23){
        val index = i * 2 + 3;
        if(index>=arr.length||"".equals(arr(index))){
          trajectory(i)=null;
        }else{
          val point=new Point(java.lang.Double.parseDouble(arr(index)),java.lang.Double.parseDouble(arr(index+1)))
          if(GridDivide.checkPoint(point.longitude,point.latitude)){
            trajectory(i)=point
          }else{
            trajectory(i)=null;
          }
        }
      }
      userDay=new UserDay(arr(1),arr(2),trajectory)
    }else{
      println(new Date()+" 数据格式不正确！line:"+line)
    }
    userDay
  }

  case class Point(longitude:Double,latitude:Double)   //latitude纬度
  case class UserDay(date:String,imei:String,trajectory:Array[Point])  //用户的一天.
}
