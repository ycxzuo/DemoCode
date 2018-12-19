package com.yczuoxin.jvm.demo.classloader.bean;

public class SuperClass {
    static {
        System.out.println("SuperClass 被初始化");
    }
    public static int value = 1;
}