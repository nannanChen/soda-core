package com.soda.job

import java.util.Date

import com.soda.common.{GridDivide, ConstantsUtil, IdentityTypeEnum, ConfigJob}
import com.soda.redis.RedisService
import com.soda.vo.{User, Basic, PointDetail}
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.mapred.JobConf
import org.apache.spark.{SparkContext, SparkConf}
import scala.collection.mutable.ArrayBuffer
/**
  * Created by nan on 2016/9/26 16:41
  * 将地铁数据的位置信息转换为经纬度，并且组合起每个卡号每天的行为
  */
/**在192.168.20.90上执行
  * spark-submit \
  * --master spark://192.168.20.90:7077 \
  * --deploy-mode client \
  * --jars /home/hadoop/online/lib/hbase-server-1.2.0.jar,/home/hadoop/online/lib/hbase-protocol-1.2.0.jar,/home/hadoop/online/lib/guava-12.0.1.jar,/home/hadoop/online/lib/htrace-core-3.1.0-incubating.jar,/home/hadoop/online/lib/metrics-core-2.2.0.jar,/home/hadoop/online/lib/commons-pool2-2.3.jar,/home/hadoop/online/lib/hbase-common-1.2.0.jar,/home/hadoop/online/lib/hbase-client-1.2.0.jar,/home/hadoop/online/lib/jedis-2.8.1.jar  \
  * --class com.soda.job.SubEtlJob /home/hadoop/nannan/soda-1.0-SNAPSHOT.jar >> soda.log

  * on yarn
  * spark-submit \
  * --master yarn \
  * --deploy-mode cluster \
  * --driver-memory 5g \
  * --executor-memory 5g \
  * --executor-cores 2 \
  * --num-executors 3 \
  * --jars /home/hadoop/online/lib/hbase-server-1.2.0.jar,/home/hadoop/online/lib/hbase-protocol-1.2.0.jar,/home/hadoop/online/lib/guava-12.0.1.jar,/home/hadoop/online/lib/htrace-core-3.1.0-incubating.jar,/home/hadoop/online/lib/metrics-core-2.2.0.jar,/home/hadoop/online/lib/commons-pool2-2.3.jar,/home/hadoop/online/lib/hbase-common-1.2.0.jar,/home/hadoop/online/lib/hbase-client-1.2.0.jar,/home/hadoop/online/lib/jedis-2.8.1.jar  \
  * --class com.soda.job.SubEtlJob /home/hadoop/nannan/soda-1.0-SNAPSHOT.jar >> soda.log

  */

object SubEtlJob extends ConfigJob {

