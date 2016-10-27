package com.soda.job

import java.sql.{PreparedStatement, Connection}
import java.util.Date
import com.soda.common._
import com.soda.vo.{GridDetail}
import org.apache.hadoop.hbase.util.{Bytes, MD5Hash}
import org.apache.spark.{SparkConf, SparkContext}
import scala.collection.mutable.ArrayBuffer

/**
  * 联通数据清洗任务
  * /home/hadoop/tools/spark-1.6.0-bin-hadoop2.6/bin/spark-submit \
  * --master yarn \
  * --deploy-mode cluster \
  * --driver-memory 5g \
  * --executor-memory 5g \
  * --executor-cores 3 \
  * --num-executors 10 \
  * --jars /home/hadoop/sodatest/lib/jedis-2.8.1.jar,/home/hadoop/sodatest/lib/commons-pool2-2.4.2.jar,/home/hadoop/sodatest/lib/hbase-common-1.2.0.jar,/home/hadoop/sodatest/lib/mysql-connector-java-5.1.38.jar  \
  * --class com.soda.job.UnicomTwoEtlJob /home/hadoop/sodatest/soda-1.0-SNAPSHOT.jar >> soda.log
  *
  * Created by kcao on 2016/9/22.
  */
object UnicomTwoEtlJob{

  def main(args: Array[String]): Unit = {

    System.setProperty("spark.default.parallelism","60")
    val conf = new SparkConf().setAppName("UnicomEtlJob") //创建环境变量
    conf.set("spark.default.parallelism","60")
    val sc = new SparkContext(conf)  //创建环境变量实例

    val data = sc.textFile("hdfs://192.168.20.90:9000/soda/fusai/表一结果.csv").cache()  //读取数据

    val userDays=data.map(_.replaceAll("\"","")).filter(_.indexOf("IMEI,00:00-01:00")==(-1)).map(packageUserDay(_))

    val gridDetail=userDays.flatMap(packageGridDetail(_))

    val groupDHFT=gridDetail.map(grid=>(grid.date+"-"+grid.hour+":"+grid.fromIndex+"->"+grid.toIndex,Seq(grid.imei))).reduceByKey(_.union(_)).cache()

//    val gridFromToNum2=groupDHFT.map(packageGridFromToNum2(_))
//    val gridFromToNum2Count=gridFromToNum2.count()
//    println(new Date()+"gridFromToNum2Count:"+gridFromToNum2Count)
//    gridFromToNum2.foreachPartition(toMySqlGridFromToNum2(_))

//    val gridPeopleGroup2=groupDHFT.flatMap(packagePeopleGroup2(_))
//    val gridPeopleGroup2Count=gridPeopleGroup2.count()
//    println(new Date()+"gridPeopleGroup2Count:"+gridPeopleGroup2Count)
//    gridPeopleGroup2.foreachPartition(toGridPeopleGroup2(_))

    val gridImeiDetail=groupDHFT.flatMap(packageImeiDetail(_)).cache()
    val gridImeiDetailCount=gridImeiDetail.count()
    println(new Date()+"gridImeiDetailCount:"+gridImeiDetailCount)
//    gridImeiDetail.foreachPartition(toGridImeiDetail(_))
    gridImeiDetail.saveAsTextFile("hdfs://192.168.20.90:9000/soda/mysql/GridImeiDetail1")

    sc.stop()
    System.exit(0)
  }


