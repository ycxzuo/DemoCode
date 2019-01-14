package com.yczuoxin.jvm.demo.classloader.init;

public class InitDemo4 {
    static {
        i = 0;
        // System.out.println(i); // illegal forward reference(非法向前引用)
    }
    static int i = 1;
}