    def main(args: Array[String]) {

      val hbaseConf = HBaseConfiguration.create()
      hbaseConf.set("hbase.zookeeper.property.clientPort", "2181")
      hbaseConf.set("hbase.zookeeper.quorum", "zk1,zk2,zk3")
      val jobConf = new JobConf(hbaseConf)
      System.setProperty("spark.default.parallelism","12")//stages 最大使用线程数量=executor-cores*num-executors的2或3倍
      jobConf.setOutputFormat(classOf[TableOutputFormat])
      jobConf.set(TableOutputFormat.OUTPUT_TABLE,ConstantsUtil.POINT_DETAIL)

//      val conf = new SparkConf().setMaster("local").setAppName("SubEtlJob")//创建环境变量 client
      val conf = new SparkConf().setAppName("SubEtlJob3.1")
      conf.set("spark.default.parallelism","12")
      val sc = new SparkContext(conf)  //创建环境变量实例
      val start = System.currentTimeMillis()
      //trafficData.csv
      println("-----------------START---------------------")
            val data = sc.textFile("hdfs://192.168.20.90:9000/soda/fusai/交通卡数据2016.3/SPTCC-20160301.csv")  //读取数据
//      val data = sc.textFile("hdfs://192.168.20.90:9000/soda/test/SubwayTest.txt")
      RedisService.parseStat(data)
      println("STARTetlSubData------------------------")
      //公交卡对象
      val pairs = data.map(_.split(",")).filter(_.length==7).map(formatLine(_)).filter(_.latitude!=(-1d)).filter(x=>{
        GridDivide.checkPoint(x.longitude,x.latitude)
      })
      println("开始分组---------------------")
      val group = pairs.map(card=>{(card.cardID+card.date,Seq(card))}).reduceByKey(_.union(_)).map(g=>{
        g._2.sortBy(_.time)
      }).map(packagePointDetail(_)).flatMap(configPrecursorAndNext(_))
      println("66666分组结束-------------------开始存入HBase")

//      val pointDetailPut=pointDetail.map(point=>{
//        (new ImmutableBytesWritable,packagePut(point))
//      })
      val pointDetailPut = group.map(detail=>{
        (new ImmutableBytesWritable,packagePut(detail))
      })
//  .saveAsHadoopDataset(jobConf)

      val pointDetailPutCount=pointDetailPut.count()
      println(new Date()+" pointDetailPutCount:"+pointDetailPutCount)

      pointDetailPut.saveAsHadoopDataset(jobConf)

      print("共花费:"+(System.currentTimeMillis()-start)/60000+"min")
    }
  /**
    *
    * @param splited 每一行要格式化的数据  数组类型  已经被分割过滤长度
    */

  def formatLine(splited:Array[String]):CardDay ={
    //对日期做处理
    val date = splited(1).split("-")
    var dateend = ""
    if (date(1).length == 1 && date(2).length == 1) {
      dateend = date(0) + "0" + date(1) + "0" + date(2)
    } else if (date(1).length == 1 && date(2).length == 2) {
      dateend = date(0) + "0" + date(1) + date(2)
    } else if (date(1).length == 2 && date(2).length == 1) {
      dateend = date(0) + date(1) + "0" + date(2)
    } else {
      dateend = date(0) + date(1) + date(2)
    }
    //对时间做处理
    val timeend = splited(2).split(":")(0).toInt
    //对经纬度做转换
    var longitude = ""
    var latitude = ""
    val map = RedisService.getlongitudeandlatitudeFromRedis("上海" + splited(3))
    if (map.isEmpty) {
      longitude ="-1"
      latitude ="-1"
      println("lng:"+longitude+"\tlat:"+latitude)
    } else {
      longitude = map.get("lng").toString
      latitude = map.get("lat").toString
    }
    new CardDay(splited(0), dateend, timeend, java.lang.Double.parseDouble(longitude), java.lang.Double.parseDouble(latitude), splited(4), splited(5), splited(6))
  }
  /**
    * 所有的任务必须实现这个方法
    *
    * @param any
    * @return
    */
  override def packagePointDetail(any: Any): Array[PointDetail] = {
    val list=any.asInstanceOf[Seq[CardDay]]
    val buffer=new ArrayBuffer[PointDetail]()
    list.foreach(cardDay=>{
      val rowKey = createNewRowKey(cardDay.date,cardDay.time,getIndex(cardDay.longitude,cardDay.latitude),cardDay.cardID)
      println("---------------------"+rowKey)
      val pointDetail=new PointDetail(rowKey,new Basic("",cardDay.longitude,cardDay.latitude,"",cardDay.date,cardDay.time,getIndex(cardDay.longitude,cardDay.latitude)),new User(IdentityTypeEnum.CARDID,cardDay.cardID))
      buffer.+=(pointDetail)
      println("packagePointDetail buffer="+buffer.size)
    })
    buffer.toArray
  }

  def getIndex(lng:Double,lat:Double): Int = {
    GridDivide.findIndex(lng,lat)
  }

  case class Point(longitude:Double,latitude:Double)
  case class CardDay(cardID:String,date:String,time:Int,longitude:Double,latitude:Double,industy:String,amount:String,typ:String)
}
