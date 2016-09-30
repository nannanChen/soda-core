package com.soda.job

import java.util.Date

import com.soda.common.{DistanceUtil, GridDivide}
import org.apache.spark.{SparkContext, SparkConf}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by kcao on 2016/9/29.
  */
object SodaEtlDynamicGridCountJob {

  val distance=2*1000   //1公里=1000米   2公里为一个商圈  下面三个商圈
  val nanJingDong=new Point(121.4910040000,31.2436160000)
  val xuJiaHui=new Point(121.4446320000,31.1993870000)
  val xinZhuang=new Point(121.3914070000,31.1167550000)

  def main(args: Array[String]): Unit = {
    System.setProperty("spark.default.parallelism","60")
    val conf = new SparkConf().setAppName("SodaEtlDynamicGridCountJob") //创建环境变量
    conf.set("spark.default.parallelism","60")
    val sc = new SparkContext(conf)  //创建环境变量实例
    val data = sc.textFile("hdfs://192.168.20.90:9000/soda/fusai/表一结果.csv").cache()  //读取数据

    val userDays=data.map(_.replaceAll("\"","")).filter(_.indexOf("IMEI,00:00-01:00")==(-1)).map(packageUserDay(_))

    val pointTuple5=userDays.flatMap(packagePointToTuple5(_))

    val pointTuple5Filter=pointTuple5.filter(tuple5=>GridDivide.checkPoint(tuple5._3,tuple5._4)).cache()

    val pointTuple5FilterCount=pointTuple5Filter.count()
    println(new Date()+" pointTuple5FilterCount:"+pointTuple5FilterCount)

    //对每个点计算距离   过滤2公里内的点
    val pointTuple6=pointTuple5Filter.map(tuple5=>{(tuple5._1,tuple5._2,tuple5._3,tuple5._4,tuple5._5,computeDistance(new Point(tuple5._3,tuple5._4),nanJingDong))}).filter(_._6<distance)
//      .map(tuple6=>{
//      (tuple6._1,tuple6._2,tuple6._3,tuple6._4,tuple6._5,tuple6._6,GridDivide.findIndex(tuple6._3,tuple6._4))
//    })

    val pointTuple6Count=pointTuple6.count()
    println(new Date()+" pointTuple6Count:"+pointTuple6Count)

    //按照时间 和 网格块分组
    val pointByHourGrid=pointTuple6.map(tuple6=>(tuple6._2+"_"+GridDivide.findIndex(tuple6._3,tuple6._4),Seq(tuple6))).reduceByKey(_.union(_))
    //todo 动态网格构思
//    pointByHourGrid.re
  }



  def computeDistance(start:Point,end:Point):Double ={
    //    Math.sqrt(Math.pow(start.longitude-end.longitude,2)+Math.pow(start.latitude-end.latitude,2))
    DistanceUtil.GetDistance(start.longitude,start.latitude,end.longitude,end.latitude)
  }

  def packagePointToTuple5(any: Any): Array[(String,Int,Double,Double,String)] ={
    val buffer=new ArrayBuffer[(String,Int,Double,Double,String)]()
    if(any!=null&&any.isInstanceOf[UserDay]){
      val userDay=any.asInstanceOf[UserDay]
      for(i <- 0 until userDay.trajectory.length){
        val point=userDay.trajectory(i)
        if(point!=null){
          buffer.+=((userDay.date,i,point.longitude,point.latitude,userDay.imei))
        }
      }
    }else{
      println(new Date()+" packagePointToTuple5 any="+any)
    }
    buffer.toArray
  }

  def packageUserDay(line:String): UserDay ={
    var userDay: UserDay = null;
    val arr=line.split(",");
    if(arr.length>=2){
      val trajectory:Array[Point]=new Array[Point](24);
      for(i <- 0 to 23){
        val index = i * 2 + 2;
        if(index>=arr.length||"".equals(arr(index))){
          trajectory(i)=null;
        }else{
          trajectory(i)=new Point(java.lang.Double.parseDouble(arr(index)),java.lang.Double.parseDouble(arr(index+1)))
        }
      }
      userDay=new UserDay(arr(0),arr(1),trajectory)
    }else{
      println(new Date()+" 数据格式不正确！line:"+line)
    }
    userDay
  }
  case class Point(longitude:Double,latitude:Double)   //latitude纬度
  case class UserDay(date:String,imei:String,trajectory:Array[Point])  //用户的一天.
}
