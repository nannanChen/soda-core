package com.soda.job

import org.apache.hadoop.hbase.protobuf.ProtobufUtil;
import org.apache.hadoop.hbase.util.Base64;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.spark.{SparkContext, SparkConf}

/**
  * 数据整合  提供前端展现使用
  *
  * spark-submit \
   --master spark://192.168.20.90:7077 \
   --deploy-mode client \
   --jars /home/hadoop/online/lib/htrace-core-3.0.4.jar,/home/hadoop/online/lib/hbase-common-0.99.2.jar,/home/hadoop/online/lib/hbase-protocol-0.99.2.jar,/home/hadoop/online/lib/hbase-client-0.99.2.jar,/home/hadoop/online/lib/commons-pool2-2.3.jar,/home/hadoop/online/lib/guava-12.0.1.jar,/home/hadoop/online/lib/hbase-server-0.98.8-hadoop2.jar  \
   --class com.soda.job.SodaEtlJob /home/hadoop/sodatest/soda-1.0-SNAPSHOT.jar >> soda.log
  *
  * Created by kcao on 2016/9/23.
  */
object SodaEtlJob {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local").setAppName("SodaEtlJob") //创建环境变量
    val sc = new SparkContext(conf) //创建环境变量实例

    val hbaseConf = HBaseConfiguration.create()
    hbaseConf.set(TableInputFormat.INPUT_TABLE, "point_detail")

    val scan = new org.apache.hadoop.hbase.client.Scan();
    println("scan:"+scan+" "+scan.getClass)
    scan.setCacheBlocks(false)
    scan.addFamily(Bytes.toBytes("base"));
    scan.addColumn(Bytes.toBytes("base"), Bytes.toBytes("longitude"));
    val proto = ProtobufUtil.toScan(scan);
    val ScanToString = Base64.encodeBytes(proto.toByteArray());
    conf.set(TableInputFormat.SCAN, ScanToString);

    val hBaseRDD = sc.newAPIHadoopRDD(hbaseConf, classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])

    val num=hBaseRDD.count()
    println("num="+num)
    System.exit(0)
  }

}
