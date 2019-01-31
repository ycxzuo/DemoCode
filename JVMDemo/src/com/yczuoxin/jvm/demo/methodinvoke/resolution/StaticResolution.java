package com.yczuoxin.jvm.demo.methodinvoke.resolution;

/**
 * javap -verbose StaticResolution
 */
public class StaticResolution {
    public static void sayHello () {
        System.out.println("hello world");
    }

    public static void main(String[] args) {
        StaticResolution.sayHello();
    }
}
