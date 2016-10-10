package com.soda.kmeans

import com.soda.common.HashAlgorithmsUtil
import com.soda.redis.RedisService
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.immutable.HashMap

/**
  * Created by nan on 2016/9/27 9:43
  *
  */

/**
  *
   spark-submit \
   --master yarn \
   --deploy-mode cluster \
   --driver-memory 5g \
   --executor-memory 5g \
   --executor-cores 2 \
   --num-executors 3 \
   --jars /home/hadoop/online/lib/spark-mllib_2.10-1.5.2.jar,/home/hadoop/online/lib/jedis-2.8.1.jar,/home/hadoop/online/lib/commons-pool2-2.3.jar \
   --class com.soda.kmeans.KmeansInterTwo /home/hadoop/nannan/KmeansTwo/soda-1.0-SNAPSHOT.jar \
   >>soda.log
  */

/**
   spark-submit \
   --master spark://192.168.20.90:7077 \
   --deploy-mode client \
   --jars /home/hadoop/online/lib/spark-mllib_2.10-1.5.2.jar,/home/hadoop/online/lib/jedis-2.8.1.jar,/home/hadoop/online/lib/commons-pool2-2.3.jar \
   --class com.soda.kmeans.KmeansInterTwo /home/hadoop/nannan/KmeansTwo/soda-1.0-SNAPSHOT.jar \
   >>soda.log
  */
object KmeansInterTwo {
  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("KmeasInterTwo")
    val sc = new SparkContext(conf)
//    sc.setLocalProperty("file.encoding","GB2312")
    val rawTrainingData = sc.textFile("hdfs://192.168.20.90:9000/soda/fusai/biaoerutf8_1.csv")

    val parsedTrainingData = rawTrainingData.filter(!isColumnNameLine(_)).filter((_.replace("\"","").split(",").length==29)).map(line=>{
      val splited= line.replaceAll("\"","").split(",")
      val iemi = HashAlgorithmsUtil.BKDRHash(splited(0))
      val brand = getBrand(splited(4))
      val x = line.replaceAll("\"","").replace(splited(0),iemi.toString).replaceFirst(splited(4),brand).replace(splited(5),"0").replace("男","0").replace("女","1")
        .replace("不详","2").replace("低","0").replace("中","1").replace("高","2").replace(splited(11),"0").replace(splited(12),"0")
      val y = x.split(",").drop(6).mkString(",")
        Vectors.dense(y.split(",").map(_.trim).filter(!"".equals(_)).map(_.toDouble)) //org.apache.spark.mllib.linalg.Vector
    })

    val map1=rawTrainingData.filter(!isColumnNameLine(_)).filter((_.replace("\"","").split(",").length==29)).map(line=>{
      val splited= line.replaceAll("\"","").split(",")
      val iemi = HashAlgorithmsUtil.BKDRHash(splited(0))
      val brand = getBrand(splited(4))
      val x = line.replaceAll("\"","").replace(splited(0),iemi.toString).replaceFirst(splited(4),brand).replace(splited(5),"0").replace("男","0").replace("女","1")
        .replace("不详","2").replace("低","0").replace("中","1").replace("高","2").replace(splited(11),"0").replace(splited(12),"0")
      val y = x.split(",").drop(6).mkString(",")
      (splited(0),y.split(",").map(_.trim).filter(!"".equals(_)).map(_.toDouble)) //org.apache.spark.mllib.linalg.Vector
    })


    val numClusters = Array(10)
    val numIteration = 10
    val runTimes = 3

    println("----------------START-------------------")
    numClusters.foreach(f=>{
      kmeans(parsedTrainingData,f,numIteration,map1)
    })
    /**
      * 根据以上结果输入数据
      */
    println("Spark MLlib K-means clustering test finished.")
  }
  def kmeans(parsedTrainingData: RDD[Vector], numClusters:Int, numIteration:Int, map:RDD[(String,Array[Double])]): Unit ={
    var clusterIndex = 0
    val cluster = KMeans.train(parsedTrainingData,numClusters,numIteration)
    println("类簇 Number:" + cluster.clusterCenters.length)
    println("类簇 Centers Information Overvies:")
    cluster.clusterCenters.foreach(x=>{
      println("类簇 Center Point:"+clusterIndex +":")
      println(x)
      clusterIndex += 1
    })
    parsedTrainingData.collect().foreach(testDataLine => {
      testDataLine.toArray.mkString(",")
//     val map2 = map.foreach(x=>{
//        println("x._1:"+x._1+"\tx._2:"+x._2.mkString(","))
//      })
//
//      println("map:"+map2)
      val predictedClusterIndex = cluster.predict(testDataLine)
      val iemi = map.filter(f=>{
        val s1="["+f._2.mkString(",")+"]";
        val s2=testDataLine.toString
        val rs=s1.equals(s2)
//        println("s1="+s1+" equals s2="+s2+" rs="+rs)
        rs
      }).map(_._1).foreach(x=>{
        RedisService.addTag(x,predictedClusterIndex+"")
      })
//      println("iemi:"+iemi)
//      println("-------这条数据:"+testDataLine.toString + "属于:"+predictedClusterIndex)
    })
//    val ks:Array[Int] = Array(10)
//    ks.foreach(cluster => {
//      val model:KMeansModel = KMeans.train(parsedTrainingData, cluster,numIteration)
//      val ssd = model.computeCost(parsedTrainingData)
//      println("sum of squared distances of points to their nearest center when k=" + cluster + " -> "+ ssd)
//    })
  }


  /**
    * 判断该行是否含有某字符串
    *
    * @param line
    * @return
    */
   def isColumnNameLine(line:String):Boolean = {
    if (line != null && line.contains("**")) true
    else false
  }

   def getBrand(brand:String): String ={
    var b =""
    if(brand.equals("苹果"))
      b = "0"
    else
      b ="1"
    b
  }

}
