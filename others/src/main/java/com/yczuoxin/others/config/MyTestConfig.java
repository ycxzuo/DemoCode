package com.yczuoxin.others.config;

import com.yczuoxin.others.bean.Car;
import com.yczuoxin.others.bean.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Component  // 不同的car
@Configuration  // 同一个car
public class MyTestConfig {

    @Bean
    public Driver driverTest(){
        Driver driver = new Driver();
        driver.setId(1);
        driver.setName("driver");
        driver.setCar(car());
        return driver;
    }

    @Bean
    public Car car(){
        Car car = new Car();
        car.setId(1);
        car.setName("car");
        return car;
    }
}