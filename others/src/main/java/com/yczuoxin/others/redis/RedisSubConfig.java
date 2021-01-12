package com.yczuoxin.others.redis;

import redis.clients.jedis.JedisPubSub;

public class RedisSubConfig extends JedisPubSub {

    @Override
    public void onMessage(String channel, String message) {
        System.out.println("收到消息：" + message + ", channel name:" + channel);

    }

}
