package com.soda

import com.soda.common.ObjectId
import org.apache.hadoop.hbase.util.Bytes

/**
  * Created by Administrator on 2016/9/23.
  */
object Test {

  def main(args: Array[String]) {
      val key="(12f79f6ea30c2016030112,20160301,4d7ba441d20c2016030111,121.505261,31.251893,37400169340c2016030113,nanJingDong,1640,121.505261,31.251893,413dc435e9fb201603011)"
    val arr=key.split(",")
    println(arr.length)

  }

}
