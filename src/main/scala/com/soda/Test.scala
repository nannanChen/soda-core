package com.soda

import java.net.{URLDecoder, URLEncoder}

/**
  * Created by Administrator on 2016/9/23.
  */
object Test {

  def main(args: Array[String]) {

    val b="abc曹奎".getBytes("GBK")

    b.foreach(println(_))
    println("================")

    val b1=new String(b,"utf-8")
    println(b1)
    println("================")

    val bb=b1.getBytes("utf-8")
    bb.foreach(println(_))

    println("================")
    val b2=new String(bb,"GBK")
    println(b2)
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
