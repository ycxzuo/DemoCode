package com.yczuoxin.jvm.demo.classloader.bean;

public class SubClass extends SuperClass {
    static {
        System.out.println("SubClass 初始化");
    }
}
