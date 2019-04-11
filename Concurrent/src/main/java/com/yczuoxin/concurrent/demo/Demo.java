package com.yczuoxin.concurrent.demo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Demo {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        int[] arr = new int[]{1,6,10,18,4,9,8,12,7,11};
        Class<?> clazz = Class.forName("java.util.DualPivotQuicksort");
        Method sort =
                clazz.getDeclaredMethod("sort", int[].class, int.class, int.class, boolean.class);
        sort.setAccessible(true);
        sort.invoke(arr, arr, 0,9, false);
        System.out.println(arr[2]);
    }
}
