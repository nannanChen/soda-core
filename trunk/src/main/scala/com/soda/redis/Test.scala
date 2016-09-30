package com.soda.redis

import com.soda.common.{HashAlgorithmsUtil, ConstantsUtil, JedisClusterUtil}
import com.soda.kmeans.KmeansInter
import org.apache.spark.rdd.RDD

/**
  * Created by nan on 2016/9/26 16:42
  *
  */

object Test {
  def main(args: Array[String]) {
//    val pool=JedisClusterUtil.getJedisClusterPool;
//    val aa=pool.hset(ConstantsUtil.StationToLnglat,"3号线曹杨路","121.42006909239932|31.246581806372546")
//    println(aa)
//    val ll=pool.hget(ConstantsUtil.StationToLnglat,"3号线曹杨路")
//    val bb =RedisService.getlongitudeandlatitudeFromRedis("9号线合川路")
//    println(bb)
//    isteadStr("f0d507b3d91c443958e061dc9441bbf3,36,女,低,苹果,A1524,196,0,0,0,102,是,否,21,374.0,0.0,0.0,0.0,53.0,11.0,0.0,0.0,0.0,0.0,0.0,0.0,2.0,0.0,16.0")

    println(isteadStr("\"dfdbe3dbce0e0b0fe9f0dd803084619a\",\"17\",\"男\",\"高\",\"华为\",\"EM770W\",\"88\",\"0\",\"0\",\"0\",\"0\",\"否\",\"否\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\""))
  }
private def isteadStr(line:String):Unit = {
  val splited= line.replaceAll("\"","").split(",")
  val x = line.replaceAll("\"","").replace(splited(0),1.toString).replaceFirst(splited(4),"5").replace(splited(5),"0").replace("男","0").replace("女","1")
    .replace("不详","2").replace("低","0").replace("中","0").replace("高","0").replace(splited(11),"0").replace(splited(12),"0")
  println("x:"+x)
  val y1 = x.split(",").toList.drop(6).mkString(",")
  val y = x.split(",").drop(6).mkString(",")
  println("y:"+y)
  println("y1:"+y1)
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
