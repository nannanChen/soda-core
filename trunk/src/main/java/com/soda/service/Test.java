package com.soda.service;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/23.
 */
public class Test {
    public static void main(String[] args) {
        SubwayImpl test = new SubwayImpl();
        Map map =test.getlongitudeandlatitude("11号线枫桥路");
        System.out.println(map.get("lng"));
    }
}