  def toMySqlGridFromToNum2(iterator: Iterator[(String, String, String, String, Int,String)]): Unit = {
    var conn: Connection = null
    var ps: PreparedStatement = null
    val sqlMaster ="insert into `soda`.`grid_from_to_num1` (`date`, `hour`,`from_index`,`to_index`,`count`,`grid_people_group_id`) values (?,?,?,?,?,?)"
    try {
      Class.forName("com.mysql.jdbc.Driver")
      conn=DataSourceUtil.dataSource.getConnection
      iterator.foreach(tuple7 => {
        ps = conn.prepareStatement(sqlMaster)
        ps.setString(1, tuple7._1)
        ps.setInt(2, Integer.parseInt(tuple7._2))
        ps.setInt(3, Integer.parseInt(tuple7._3))
        ps.setInt(4, Integer.parseInt(tuple7._4))
        ps.setInt(5, (tuple7._5*XYData.xyDataMap.get(tuple7._1).intValue()))
        ps.setString(6, tuple7._6)
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

  def toGridPeopleGroup2(iterator: Iterator[(String,String,Int,String)]): Unit = {
    var conn: Connection = null
    var ps: PreparedStatement = null
    val sqlSlave ="insert into `soda`.`grid_people_group1` (`grid_people_group_id`, `type`,`count`) values (?,?,?)"
    try {
      Class.forName("com.mysql.jdbc.Driver")
      conn=DataSourceUtil.dataSource.getConnection
      iterator.foreach(tuple4 => {
        ps = conn.prepareStatement(sqlSlave)
        ps.setString(1,tuple4._1)
        ps.setString(2,tuple4._2)
        ps.setInt(3, (tuple4._3*XYData.xyDataMap.get(tuple4._4).intValue()))
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

  def toGridImeiDetail(iterator: Iterator[(String,String,String)]): Unit = {
    var conn: Connection = null
    var ps: PreparedStatement = null
    val sqlSlave ="insert into `soda`.`grid_imei_detail` (`grid_people_group_id`,`type`, `imei`) values (?,?,?)"
    try {
      Class.forName("com.mysql.jdbc.Driver")
      conn=DataSourceUtil.dataSource.getConnection
      iterator.foreach(tuple3 => {
        ps = conn.prepareStatement(sqlSlave)
        ps.setString(1,tuple3._1)
        ps.setString(2,tuple3._2)
        ps.setString(3,tuple3._3)
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

  def packageGridFromToNum2(kv: (String,Seq[String])): (String, String, String, String, Int,String) = {
    val key=kv._1.split(":")
    val date=key(0).split("-")(0)
    val hour=key(0).split("-")(1)
    val fromIndex=key(1).split("->")(0)
    val toIndex=key(1).split("->")(1)
    val grid_people_id=MD5Hash.getMD5AsHex(Bytes.toBytes(kv._1))
    (date,hour,fromIndex,toIndex,kv._2.size,grid_people_id)
  }

  def packagePeopleGroup2(kv: (String,Seq[String])): Array[(String,String,Int,String)] = {
    val key=kv._1.split(":")
    val date=key(0).split("-")(0)
    val buffer=new ArrayBuffer[(String,String,Int,String)]()
    val grid_people_id=MD5Hash.getMD5AsHex(Bytes.toBytes(kv._1))
    kv._2.map(imei=>{
      var typeIndex=JedisClusterUtil.getJedisClusterPool().hget("iemiTagThree",imei)
      if("".equals(typeIndex)||"null".equals(typeIndex)||typeIndex==null){
        typeIndex="0"
      }
      (typeIndex,imei)
    }).groupBy(_._1).map(kv=>{(kv._1,kv._2.size)}).foreach(kvv=>{
      buffer.+=((grid_people_id,kvv._1,kvv._2,date))
    })
    buffer.toArray
  }

  def packageImeiDetail(kv: (String,Seq[String])): Array[String] = {
    val buffer=new ArrayBuffer[String]()
    val grid_people_id=MD5Hash.getMD5AsHex(Bytes.toBytes(kv._1))
    kv._2.foreach(imei=>{
      var typeIndex=JedisClusterUtil.getJedisClusterPool().hget("iemiTagThree",imei)
      if("".equals(typeIndex)||"null".equals(typeIndex)||typeIndex==null){
        typeIndex="0"
      }
      buffer.+=(grid_people_id+","+typeIndex+","+imei)
    })
    buffer.toArray
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
