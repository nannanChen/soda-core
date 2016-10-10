package com.soda.redis

import java.util

import com.soda.common.{ConstantsUtil, JedisClusterUtil}
import com.soda.service.SubwayImpl
import org.apache.log4j.Logger
import org.apache.spark.rdd.RDD

/**
  * Created by nan on 2016/9/26 16:42
  *
  */

object RedisService extends Serializable{

  val log_ : Logger = Logger.getLogger(this.getClass)
  val map = new util.HashMap[String,String]()
  val pool = JedisClusterUtil.getJedisClusterPool()
  val subwayImpl = new SubwayImpl

  /**
    * 将站点及对应的经纬度设置到redis
    * @param station 站点
    * @param lnglat 经纬度
    */
  def addStationToLnglat(station:String,lnglat:String): Unit ={
    pool.hset(ConstantsUtil.StationToLnglat,station,lnglat)
  }

  def addTag(iemi:String,tag:String): Unit ={
    pool.hset(ConstantsUtil.IEMITag,iemi,tag)
  }
  def getTag(iemi:String): String ={
    pool.hget(ConstantsUtil.IEMITag,iemi)
  }
  /**
    * 根据站点从redis取得经纬度
    * @param station
    * @return map
    */
  def getlongitudeandlatitudeFromRedis(station:String):util.HashMap[String, String]={
    map.put("lng",pool.hget(ConstantsUtil.StationToLnglat,station).split(",")(0))
    map.put("lat",pool.hget(ConstantsUtil.StationToLnglat,station).split(",")(1))
    return map
  }


  /**
    * 判断是redia是否存在该地点，如果存在，直接访问redis则不处理，如果不存在，则存入redis
    * @param data 数据集
    */
  def parseStat(data: RDD[String])={
    println("开始向redis存入地点及经纬度" )
    val statRdd = data.map(_.split(",")).filter(_.length==7).map(_(3)).distinct
    println("去重之后的地点RDD长度:"+statRdd.collect().length)
    for(z<-statRdd if !pool.hexists(ConstantsUtil.StationToLnglat,"上海"+z)){
      println("该地点需要访问百度地图")
      addStationToLnglat("上海"+z,subwayImpl.getlongitudeandlatitudeFromBaiDu("上海"+z))
    }
    println("存入redis过程结束")
    }
}
