package com.soda

import com.soda.common.ObjectId
import org.apache.hadoop.hbase.util.Bytes

/**
  * Created by Administrator on 2016/9/23.
  */
object Test {

  def main(args: Array[String]) {
//    Range(10,-1,-1).foreach(e => println(new StringBuffer(new ObjectId().toString).reverse().toString))
//    val rk=Bytes.toBytes("20150812163500.D010")
    val rowKey=new ObjectId().toString;
//    println("rowKey:"+rowKey)

    val rk=Bytes.toBytes("3ccbe609dabccffa51df4e75201603013")
    println(rk.length)


    for( a <- 0 until 9){
      println( "Value of a: " + a );
    }
  }

}
