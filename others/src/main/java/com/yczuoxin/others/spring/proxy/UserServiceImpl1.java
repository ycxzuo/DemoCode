package com.yczuoxin.others.spring.proxy;

public class UserServiceImpl1 implements UserService1 {
    @Override
    public void test() {
        System.out.println("调用 test 方法");
    }
}
