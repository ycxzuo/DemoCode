package com.yczuoxin.jvm.demo.classloader.classloader;

import java.io.IOException;
import java.io.InputStream;

/**
 *  不同的类加载器加载出来的同一个类，依然是独立的两个类，做对象所属类型检查时结果自然为 false
 */
public class ClassLoaderDemo1 {

    public static void main(String[] args) {
        ClassLoader myLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                try {
                    String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
                    InputStream is = getClass().getResourceAsStream(fileName);
                    if (is == null) {
                        return super.loadClass(name);
                    }
                    byte[] b = new byte[is.available()];
                    is.read(b);
                    return defineClass(name, b, 0, b.length);
                } catch (IOException e) {
                    throw new ClassNotFoundException(name);
                }
            }
        };
        Object obj = null;
        try {
            obj = myLoader.loadClass("com.yczuoxin.jvm.demo.classloader.classloader.ClassLoaderDemo1").newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(obj.getClass()); // class com.yczuoxin.jvm.demo.classloader.classloader.ClassLoaderDemo1
        System.out.println(obj instanceof ClassLoaderDemo1); // false
    }
}
