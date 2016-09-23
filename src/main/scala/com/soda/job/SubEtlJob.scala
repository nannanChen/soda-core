package com.soda.job

import com.soda.common.{IdentityTypeEnum, ObjectId, ConfigJob, SubData}
import com.soda.job.UnicomEtlJob._
import com.soda.service.SubwayImpl
import com.soda.vo.{User, Basic, PointDetail}
import org.apache.spark.{SparkContext, SparkConf}

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

/**
  * Created by Administrator on 2016/9/23.
  * 将地铁数据的位置信息转换为经纬度，并且组合起每个卡号每天的行为
  */
object SubEtlJob extends ConfigJob {

  //
  def parseFile(file: String):Unit = {
  }
    def main(args: Array[String]) {
      val subwayImpl = new SubwayImpl
      val conf = new SparkConf().setMaster("local").setAppName("SodaModel") //创建环境变量
      val sc = new SparkContext(conf)  //创建环境变量实例
      val data = sc.textFile("hdfs://192.168.20.90:9000/soda/test/data.txt")  //读取数据
      println("----------------START---------------------------")
      val pairs = data.map{ x => {
          val splited = x.split(",")
          //对时间做处理
          var time =splited(2).split(":")
          if(time(0).length==1)
          time(0) = "0"+time(0);
          val timeend = time(0)
          //对经纬度做转换
          println("station----------------"+splited(3))
          val map = subwayImpl.getlongitudeandlatitude("上海"+splited(3))
        println("---------map-----------"+map)
          val longitude = map.get("lng").toString
          val latitude = map.get("lat").toString

          val one = new CardDay(splited(0),splited(1),timeend,java.lang.Double.parseDouble(longitude),java.lang.Double.parseDouble(latitude),splited(4),splited(5),splited(6))
          (one.date,one)
        }
      }
      //按天分
      val groupDate=pairs.groupByKey();
      val tmp = groupDate.map(x=>{
        val temp = x._2.toList.sortBy(b=>b.time)map((a=>{(a.cardID,a)}))
        temp.groupBy(kv=>{kv._1})
      });
      //按卡号分
      tmp.foreach(f=>{
        f.foreach(ff=>{
          ff._2.map(a=>{packagePointDetail(a._2)}).foreach(processArray(_))
        })
      })
    }

  /**
    * 所有的任务必须实现这个方法
    *
    * @param any
    * @return
    */
  override def packagePointDetail(any: Any): Array[PointDetail] = {
    val cardDay=any.asInstanceOf[CardDay]
    val buffer=new ArrayBuffer[PointDetail]()
    val rowKey = createRowKey(cardDay.date,cardDay.time)
    println("---------------------"+rowKey)
    val pointDetail=new PointDetail(rowKey,new Basic("",cardDay.longitude,cardDay.latitude,"",cardDay.date,cardDay.time),new User(IdentityTypeEnum.CARDID,cardDay.cardID))
    buffer.+=(pointDetail)
    println("packagePointDetail buffer="+buffer.size)
    buffer.toArray
    buffer.toArray
  }
  case class Point(longitude:Double,latitude:Double)
  case class CardDay(cardID:String,date:String,time:String,longitude:Double,latitude:Double,industy:String,amount:String,typ:String)
}
