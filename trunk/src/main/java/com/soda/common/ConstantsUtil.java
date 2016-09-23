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

    public static final String POINT_DETAIL="point_detail";


    private ConstantsUtil() {} // prevent instantiation

}
