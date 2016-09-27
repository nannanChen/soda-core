package com.soda

import com.soda.common.ObjectId
import org.apache.hadoop.hbase.util.Bytes

/**
  * Created by Administrator on 2016/9/23.
  */
object Test {

  def main(args: Array[String]) {
//      val key="20160315,da7eb05faecef79d2077ec48c6d26f24,,,,,,,,,,,,,,,,,,,,,,,,,121.40727,31.29040001,,,,,,,,,,,,,,,,,,,,,,"
    val key="20160318,abf9cef634641461eabae2a13bd68410,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"
    val arr=key.replaceAll("\"","")
    println(arr.split(",").length)
    val userday=packageUserDay(arr)
    println(userday)

  }

  def packageUserDay(line:String): UserDay ={
    println(line)
    var userDay: UserDay = null;
    val arr=line.split(",");
    if(arr.length>=3){
      val trajectory:Array[Point]=new Array[Point](24);
      for(i <- 0 to 23){
        val index = i * 2 + 2;
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
