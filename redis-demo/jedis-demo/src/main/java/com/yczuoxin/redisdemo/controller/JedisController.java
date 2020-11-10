package com.yczuoxin.redisdemo.controller;

import com.yczuoxin.redisdemo.utils.JedisUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

@RestController
public class JedisController {

    public JedisController(JedisUtil jedisUtil){
        this.jedisUtil = jedisUtil;
    }
    private final JedisUtil jedisUtil;


    @GetMapping("/redis/{key}")
    public String testJedis(@PathVariable("key") String key) {
        Jedis jedis = jedisUtil.getJedis();
        if (Boolean.TRUE.equals(jedis.exists(key))) {
            return jedis.get(key);
        }
        return "Redis 不存在该数据";
    }

}
