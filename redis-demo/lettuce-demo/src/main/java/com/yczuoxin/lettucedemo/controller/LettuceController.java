package com.yczuoxin.lettucedemo.controller;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LettuceController {

    private RedisTemplate<String, Object> redisTemplate;

    public LettuceController(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/redis/{key}")
    public String getFromLettuce(@PathVariable("key") String key) {
        if (redisTemplate.hasKey(key)) {
            return redisTemplate.opsForValue().get(key).toString();
        }
        redisTemplate.opsForValue().set(key, "123");
        return "Redis 没有存储这个 key";
    }

}
