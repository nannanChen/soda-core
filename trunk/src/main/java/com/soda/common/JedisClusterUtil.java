package com.soda.common;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import java.io.Serializable;
import java.util.HashSet;

/**
 * Created by kcao on 2016/5/11.
 */
public class JedisClusterUtil implements Serializable {

    private static JedisCluster pool;

    static{
        HashSet jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort(ConstantsUtil.REDIS_HOST1, ConstantsUtil.REDIS_PORT));
        jedisClusterNodes.add(new HostAndPort(ConstantsUtil.REDIS_HOST3, ConstantsUtil.REDIS_PORT));
        jedisClusterNodes.add(new HostAndPort(ConstantsUtil.REDIS_HOST2, ConstantsUtil.REDIS_PORT));
        pool = new JedisCluster(jedisClusterNodes);
    }

    public static JedisCluster getJedisClusterPool(){
        return pool;
    }


    private static char[] hexChar={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
    public static void main(String[] args) {
        JedisCluster pool=JedisClusterUtil.getJedisClusterPool();
        java.util.Random random=new java.util.Random();
        StringBuffer hex=new StringBuffer();
        for (int i=0;i<8;i++) {
            int result=random.nextInt(16);
            hex.append(hexChar[result]);
        }
        System.out.println(hex.toString());
    }

}
