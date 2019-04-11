package com.yczuoxin.jvm.demo.classloader.custom;

import java.lang.reflect.Method;

public class MyClassLoaderTest {
    public static void main(String[] args) throws ClassNotFoundException {
        MyClassLoader classLoader = new MyClassLoader("D:\\mygit\\DemoCode\\Concurrent\\out\\production\\DemoCode\\com\\yczuoxin\\jvm\\demo\\classloader\\bean");
        Class<?> clazz = classLoader.loadClass("Person");
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            System.out.println(method.getName());
        }
    }
}
