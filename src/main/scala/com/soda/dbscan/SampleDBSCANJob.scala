package com.soda.dbscan

import java.util.Date

import com.soda.common.{IdentityTypeEnum, GridDivide}
import com.soda.job.UnicomOneEtlJob._
import com.soda.vo.{User, Basic, PointDetail, GridDetail}
import org.apache.spark.mllib.clustering.dbscan.DBSCAN
import org.apache.spark.mllib.linalg.Vectors
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
  * --class com.soda.dbscan.SampleDBSCANJob /home/hadoop/sodatest/soda-1.0-SNAPSHOT.jar 0.3 10 250 12
  *
  */
object SampleDBSCANJob {

  val log = LoggerFactory.getLogger(SampleDBSCANJob.getClass)

  def main(args: Array[String]) {

    if (args.length != 4) {
      System.err.println("请输入正确的参数列表: <eps> <minPoints> <maxPointsPerPartition> <hour>")
      System.exit(1)
    }

    val (eps, minPoints,maxPointsPerPartition,hour) =
      (args(0).toFloat,args(1).toInt,args(2).toInt,args(3).toInt)

    println("eps="+eps+" minPoints="+minPoints+" maxPointsPerPartition="+maxPointsPerPartition)

    val conf = new SparkConf().setAppName("SampleDBSCANJob")
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    // conf.set("spark.storage.memoryFraction", "0.1")
    val sc = new SparkContext(conf)

//    val data = sc.textFile("hdfs://192.168.20.90:9000/soda/test/dbscan_data2.csv")
    val data = sc.textFile("hdfs://192.168.20.90:9000/soda/fusai/表一结果.csv")  //读取数据

    val userDays=data.map(_.replaceAll("\"","")).filter(_.indexOf("IMEI,00:00-01:00")==(-1)).map(packageUserDay(_))

    val hourPointDetail=userDays.flatMap(packagePointDetail(_)).filter(_.basic.hour==hour).map(detail=>{
      detail.basic.longitude+","+detail.basic.latitude
    })

    val hourPointDetailCount=hourPointDetail.count()
    println("hourPointDetailCount="+hourPointDetailCount)

    val parsedData = hourPointDetail.map(s => Vectors.dense(s.split(',').map(_.toDouble)))

    log.info(s"EPS: $eps minPoints: $minPoints")

    val model = DBSCAN.train(
      parsedData,
      eps = eps,
      minPoints = minPoints,
      maxPointsPerPartition = maxPointsPerPartition)

//    model.labeledPoints.map(p => s"${p.x}|${p.y}|${p.cluster}").coalesce(1).saveAsTextFile("hdfs://192.168.20.90:9000/soda/dbscan")
    model.labeledPoints.map(p=>{(p.cluster,1)}).reduceByKey(_+_).coalesce(1).collect().foreach(println(_))

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
          val point=new Point(java.lang.Double.parseDouble(arr(index)),java.lang.Double.parseDouble(arr(index+1)))
          if(GridDivide.checkPoint(point.longitude,point.latitude)){
            trajectory(i)=point
          }else{
            trajectory(i)=null;
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
  case class UserDay(date:String,imei:String,trajectory:Array[Point])  //用户的一天.
}
