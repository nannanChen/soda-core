package com.soda.common

import com.soda.vo.PointDetail

/**
  * Created by kcao on 2016/9/23.
  */
trait ConfigJob extends Serializable{

  def createNewRowKey(date: String, phour: Int,pindex: Int,psuffix:String):String={
    var suffix=psuffix
    if(psuffix.length>10){
      suffix=psuffix.substring(0,10)
    }
    var hour=phour+""
    if(phour<10){
      hour="0"+phour
    }
    var index=pindex+""
    if(pindex<10){
      index="00"+pindex
    }
    if(pindex<100&&pindex>=10){
      index="0"+pindex
    }
    index + hour + date + suffix
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
      println("buffer.size:"+buffer.size)
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
//    println("================ConfigBean========================configPrecursorAndNext===================end==========================================buffer="+buffer.size)
    buffer
  }

  /**
    * 所有的任务必须实现这个方法
    *
    * @param any
    * @return
    */
  def packagePointDetail(any: Any): Array[PointDetail]

}
