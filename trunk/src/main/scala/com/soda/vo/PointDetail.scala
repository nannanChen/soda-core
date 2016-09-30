package com.soda.vo

import com.soda.common.IdentityTypeEnum

/**
  * Created by kcao on 2016/9/23.
  */

abstract class Element extends Serializable

case class Basic(var precursor:String,longitude:Double,latitude:Double,var next:String,date:String,hour:Int,var index:Int) extends  Element //点基本信息  对应hbase的PointDetail表的Basic列簇

case class User(valType:IdentityTypeEnum,value:String) extends  Element //用户信息  对应hbase的PointDetail表的User列簇

case class PointDetail(rowkey:String,basic:Basic,user:User) extends Element //点详情  对应hbase一条记录
