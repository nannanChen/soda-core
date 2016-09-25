package com.soda.common;

import java.io.Serializable;

/**
 * Created by kcao on 2016/9/25.
 */
public class DistanceUtil implements Serializable{

    private static final  double EARTH_RADIUS = 6378137;//赤道半径(单位m)

    /**
     * 转化为弧度(rad)
     * */
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    /**
     * @param lon1 第一点的经度
     * @param lat1 第一点的纬度
     * @param lon2 第二点的经度
     * @param lat2 第二点的纬度
     * @return 返回的距离，单位m
     * */
    public static double GetDistance(double lon1,double lat1,double lon2, double lat2)
    {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2)+Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        //s = Math.round(s * 10000) / 10000;
        return s;
    }

    public static void main(String[] args) {
        double lon1=121.5010320000, lat1=31.1671800000;
        double lon2=121.4446320000, lat2=31.1993870000;   //徐家汇
        double rs=DistanceUtil.GetDistance(lon1,lat1,lon2,lat2);
        System.out.println(rs);
    }

}
