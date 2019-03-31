package com.yczuoxin.concurrent.demo;

public class Demo3 {
    public static void main(String[] args) {
        int x, y;
        x = 3;
        y = 4;
        System.out.println(x != (x = y));
    }
}
