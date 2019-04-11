package com.yczuoxin.concurrent.demo.basis.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectFieldOffsetDemo {

    public static void main(String[] args) {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            Unsafe unsafe = (Unsafe) f.get(null);
            long value = unsafe.objectFieldOffset(AtomicLong.class.getDeclaredField("value"));
            System.out.println(value);
        } catch (Exception e) { }
    }
}
