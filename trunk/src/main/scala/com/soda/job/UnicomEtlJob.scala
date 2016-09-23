package com.soda.job

import com.soda.common.{ConfigJob, ObjectId, IdentityTypeEnum}
import com.soda.vo.{Basic, PointDetail,User}
import org.apache.spark.{SparkContext, SparkConf}
import scala.collection.mutable.ArrayBuffer

/**
  * 联通数据清洗任务
  * spark-submit \
   --master spark://192.168.20.90:7077 \
   --deploy-mode client \
   --jars /home/hadoop/online/lib/htrace-core-3.0.4.jar,/home/hadoop/online/lib/hbase-common-0.99.2.jar,/home/hadoop/online/lib/hbase-protocol-0.99.2.jar,/home/hadoop/online/lib/hbase-client-0.99.2.jar,/home/hadoop/online/lib/commons-pool2-2.3.jar,/home/hadoop/online/lib/guava-12.0.1.jar  \
   --class com.soda.job.UnicomEtlJob /home/hadoop/sodatest/soda-1.0-SNAPSHOT.jar >> soda.log
  * Created by kcao on 2016/9/22.
  */
object UnicomEtlJob extends ConfigJob{

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local").setAppName("UnicomEtlJob") //创建环境变量
    val sc = new SparkContext(conf)  //创建环境变量实例
    val data = sc.textFile("hdfs://192.168.20.90:9000/soda/test/5.1-位置数据.csv")  //读取数据

    val userDays=data.filter(_.indexOf("IMEI,00:00-01:00")==(-1)).map(packageUserDay(_))
    val userDaysNum=userDays.collect().length
    println("userDaysNum="+userDaysNum)

    val pointDetail=userDays.map(packagePointDetail(_))
    val pointDetailNum=pointDetail.collect().length
    println("pointDetailNum="+pointDetailNum)

    processRdd(pointDetail);

    System.exit(0)
  }

  override def packagePointDetail(any: Any): Array[PointDetail] ={
    val userDay=any.asInstanceOf[UserDay]
    val buffer=new ArrayBuffer[PointDetail]()
    for(i <- 0 until userDay.trajectory.length){
      val point=userDay.trajectory(i)
      if(point!=null){
        val rowKey = createRowKey(userDay.date,i)
        val pointDetail=new PointDetail(rowKey,new Basic("",point.longitude,point.latitude,"",userDay.date,i+""),new User(IdentityTypeEnum.IMEI,userDay.imei))
        buffer.+=(pointDetail)
      }
    }
    println("packagePointDetail buffer="+buffer.size)
    buffer.toArray
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
      println("数据格式不正确！line:"+line)
    }
    userDay
  }
  case class Point(longitude:Double,latitude:Double)   //latitude纬度
  case class UserDay(date:String,imei:String,trajectory:Array[Point])  //用户的一天.

}
