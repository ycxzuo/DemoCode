package com.yczuoxin.others.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class InvocationHandlerImpl implements InvocationHandler {

    private Object subject;

    public InvocationHandlerImpl(Object subject) {
        this.subject = subject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("进入代理方法。。。。。。。。。");

        System.out.println("调用方法前.....");
        Object invoke = method.invoke(subject, args);
        System.out.println("调用方法后");
        return invoke;
    }
}
