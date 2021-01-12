package com.yczuoxin.others.redis;

import redis.clients.jedis.Jedis;

public class RedisPubClient {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("47.106.80.100", 6379);
        System.out.println("publish ing");
        jedis.publish("test", "hello world");
        System.out.println("publish over");
    }
}
