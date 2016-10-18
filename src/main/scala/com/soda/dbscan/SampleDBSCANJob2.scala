package com.soda.dbscan

import java.sql.{PreparedStatement, Connection}
import java.util.Date
import com.soda.common.{XYData, DataSourceUtil, IdentityTypeEnum, GridDivide}
import com.soda.job.UnicomOneEtlJob._
import com.soda.vo.{User, Basic, PointDetail}
import org.alitouka.spark.dbscan.{ClusterId, Dbscan, DbscanSettings}
import org.alitouka.spark.dbscan.spatial.rdd.PartitioningSettings
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
  * --jars /home/hadoop/sodatest/lib/mysql-connector-java-5.1.38.jar  \
  * --class com.soda.dbscan.SampleDBSCANJob2 /home/hadoop/sodatest/soda-1.0-SNAPSHOT.jar 0.0175 20 12
  *
  */
object SampleDBSCANJob2 {

  val log = LoggerFactory.getLogger(SampleDBSCANJob.getClass)

  def main(args: Array[String]) {

    if (args.length != 3) {
      System.err.println("请输入正确的参数列表: <eps> <minPoints> <hour>")
      System.exit(1)
    }

    val (eps, minPoints,hour) =
      (args(0).toFloat,args(1).toInt,args(2).toInt)

    println("eps="+eps+" minPoints="+minPoints+" hour="+hour)

    val conf = new SparkConf().setAppName("SampleDBSCANJob2")
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    // conf.set("spark.storage.memoryFraction", "0.1")
    val sc = new SparkContext(conf)

//    val data = sc.textFile("hdfs://192.168.20.90:9000/soda/test/dbscan_data2.csv")
    val data = sc.textFile("hdfs://192.168.20.90:9000//soda/test/5.1-位置数据.csv")  //读取数据

    val userDays=data.map(_.replaceAll("\"","")).filter(_.indexOf("IMEI,00:00-01:00")==(-1)).map(packageUserDay(_))

    val allPointDetail=userDays.flatMap(packagePointDetail(_)).map(detail=>{
      detail.basic.longitude+","+detail.basic.latitude
    })

    val allPointDetailCount=allPointDetail.count()
    println("allPointDetailCount="+allPointDetailCount)

    val dataList=allPointDetail.map (
      line => {
        new org.alitouka.spark.dbscan.spatial.Point (line.split(",").map( _.toDouble))
      }
    )

    val settings = new DbscanSettings ()
      .withEpsilon(eps)
      .withNumberOfPoints(minPoints)
      .withTreatBorderPointsAsNoise(DbscanSettings.getDefaultTreatmentOfBorderPoints)
      .withDistanceMeasure(new org.apache.commons.math3.ml.distance.ManhattanDistance())

    val partitioningSettings = new PartitioningSettings (numberOfPointsInBox = minPoints)

    val clusteringResult = Dbscan.train(dataList, settings, partitioningSettings)

//    clusteringResult.allPoints.map ( pt => {
//      (pt.clusterId,pt.coordinates.mkString(","))
//    }).groupByKey().map(tuple2=>{(tuple2._1,tuple2._2.size)}).coalesce(1).saveAsTextFile("hdfs://192.168.20.90:9000/soda/dbscan6")

//    clusteringResult.allPoints.map(p=>{(p.clusterId,1)}).reduceByKey(_+_).coalesce(1).collect().foreach(println(_))

    clusteringResult.allPoints.map ( pt => {
      (pt.clusterId,pt.coordinates.array(0),pt.coordinates.array(1))
    }).foreachPartition(toMysql(_))

    log.info("Stopping Spark Context...")
    sc.stop()

  }

  def toMysql(iterator: Iterator[(ClusterId, Double, Double)]): Unit = {
    var conn: Connection = null
    var ps: PreparedStatement = null
    val sqlMaster ="insert into `soda`.`dbscan_point` (`clusterId`, `x`,`y`) values (?,?,?)"
    try {
      Class.forName("com.mysql.jdbc.Driver")
      conn=DataSourceUtil.dataSource.getConnection
      iterator.foreach(tuple3 => {
        ps = conn.prepareStatement(sqlMaster)
        ps.setInt(1, tuple3._1.toInt)
        ps.setString(2, tuple3._2.toString)
        ps.setString(3, tuple3._3.toString)
        ps.executeUpdate()
      })
    }finally {
      if (ps != null) {
        ps.close()
      }
      if (conn != null) {
        conn.close()
      }
    }
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
