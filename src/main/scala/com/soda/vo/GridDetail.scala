package com.soda.vo

/**
  * 网格数据
  * Created by kcao on 2016/9/30.
  */
case class GridDetail(date:String,imei:String,hour:Int,fromIndex:Int,toIndex:Int) {

  override def toString: String = {
    //这一天  这个点  这个人 从 这个方框  到  这个方框
    date+","+hour+","+imei+","+fromIndex+","+toIndex
  }

}
