package com.yczuoxin.others.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

public class RedisPipelineClient {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("47.106.80.100", 6379);
        Pipeline pipelined = jedis.pipelined();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            pipelined.set("pipeline: test_" + i, i + "");
        }
        pipelined.sync();
        System.out.println(System.currentTimeMillis() - start);
    }
}
