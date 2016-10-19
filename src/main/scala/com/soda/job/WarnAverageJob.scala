package com.soda.job

import java.sql.{PreparedStatement, Connection}
import java.util

import com.soda.common.{XYData, DataSourceUtil, MySqlUtil}

/**
  * 预警平均值计算任务
  * Created by kcao on 2016/10/19.
  */
object WarnAverageJob {

  def main(args: Array[String]) {
    val mysql=new MySqlUtil(DataSourceUtil.dataSource)
    val params=new util.ArrayList[AnyRef]()
    val indexs=mysql.findMoreResult("SELECT DISTINCT from_index FROM grid_from_to_num1",params)
    for(i <- 0 until indexs.size()){
      val from_index=indexs.get(i).get("from_index")
      println("from_index:"+from_index)
      params.clear()
      params.add(from_index)
      val dataList=mysql.findMoreResult("SELECT from_index ,SUM(COUNT)/31 as avg,hour FROM `grid_from_to_num1` WHERE (from_index = to_index  OR to_index = -1)AND from_index=? GROUP BY HOUR",params)
      toMySqlWarnAverage(dataList)
    }
  }

  def toMySqlWarnAverage(dataList: util.List[util.Map[String, AnyRef]]) = {
    var conn: Connection = null
    var ps: PreparedStatement = null
    val sqlMaster ="insert into `soda`.`warn_average` (`grid_index`, `avg`,`hour`) values (?,?,?)"
    try {
      Class.forName("com.mysql.jdbc.Driver")
      conn=DataSourceUtil.dataSource.getConnection
      for(i <- 0 until dataList.size()) {
        val warnAverage = dataList.get(i)
        ps = conn.prepareStatement(sqlMaster)
        ps.setInt(1, Integer.parseInt(warnAverage.get("from_index").toString))
        val avg=warnAverage.get("avg").toString
        println(avg)
        if(avg.indexOf('.')==(-1)){
          ps.setInt(2, Integer.parseInt(avg))
        }else{
          ps.setInt(2, Integer.parseInt(avg.split('.')(0)))
        }
        ps.setInt(3, Integer.parseInt(warnAverage.get("hour").toString))
        ps.executeUpdate()
      }
    }finally {
      if (ps != null) {
        ps.close()
      }
      if (conn != null) {
        conn.close()
      }
    }
  }

}
