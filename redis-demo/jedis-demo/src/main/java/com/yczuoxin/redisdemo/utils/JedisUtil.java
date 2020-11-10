package com.yczuoxin.redisdemo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Configuration
public class JedisUtil {

    @Autowired
    private JedisPool jedisPool;

    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    public void close(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

}
