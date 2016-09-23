package com.soda.common

import com.soda.vo.PointDetail
import org.apache.hadoop.hbase.client.{Put, HTable}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.rdd.RDD

/**
  * Created by kcao on 2016/9/23.
  */
trait ConfigJob extends Serializable{

//  val log_ : Logger = Logger.getLogger(this.getClass)

  /**
    * 生成rowkey
    * @param date
    * @param time
    * @return
    */
  def createRowKey(date: String, time: Int):String={
    createRowKey(date,time)
  }

  def createRowKey(date: String, time: String):String={
    new StringBuffer(new ObjectId().toString).reverse().toString + date + time
  }

  /**
    * 填充每条轨迹的前驱后继
    *
    * @param buffer  每条轨迹
    * @return 每天轨迹
    */
  def configPrecursorAndNext(buffer: Array[PointDetail]) = {
//    log_.info("=============ConfigBean===========================configPrecursorAndNext===================start==============================================================")
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
      println("没有前驱后继可以设置,buffer="+buffer.size)
    }

//    for(i <- 0 until buffer.size){
//      log_.info("configPrecursorAndNext: i="+i+" PointDetail="+buffer(i))
//    }
    println("================ConfigBean========================configPrecursorAndNext===================end==========================================buffer="+buffer.size)
    buffer
  }


  /**
    * 处理rdd数据
    * rdd内部每一个元素代表一个对象【手机，公交卡。。。】一天的轨迹。
    * 每一条轨迹由数组组成，数组内每一个元素代表轨迹上一个点的详细情况
    *
    * @param rdd
    */
  def processRdd(rdd: RDD[Array[PointDetail]]) = {
    rdd.map(arr=>configPrecursorAndNext(arr)).foreachPartition(partitionOfRecords=>{
      partitionOfRecords.foreach(processArray(_))
    })
  }

  def processArray(records:Array[PointDetail]) = {
    val table: HTable = new HTable(HbaseUtil.getConfiguration,ConstantsUtil.POINT_DETAIL)
    records.foreach(detail=>{
      val put: Put = new Put(Bytes.toBytes(detail.rowkey))
      put.add(Bytes.toBytes("basic"), Bytes.toBytes("precursor"), Bytes.toBytes(detail.basic.precursor))
      put.add(Bytes.toBytes("basic"), Bytes.toBytes("longitude"), Bytes.toBytes(detail.basic.longitude+""))
      put.add(Bytes.toBytes("basic"), Bytes.toBytes("latitude"), Bytes.toBytes(detail.basic.latitude+""))
      put.add(Bytes.toBytes("basic"), Bytes.toBytes("next"), Bytes.toBytes(detail.basic.next))
      put.add(Bytes.toBytes("basic"), Bytes.toBytes("date"), Bytes.toBytes(detail.basic.date))
      put.add(Bytes.toBytes("basic"), Bytes.toBytes("time"), Bytes.toBytes(detail.basic.time))
      put.add(Bytes.toBytes("user"), Bytes.toBytes("valType"), Bytes.toBytes(detail.user.valType.toString))
      put.add(Bytes.toBytes("user"), Bytes.toBytes("value"), Bytes.toBytes(detail.user.value))
      table.put(put)
      println("detail.rowkey:"+detail.rowkey)
    })
  }

  /**
    * 所有的任务必须实现这个方法
    *
    * @param any
    * @return
    */
  def packagePointDetail(any: Any): Array[PointDetail]

}
