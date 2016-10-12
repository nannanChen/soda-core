package com.soda.mllib.Linear

import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.{LabeledPoint, LinearRegressionWithSGD}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by kcao on 2016/10/10.
  * 5,1 1
  * 7,2 1
  * 9,3 2
  * 11,4 1
  * 19,5 3
  * 18,6 2
  */
object LinearRegression {

  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("LinearRegression")

    val sc = new SparkContext(conf)
    val data = sc.textFile("/test/data/lpsa2.data")
    data.foreach(println(_))
    val parsedData = data.map { line =>
        val parts = line.split(',')
        LabeledPoint(parts(0).toDouble, Vectors.dense(parts(1).split(' ').map(_.toDouble)))
      }.cache()

    val model = LinearRegressionWithSGD.train(parsedData, 10,0.1)

    val result = model.predict(Vectors.dense(2))
    println(result)

  }

}
