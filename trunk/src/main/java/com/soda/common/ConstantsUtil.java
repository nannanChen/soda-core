package com.soda.common;

import java.io.Serializable;

/**
 * Created by kcao on 2016/3/11.
 */
public final class ConstantsUtil implements Serializable{

    //ZK地址
    public static final String ZOOKEEPER_ADDRESS="zk1:2181,zk2:2181,zk3:2181";
    //hdfs地址
    public static final String HDFS_ADDRESS="hdfs://masters";

    public static final String POINT_MAP_INFO="point_map_info";
    public static final String POINT_DETAIL ="point_detail_grid";

    public static final String REDIS_HOST1="redisAddress1";
    public static final String REDIS_HOST2="redisAddress2";
    public static final String REDIS_HOST3="redisAddress3";
    public static final int REDIS_PORT=12002;

    //redis表站点转经纬度
    public static final String StationToLnglat = "stationToLnglat";

    //dbcp配置
    public static final String DRIVERCLASSNAME="com.mysql.jdbc.Driver";
    public static final String DB_URL="jdbc:mysql://192.168.20.93:3306/soda?useUnicode=true&characterEncoding=utf-8";
    public static final String DB_USERNAME="root";
    public static final String DB_PASSWORD="admin123!!";
    public static final int maxActive=30;
    public static final int maxIdle=1;
    public static final int maxWait=1000;
    public static final boolean removeAbandoned=true;
    public static final int removeAbandonedTimeout=180;


    private ConstantsUtil() {} // prevent instantiation

}
