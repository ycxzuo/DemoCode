package com.yczuoxin.lettucedemo.controller;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class LettuceController {

    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> value;

    public LettuceController(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/redis/{key}")
    public String getFromLettuce(@PathVariable("key") String key) {
        if (redisTemplate.hasKey(key)) {
            return value.get(key);
        }
        redisTemplate.opsForValue().set(key, "123");
        return "Redis 没有存储这个 key";
    }

}
