package com.yczuoxin.others.spring.proxy.my;

import java.lang.reflect.Method;

public interface InvocationHandler {
    void invoke(Object o, Method method);
}
