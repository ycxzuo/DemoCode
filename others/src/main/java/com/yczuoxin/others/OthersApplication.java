package com.yczuoxin.others;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
public class OthersApplication {

	public static void main(String[] args) {
		SpringApplication.run(OthersApplication.class, args);
	}

}
