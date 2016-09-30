package com.soda.job

import java.util.Date
import com.soda.common.{GridDivide, ConstantsUtil, ConfigJob, IdentityTypeEnum}
import com.soda.vo.{Basic, PointDetail,User}
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.mapred.JobConf
import org.apache.spark.{SparkContext, SparkConf}
import scala.collection.mutable.ArrayBuffer

/**
  * 联通数据清洗任务
  * /home/hadoop/tools/spark-1.6.0-bin-hadoop2.6/bin/spark-submit \
  * --master yarn \
  * --deploy-mode cluster \
  * --driver-memory 5g \
  * --executor-memory 5g \
  * --executor-cores 3 \
  * --num-executors 15 \
  * --jars /home/hadoop/sodatest/lib/hbase-server-1.2.0.jar,/home/hadoop/sodatest/lib/hbase-common-1.2.0.jar,/home/hadoop/sodatest/lib/hbase-protocol-1.2.0.jar,/home/hadoop/sodatest/lib/hbase-client-1.2.0.jar,/home/hadoop/sodatest/lib/guava-11.0.2.jar,/home/hadoop/sodatest/lib/htrace-core-3.1.0-incubating.jar,/home/hadoop/sodatest/lib/metrics-core-2.2.0.jar  \
  * --class com.soda.job.UnicomEtlJob /home/hadoop/sodatest/soda-1.0-SNAPSHOT.jar >> soda.log
  *
  * Created by kcao on 2016/9/22.
  */
object UnicomEtlJob extends ConfigJob{

  def main(args: Array[String]): Unit = {

    val hbaseWriteConf = HBaseConfiguration.create()
    val jobConf = new JobConf(hbaseWriteConf)
    jobConf.set("hbase.zookeeper.property.clientPort", "2181")
    jobConf.set("hbase.zookeeper.quorum", "zk1,zk2,zk3")
    jobConf.set("zookeeper.znode.parent","/hbase")
    jobConf.setOutputFormat(classOf[TableOutputFormat])
    jobConf.set(TableOutputFormat.OUTPUT_TABLE,ConstantsUtil.POINT_DETAIL)

    System.setProperty("spark.default.parallelism","60")
    val conf = new SparkConf().setAppName("UnicomEtlJob") //创建环境变量
    conf.set("spark.default.parallelism","60")
    val sc = new SparkContext(conf)  //创建环境变量实例
//    val data = sc.textFile("hdfs://192.168.20.90:9000/soda/test/5.1-位置数据.csv")  //读取数据
    val data = sc.textFile("hdfs://192.168.20.90:9000/soda/fusai/表一结果.csv").cache()  //读取数据

    val userDays=data.map(_.replaceAll("\"","")).filter(_.indexOf("IMEI,00:00-01:00")==(-1)).map(packageUserDay(_))

    val pointDetail=userDays.map(packagePointDetail(_)).flatMap(configPrecursorAndNext(_))

    val pointDetailPut=pointDetail.map(point=>{
      (new ImmutableBytesWritable,packagePut(point))
    })

    val pointDetailPutCount=pointDetailPut.count()
    println(new Date()+" pointDetailPutCount:"+pointDetailPutCount)

    pointDetailPut.saveAsHadoopDataset(jobConf)

    println(new Date()+" UnicomEtlJob end!!!")

    sc.stop()
    System.exit(0)
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
  //初赛下标
//  def packageUserDay(line:String): UserDay ={
//    var userDay: UserDay = null;
//    val arr=line.split(",");
//    if(arr.length>=3){
//      val trajectory:Array[Point]=new Array[Point](24);
//      for(i <- 0 to 23){
//        val index = i * 2 + 3;
//        if(index>=arr.length||"".equals(arr(index))){
//          trajectory(i)=null;
//        }else{
//          trajectory(i)=new Point(java.lang.Double.parseDouble(arr(index)),java.lang.Double.parseDouble(arr(index+1)))
//        }
//      }
//      userDay=new UserDay(arr(1),arr(2),trajectory)
//    }else{
//      println(new Date()+" 数据格式不正确！line:"+line)
//    }
//    userDay
//  }

  case class Point(longitude:Double,latitude:Double)   //latitude纬度
  case class UserDay(date:String,imei:String,trajectory:Array[Point])  //用户的一天.

}
