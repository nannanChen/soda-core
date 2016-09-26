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




}
