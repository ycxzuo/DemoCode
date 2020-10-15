package com.yczuoxin.others.string;

public class Test2 {
    public static void main(String[] args) {
        int count = 2000;
        testString(count);
        testString2(count);
    }

    private static void testString(int count) {
        String str = "";
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            str += "a" + "b" + "c" + "d" + "e" + "f" + "g";
        }
        System.out.println("testString method takes " + (System.currentTimeMillis() - start) + " millis");
    }

    private static void testString2(int count) {
        String str = "";
        String a = "a";
        String b = "b";
        String c = "c";
        String d = "d";
        String e = "e";
        String f = "f";
        String g = "g";
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            str += a + b + c + d + e + f + g;
        }
        System.out.println("testString method takes " + (System.currentTimeMillis() - start) + " millis");
    }
}
