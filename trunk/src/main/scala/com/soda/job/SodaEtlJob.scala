package com.soda.job

import com.soda.common.IdentityTypeEnum
import com.soda.vo.{Basic, PointDetail,User}
import org.apache.log4j.Logger
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkContext, SparkConf}
import scala.collection.mutable.ArrayBuffer

/**
  * spark-submit \
  * --master spark://192.168.20.90:7077 \
  * --deploy-mode client \
  * --class com.soda.job.SodaEtlJob /home/hadoop/sodatest/soda-1.0-SNAPSHOT.jar >> soda.log
  * Created by kcao on 2016/9/22.
  */
object SodaEtlJob{

  val log_ : Logger = Logger.getLogger(this.getClass)


  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local").setAppName("SodaModel") //创建环境变量
    val sc = new SparkContext(conf)  //创建环境变量实例
    val data = sc.textFile("hdfs://192.168.20.90:9000/soda/test/5.1-位置数据.csv")  //读取数据

    val userDays=data.filter(_.indexOf("IMEI,00:00-01:00")==(-1)).map(packageUserDay(_))
    val userDaysNum=userDays.collect().length
    log_.info("userDaysNum="+userDaysNum)

    val pointDetail=userDays.map(packagePointDetail(_))
    val pointDetailNum=pointDetail.collect().length
    log_.info("pointDetailNum="+pointDetailNum)

    //    processRdd(pointDetail);

  }

  def processRdd(rdd: RDD[Array[PointDetail]]) = {
    rdd.foreachPartition(partitionOfRecords=>{
      partitionOfRecords.foreach(record=>{
        record
      })
    })
  }

  def packagePointDetail(userDay:UserDay): Array[PointDetail] ={
    val buffer=new ArrayBuffer[PointDetail]()
    for(i <- 0 until userDay.trajectory.length){
      val rowKey=userDay.date+i+"uuid"+i
      val point=userDay.trajectory(i)
      if(point!=null){
        val pointDetail=new PointDetail(rowKey,new Basic("",point.longitude,point.latitude,"",userDay.date,i+""),new User(IdentityTypeEnum.IMEI,userDay.imei))
        buffer.+=(pointDetail)
      }
    }
    log_.info("packagePointDetail buffer="+buffer.size)
    configPrecursorAndNext(buffer).toArray
  }

  def configPrecursorAndNext(buffer: ArrayBuffer[PointDetail]) = {
    log_.info("========================================configPrecursorAndNext===================start==============================================================")
    if(buffer.size>0){
      //设置前驱precursorRowKey
      var precursorRowKey=buffer(0).rowkey
      for(i <- 1 until buffer.size){
        val current=buffer(i)
        current.basic.precursor=precursorRowKey
        precursorRowKey=current.rowkey
      }

      //设置next
      var nextRowKey=buffer.last.rowkey
      Range(buffer.size-2,-1,-1).foreach(i =>{
        val current=buffer(i)
        current.basic.next=nextRowKey
        nextRowKey=current.rowkey
      })
//      for(int i=rowData.size()-2;i>=0;i--){
//      }
    }else{
      log_.info("没有前驱后继可以设置,buffer="+buffer.size)
    }

    for(i <- 0 until buffer.size){
      log_.info("configPrecursorAndNext: i="+i+" PointDetail="+buffer(i))
    }
    log_.info("========================================configPrecursorAndNext===================end==============================================================")
    buffer
  }





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
          trajectory(i)=new Point(java.lang.Double.parseDouble(arr(index)),java.lang.Double.parseDouble(arr(index+1)))
        }
      }
      userDay=new UserDay(arr(1),arr(2),trajectory)
    }else{
      log_.info("数据格式不正确！line:"+line)
    }
    userDay
  }
  case class Point(longitude:Double,latitude:Double)   //latitude纬度
  case class UserDay(date:String,imei:String,trajectory:Array[Point])  //用户的一天.
}
