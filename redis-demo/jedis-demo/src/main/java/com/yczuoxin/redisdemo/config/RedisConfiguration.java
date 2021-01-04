package com.yczuoxin.redisdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfiguration {

    @Bean
    @Autowired
    public JedisPool jedisPool(JedisPoolConfig jedisPoolConfig,
                               @Value("${spring.redis.host}") String host,
                               @Value("${spring.redis.port}") int port,
                               @Value("${spring.redis.password}") String password,
                               @Value("${spring.redis.timeout}") int timeout) {
        return new JedisPool(jedisPoolConfig, host, port, timeout, password);
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig(@Value("${spring.redis.jedis.pool.max-idle}") int minIdle,
                                           @Value("${spring.redis.jedis.pool.max-idle}") int maxIdle,
                                           @Value("${spring.redis.jedis.pool.max-wait}") int maxWaitMillis) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMinIdle(minIdle);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(maxWaitMillis);
        return config;
    }

}