package com.soda.kmeans

import org.apache.spark.mllib.linalg.Vector
import com.soda.common.HashAlgorithmsUtil
import org.apache.spark.mllib.clustering.KMeansModel
import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkContext, SparkConf}

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
   --jars /home/hadoop/online/lib/spark-mllib_2.10-1.5.2.jar \
   --class com.soda.kmeans.KmeansInter /home/hadoop/nannan/soda-1.0-SNAPSHOT.jar \
   >>soda.log
  */

/**
   spark-submit \
   --master spark://192.168.20.90:7077 \
   --deploy-mode client \
   --jars /home/hadoop/online/lib/spark-mllib_2.10-1.5.2.jar \
   --class com.soda.kmeans.KmeansInter /home/hadoop/nannan/soda-1.0-SNAPSHOT.jar \
   >>soda.log
  */
object KmeansInter {
  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("KmeasInter")
    val sc = new SparkContext(conf)
//    sc.setLocalProperty("file.encoding","GB2312")
    val rawTrainingData = sc.textFile("hdfs://192.168.20.90:9000/soda/test/data.txt")

    val parsedTrainingData = rawTrainingData.filter(!isColumnNameLine(_)).filter((_.replace("\"","").split(",").length==29)).map(line=>{
      val splited= line.replaceAll("\"","").split(",")
      val iemi = HashAlgorithmsUtil.BKDRHash(splited(0))
      val brand = getBrand(splited(4))
      val x = line.replaceAll("\"","").replace(splited(0),iemi.toString).replaceFirst(splited(4),brand).replace(splited(5),"0").replace("男","0").replace("女","1")
        .replace("不详","2").replace("低","0").replace("中","0").replace("高","0").replace(splited(11),"0").replace(splited(12),"0")

        Vectors.dense(x.split(",").map(_.trim).filter(!"".equals(_)).map(_.toDouble)) //org.apache.spark.mllib.linalg.Vector
    })
    val numClusters = Array(5,10,15,20,21,22,23,24,25,26,27,28,29,30,35,40,45,50)
    val numIteration = 30
    val runTimes = 3

    numClusters.foreach(f=>{
      kmeans(parsedTrainingData,f,numIteration)
    })
    /**
      * 根据以上结果输入数据
      */
    println("Spark MLlib K-means clustering test finished.")
  }
  def kmeans(parsedTrainingData: RDD[Vector],numClusters:Int,numIteration:Int): Unit ={
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
      val predictedClusterIndex = cluster.predict(testDataLine)
//      println("-------这条数据:"+testDataLine.toString + "属于:"+predictedClusterIndex)
    })
    val ks:Array[Int] = Array(5,10,15,20,21,22,23,24,25,26,27,28,29,30,35,40,45,50)
    ks.foreach(cluster => {
      val model:KMeansModel = KMeans.train(parsedTrainingData, cluster,numIteration)
      val ssd = model.computeCost(parsedTrainingData)
      println("sum of squared distances of points to their nearest center when k=" + cluster + " -> "+ ssd)
    })
  }


  /**
    * 判断该行是否含有某字符串
    *
    * @param line
    * @return
    */
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
