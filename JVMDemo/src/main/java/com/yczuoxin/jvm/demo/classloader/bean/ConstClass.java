package com.yczuoxin.jvm.demo.classloader.bean;

public class ConstClass {
    static {
        System.out.println("ConstClass 被初始化");
    }
    public static final String HELLO_WORLD = "Hello World";
}

