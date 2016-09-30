package com.soda.job

import java.sql.{DriverManager, PreparedStatement, Connection}
import java.util.Date
import com.soda.common.{ConstantsUtil, GridDivide}
import org.apache.spark.{SparkConf, SparkContext}
import scala.collection.mutable.ArrayBuffer

/**
  * 联通数据清洗任务   静态图：网格方框内人数统计
  *
  * /home/hadoop/tools/spark-1.6.0-bin-hadoop2.6/bin/spark-submit \
  * --master yarn \
  * --deploy-mode cluster \
  * --driver-memory 5g \
  * --executor-memory 5g \
  * --executor-cores 3 \
  * --num-executors 15 \
  * --jars /home/hadoop/sodatest/lib/mysql-connector-java-5.1.38.jar,/home/hadoop/sodatest/lib/hbase-server-1.2.0.jar,/home/hadoop/sodatest/lib/hbase-common-1.2.0.jar,/home/hadoop/sodatest/lib/hbase-protocol-1.2.0.jar,/home/hadoop/sodatest/lib/hbase-client-1.2.0.jar,/home/hadoop/sodatest/lib/guava-11.0.2.jar,/home/hadoop/sodatest/lib/htrace-core-3.1.0-incubating.jar,/home/hadoop/sodatest/lib/metrics-core-2.2.0.jar  \
  * --class com.soda.job.UnicomEtlStaticGridCountJob /home/hadoop/sodatest/soda-1.0-SNAPSHOT.jar >> soda.log
  *
  * Created by kcao on 2016/9/22.
  */
object UnicomEtlStaticGridCountJob{

  def main(args: Array[String]): Unit = {

    System.setProperty("spark.default.parallelism","60")
    val conf = new SparkConf().setAppName("UnicomEtlStaticGridCountJob") //创建环境变量
    conf.set("spark.default.parallelism","60")
    val sc = new SparkContext(conf)  //创建环境变量实例
    val data = sc.textFile("hdfs://192.168.20.90:9000/soda/fusai/表一结果.csv").cache()  //读取数据

    val userDays=data.map(_.replaceAll("\"","")).filter(_.indexOf("IMEI,00:00-01:00")==(-1)).map(packageUserDay(_))

    val pointTuple5=userDays.flatMap(packagePointToTuple5(_))

    val pointTuple5Count=pointTuple5.count()
    println(new Date()+" pointTuple5Count:"+pointTuple5Count)

    val pointTuple5Filter=pointTuple5.filter(tuple5=>GridDivide.checkPoint(tuple5._3,tuple5._4))

    val pointTuple5FilterCount=pointTuple5Filter.count()
    println(new Date()+" pointTuple5FilterCount:"+pointTuple5FilterCount)

    val groupByDateHourIndex=pointTuple5Filter.map(tuple5=>{(tuple5._1+"_"+tuple5._2+"_"+GridDivide.findIndex(tuple5._3,tuple5._4),1)}).reduceByKey(_+_)

    val groupByDateHourIndexCount=groupByDateHourIndex.count()
    println(new Date()+" groupByDateHourIndexCount:"+groupByDateHourIndexCount)

    groupByDateHourIndex.map(tuple2=>{
      val arr=tuple2._1.split("_")
      val date=arr(0)
      val hour=arr(1)
      val index=arr(2)
      val indexPoint=GridDivide.indexMap.get(index)
      (date,hour,index,indexPoint.x,indexPoint.y,tuple2._2)
    }).foreachPartition(toMySQL(_))

    println(new Date()+" UnicomEtlJob2 end!!!")

    sc.stop()
    System.exit(0)
  }

  def toMySQL(iterator: Iterator[(String, String,String, Double, Double, Int)]): Unit = {
    var conn: Connection = null
    var ps: PreparedStatement = null
    val sql ="insert into `soda`.`grid_divide_point_num2` (`date`, `hour`,`index`,`longitude`, `latitude`,`count`) values (?,?,?,?,?,?)";
    try {
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(ConstantsUtil.DB_URL, ConstantsUtil.DB_USERNAME, ConstantsUtil.DB_PASSWORD)
      iterator.foreach(dataIn => {
        ps = conn.prepareStatement(sql)
        ps.setString(1, dataIn._1)
        ps.setInt(2, Integer.parseInt(dataIn._2))
        ps.setInt(3, Integer.parseInt(dataIn._3))
        ps.setString(4, dataIn._4.toString)
        ps.setString(5, dataIn._5.toString)
        ps.setInt(6, dataIn._6)
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
//复赛下标
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
