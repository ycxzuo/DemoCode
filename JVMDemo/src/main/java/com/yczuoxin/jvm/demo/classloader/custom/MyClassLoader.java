package com.yczuoxin.jvm.demo.classloader.custom;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

public class MyClassLoader extends ClassLoader {

    private String path;

    public MyClassLoader(String path) {
        this.path = path;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        try(FileInputStream fis = new FileInputStream(new File(path, name.substring(name.lastIndexOf(',') + 1) + ".class"));
            ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            int offset = 0;
            while ((offset = fis.read()) != -1) {
                bos.write(offset);
            }
            byte[] bytes = bos.toByteArray();
            return defineClass(name, bytes, 0, bytes.length);
        } catch (Exception e) {
            throw new ClassNotFoundException();
        }
    }
}
