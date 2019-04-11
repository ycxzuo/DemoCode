package com.yczuoxin.concurrent.other.reflect;

import java.lang.reflect.Method;

public class ReflectDemo {
    public static void main(String[] args) throws Exception {
        Class<?> clazz = Class.forName("com.yczuoxin.concurrent.other.reflect.TestDemo");
        Method[] methods = clazz.getMethods();
        for (Method me : methods) {
            Class<?>[] parameterTypes = me.getParameterTypes();
            System.out.println(me.getName());
            for (int i = 0; i < parameterTypes.length; i++) {
                System.out.println(parameterTypes[i].getName());
            }
            System.out.println("---------------------------");
        }
    }
}
