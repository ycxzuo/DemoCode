package com.yczuoxin.others.redis;

import redis.clients.jedis.Jedis;


public class RedisSubClient {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("47.106.80.100", 6379);
        jedis.subscribe(new RedisSubConfig(), "test");
    }

}
