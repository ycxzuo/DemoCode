package com.yczuoxin.jvm.demo.classloader.init;

public class InitDemo5 {
    static class Parent {
        public static int A = 1;
        static {
            A = 2;
        }
    }

    static class Sub extends Parent {
        public static int B= A;
    }

    public static void main(String[] args) {
        System.out.println(Sub.B); // 2
    }
}
