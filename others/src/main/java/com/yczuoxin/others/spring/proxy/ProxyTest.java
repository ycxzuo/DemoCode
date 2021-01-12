package com.yczuoxin.others.spring.proxy;

import java.lang.reflect.Proxy;

public class ProxyTest {
    public static void main(String[] args) {
        UserService1 userService1 = new UserServiceImpl1();
        InvocationHandlerImpl invocationHandler = new InvocationHandlerImpl(userService1);
        UserService1 service = (UserService1) Proxy.newProxyInstance(userService1.getClass().getClassLoader(), userService1.getClass().getInterfaces(), invocationHandler);
        service.test();
    }
}
