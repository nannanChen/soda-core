package com.soda.job

import org.apache.spark.{SparkContext, SparkConf}

/**
  * Created by kcao on 2016/9/22.
  */
object SodaEtlJob {

  def main(args: Array[String]): Unit = {
    val ss="981,20160301,a00bffc8abf39457c8304ab1c391db3a,,,,,,,,,,,,,,,,,,,,,,,121.4474,31.23904001,,,,,,,,,,,,,,,,,,,121.44869,31.19548001,,,,"
    val sss=ss.split(",");
    println(sss.length)

    val conf = new SparkConf().setMaster("local").setAppName("SodaModel") //创建环境变量
    val sc = new SparkContext(conf)  //创建环境变量实例
    val data = sc.textFile("hdfs://192.168.20.90:9000/soda/test/5.1-位置数据.csv")  //读取数据

    val userDays=data.filter(_.indexOf("IMEI,00:00-01:00")==(-1)).map(line=>{
        var userDay: UserDay = null;
        val arr=line.split(",");
        if(arr.length>=3){
          val trajectory:Array[Point]=new Array[Point](24);
          for(i <- 0 to 23){
            val index = i * 2 + 3;
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
    })


    userDays.filter(_!=null).collect().length
  }


  case class Point(longitude:Double,latitude:Double)   //latitude纬度
  case class UserDay(date:String,imei:String,trajectory:Array[Point])  //用户的一天
}
