package com.soda.job

import java.util.Date
import com.soda.common._
import com.soda.vo.{Basic, User, PointDetail}
import org.apache.spark.{SparkContext, SparkConf}
import scala.collection.mutable.ArrayBuffer

/**
  * 联通数据一级清洗任务  前驱后继点存储redis
  * /home/hadoop/tools/spark-1.6.0-bin-hadoop2.6/bin/spark-submit \
  * --master yarn \
  * --deploy-mode cluster \
  * --driver-memory 5g \
  * --executor-memory 5g \
  * --executor-cores 3 \
  * --num-executors 10 \
  * --jars /home/hadoop/sodatest/lib/jedis-2.8.1.jar,/home/hadoop/sodatest/lib/commons-pool2-2.4.2.jar  \
  * --class com.soda.job.UnicomOneEtlJob /home/hadoop/sodatest/soda-1.0-SNAPSHOT.jar >> soda.log
  *
  * Created by kcao on 2016/9/22.
  */
object UnicomOneEtlJob extends ConfigJob{


  def main(args: Array[String]): Unit = {

    System.setProperty("spark.default.parallelism","60")
    val conf = new SparkConf().setAppName("UnicomEtlJob") //创建环境变量
    conf.set("spark.default.parallelism","60")
    val sc = new SparkContext(conf)  //创建环境变量实例

    val data = sc.textFile("hdfs://192.168.20.90:9000/soda/fusai/表一结果.csv").cache()  //读取数据

    val userDays=data.map(_.replaceAll("\"","")).filter(_.indexOf("IMEI,00:00-01:00")==(-1)).map(packageUserDay(_))

    val pointDetail=userDays.map(packagePointDetail(_)).flatMap(configPrecursorAndNext(_))

    val jsonPotail=pointDetail.map(packageJson(_)).map(map=>{(map._1,Seq(map._2))}).reduceByKey(_.union(_))

    val jsonPotailCount=jsonPotail.count()
    println("reduceByKey jsonPotailCount2:"+jsonPotailCount)

//    jsonPotail.foreachPartition(part=>{
//      val pool = JedisClusterUtil.getJedisClusterPool
//      part.foreach(json=>{
//        pool.hset(ConstantsUtil.REDIS_POINT_DETAIL_UNICOM,json._1,json._2)
//      })
//    })

    sc.stop()
    System.exit(0)
  }

  def packageJson(detail: PointDetail): (String, String) = {
    val jsonObj=new org.json.JSONObject()
    val jsonBasic=new org.json.JSONObject()
    jsonObj.put("basic",jsonBasic)
    jsonBasic.put("date",detail.basic.date)
    jsonBasic.put("hour",detail.basic.hour)
    jsonBasic.put("precursor",detail.basic.precursor)
    jsonBasic.put("index",detail.basic.index)
    jsonBasic.put("latitude",detail.basic.latitude)
    jsonBasic.put("longitude",detail.basic.longitude)
    jsonBasic.put("next",detail.basic.next)
    val jsonUser=new org.json.JSONObject()
    jsonUser.put("valType",detail.user.valType.toString)
    jsonUser.put("value",detail.user.value)
    jsonObj.put("user",jsonUser)
    (detail.rowkey,jsonObj.toString)
  }

  override def packagePointDetail(any: Any): Array[PointDetail] ={
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
