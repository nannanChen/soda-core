package com.soda.kmeans

import com.soda.common.HashAlgorithmsUtil
import org.apache.spark
import org.apache.spark.mllib.linalg.Vectors

import scala.collection.mutable
import scala.collection.mutable.HashSet
import org.apache.spark.{SparkConf, SparkContext}
/**
  * Created by nan on 2016/9/29 10:50
  * 
  */

object Canopy {

  def main(args: Array[String]): Unit = {
    val t1 = 1d
    val t2 = 0.5d
    val conf = new SparkConf().setAppName("Canopy")
    val sc = new SparkContext(conf)
    val file = sc.textFile("hdfs://192.168.20.90:9000/soda/test/data.txt")

//      val pairs = file.map { line =>
//        val pair = line.split(",")
//        (pair(0), pair(1).split(",").map(_.toDouble))
//      }

    val pairs = file.filter(!isColumnNameLine(_)).filter((_.replace("\"","").split(",").length==29)).map(line=>{
      val splited= line.replaceAll("\"","").split(",")
      val iemi = HashAlgorithmsUtil.BKDRHash(splited(0))
      val brand = getBrand(splited(4))
      val x = line.replaceAll("\"","").replace(splited(0),iemi.toString).replaceFirst(splited(4),brand).replace(splited(5),"0").replace("男","0").replace("女","1")
        .replace("不详","2").replace("低","0").replace("中","0").replace("高","0").replace(splited(11),"0").replace(splited(12),"0")

      (x.split(",")(0),x.split(",").map(_.trim).filter(!"".equals(_)).map(_.toDouble)) //org.apache.spark.mllib.linalg.Vector
    })

      val map_centers = new HashSet[(String, Array[Double])]
      val raw_center_pairs = pairs.map(v =>
        (v._1,canopy_(v,map_centers,t2 ))).filter(a => a._2 != null).collect().toList
      val center_pairs = new HashSet[(String, Array[Double])]
      for (i <- 0 until raw_center_pairs.size) {
        canopy_(raw_center_pairs(i)._2, center_pairs, t2)
      }
    println("size:"+raw_center_pairs.size)
      sc.makeRDD(center_pairs.toList, 1).map { pair =>
        pair._1 + pair._2.mkString(",")
        print(pair._1 + pair._2.mkString(","))
      }.saveAsTextFile("hdfs://192.168.20.90:9000/soda/test/test.txt")

  }

  def measure(v1: Array[Double], v2: Array[Double]): Double = {
    var distance = 0.0
    val aa = if (v1.length < v2.length)
      v1.length
    else
      v2.length
    for (i <- 0 until aa) {
      distance += Math.pow((v1(i) - v2(i)), 2)
    }
    println("distance:"+distance)
    distance
  }

  def canopy_(p0: (String, Array[Double]), pair: HashSet[(String, Array[Double])], t2: Double): (String, Array[Double]) = {
    if (!pair.exists(p => measure(p._2, p0._2) < t2)) {
      pair += p0
      println("pair:"+pair)
      p0
    } else {
      null
    }
  }
  private def isColumnNameLine(line:String):Boolean = {
    if (line != null && line.contains("**")) true
    else false
  }
  private def getBrand(brand:String): String ={
    var b =""
    if(brand.equals("苹果"))
      b = "0"
    else
      b ="1"
    b
  }


}
