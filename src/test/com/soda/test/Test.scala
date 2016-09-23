package com.soda.test

import com.soda.common.ObjectId

/**
  * Created by Administrator on 2016/9/23.
  */
object Test {

  def main(args: Array[String]) {
    Range(10,-1,-1).foreach(e => println(new ObjectId().toString))


  }

}
