package com.yczuoxin.others.spring.proxy.my;

import java.lang.reflect.Method;

public class TimeHandler implements InvocationHandler{

    private Object target;

    public TimeHandler(Object target) {
        this.target = target;
    }

    @Override
    public void invoke(Object o, Method method) {
        long start = System.currentTimeMillis();
        System.out.println("start time: " + start);
        System.out.println(o.getClass().getName());
        try {
            method.invoke(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("cast time: " + (end - start));
    }
}
