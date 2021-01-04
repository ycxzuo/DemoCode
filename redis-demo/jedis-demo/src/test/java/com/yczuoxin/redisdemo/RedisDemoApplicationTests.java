package com.yczuoxin.redisdemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.JedisPool;

@SpringBootTest
class RedisDemoApplicationTests {

	@Autowired
	private JedisPool jedisPool;

	@Test
	void contextLoads() {
	}



}
