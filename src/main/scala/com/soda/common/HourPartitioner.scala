package com.soda.common

import org.apache.spark.Partitioner

/**
  * Created by kcao on 2016/9/29.
  */
class HourPartitioner(num: Int) extends Partitioner{
  override def numPartitions: Int = (num+1)

  override def getPartition(key: Any): Int = {
    var index=0
    index=Integer.parseInt(key.toString)
    if(index>num){
      index=24
    }
    index
  }
}
