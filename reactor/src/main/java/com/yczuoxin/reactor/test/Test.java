package com.yczuoxin.reactor.test;

import java.util.Random;
import java.util.StringJoiner;

public class Test {
    public static void main(String[] args) {
//        String str = "";
//        String a = String.valueOf(new Random().nextInt(10));
//        String b = String.valueOf(new Random().nextInt(10));
//        String c = String.valueOf(new Random().nextInt(10));
//        String d = String.valueOf(new Random().nextInt(10));
//        String e = String.valueOf(new Random().nextInt(10));
//        String f = String.valueOf(new Random().nextInt(10));
//        String g = String.valueOf(new Random().nextInt(10));
//
//        long start00 = System.currentTimeMillis();
//        for (int i = 0; i < 2000000; i++)
//        {
//            StringJoiner stringBuilder = new StringJoiner(",");
//            stringBuilder.add(a);
//            stringBuilder.add(b);
//            stringBuilder.add(c);
//            stringBuilder.add(d);
//            stringBuilder.add(e);
//            stringBuilder.add(f);
//            stringBuilder.add(g);
//
////            str = stringBuilder.append(b).append(c).append(d).append(e).append(f).append(g).toString();
//        }
////        System.out.println(System.currentTimeMillis() - start00);
//
//        long start0 = System.currentTimeMillis();
//
//        for (int i = 0; i < 200000; i++)
//        {
//            StringBuilder stringBuilder = new StringBuilder(str);
//            str=stringBuilder.append(a).toString();
////            str = stringBuilder.append(b).append(c).append(d).append(e).append(f).append(g).toString();
//        }
//        System.out.println(System.currentTimeMillis() - start0);
//
//        long start1 = System.currentTimeMillis();
//        for (int i = 0; i < 200000; i++)
//        {
//            str = a + b + c + d + e + f + g;
//            str =str + a;
//        }
//        System.out.println(System.currentTimeMillis() - start1);

        long start = System.currentTimeMillis();
        String str = "";
        String a = "1";
        String b = "v";
        for (int i = 0; i < 2; i++) {
            str = String.format("{0}{1}", a, b);
        }
        System.out.println(str + (System.currentTimeMillis() - start));


    }
}
