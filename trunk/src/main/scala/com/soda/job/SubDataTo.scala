package com.soda.job

import com.soda.common.SubData

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

/**
  * Created by Administrator on 2016/9/23.
  * 将地铁数据的位置信息转换为经纬度，并且组合起每个卡号每天的行为
  */
object SubDataTo {

  //
  def parseFile(file: String):Unit ={
    val file1 =Source.fromFile(file);
    val ab = new ArrayBuffer[SubData]();
    for(line <-file1.getLines()){
      val arr = line.split(",")
      val subdata = new SubData();
      subdata.setCardID(arr(0))
      subdata.setTradeDate(arr(1))
      subdata.setTradeTime(arr(2))
      subdata.setSubwayStation(arr(3))
      subdata.setIndustry(arr(4))
      subdata.setAmount(arr(5))
      subdata.setType(arr(6))
      ab.+=(subdata)
    }
  }

  def main(args: Array[String]) {
    parseFile("C:\\Users\\Administrator\\Desktop\\data.txt");
  }

}
