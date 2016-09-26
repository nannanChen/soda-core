package com.soda.job

import com.soda.common.{DataSourceUtil, MySqlUtil, ConstantsUtil}
import org.apache.spark.{SparkContext, SparkConf}

/**
  * Created by kcao on 2016/9/26.
  spark-submit \
   --master local \
   --deploy-mode client \
   --jars /home/hadoop/sodatest/lib/commons-dbcp-1.4.jar,/home/hadoop/sodatest/lib/mysql-connector-java-5.1.38.jar  \
   --class com.soda.job.ImportMysqlJob /home/hadoop/sodatest/soda-1.0-SNAPSHOT.jar nanJingDongRs/part-00005 point_info_5 >> soda.log
  */
object ImportMysqlJob {

  def main(args: Array[String]): Unit = {

    if(args.length!=2){
      println("args is error")
    }

    val conf = new SparkConf().setMaster("local").setAppName("ImportMysqlJob") //创建环境变量
    val sc = new SparkContext(conf)  //创建环境变量实例
    val data = sc.textFile(ConstantsUtil.HDFS_ADDRESS+"/soda/mysql/"+args(0))  //读取数据

    val sql ="insert into `soda`.`"+args(1)+"` (`row_key`, `date`, `target_precursor`, `target_longitude`, `target_latitude`, `target_next`, `name` , `distance`, `src_longitude`, `src_latitude`, `src_next`) values (?,?,?,?,?,?,?,?,?,?,?)";
    data.map(_.split(",")).foreach(arr=>{
      val mySqlUtil=new MySqlUtil(DataSourceUtil.dataSource);
      println("length:"+arr.length)
      if(arr.length==11){
        val params=new java.util.ArrayList[Object]()
        params.add(arr(0).replace("(",""))
        params.add(arr(1))
        params.add(arr(2))
        params.add(arr(3))
        params.add(arr(4)+"")
        params.add(arr(5)+"")
        params.add(arr(6))
        params.add(arr(7))
        params.add(arr(8)+"")
        params.add(arr(9)+"")
        params.add(arr(10).replace(")",""))
        mySqlUtil.updateByPreparedStatement(sql, params)
      }
    })
    System.exit(0)
  }

}
