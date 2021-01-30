package com.yczuoxin.others.spring.proxy.my;

public class MyProxyTest {
    public static void main(String[] args) throws Exception {
        Tank tank = new Tank();
        InvocationHandler handler = new TimeHandler(tank);
        Moveable m = (Moveable) Proxy.newProxyInstance(Moveable.class, handler);
        m.move();
    }
}
