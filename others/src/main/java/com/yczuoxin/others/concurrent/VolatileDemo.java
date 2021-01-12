package com.yczuoxin.others.concurrent;

public class VolatileDemo {
    private static boolean flag = false;

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            int a = 0;
            for (;;){
                a++;
                if (flag) {
                    break;
                }

            }
        });

        thread.start();
        Thread.sleep(10);
        flag = true;
        System.out.println(flag);
    }
}
