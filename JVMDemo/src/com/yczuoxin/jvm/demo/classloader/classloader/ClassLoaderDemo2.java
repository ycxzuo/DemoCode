package com.yczuoxin.jvm.demo.classloader.classloader;

public class ClassLoaderDemo2 {
    public static void main(String[] args) {
        // application class loader
        System.out.println(ClassLoader.getSystemClassLoader()); // jdk.internal.loader.ClassLoaders$AppClassLoader@2437c6dc
        // extensions class loader
        System.out.println(ClassLoader.getSystemClassLoader().getParent()); // jdk.internal.loader.ClassLoaders$PlatformClassLoader@880ec60
        // bootstrap class loader
        System.out.println(ClassLoader.getSystemClassLoader().getParent().getParent()); // null
    }
}
