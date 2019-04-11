package com.yczuoxin.concurrent.demo.basis.remake;

/**
 * 可能在输出 4 之前会输出 0，这是由于第 3 步和第 4 步可能发生指令重排
 */
public class Remake {

    public static class ReadThread extends Thread {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                if (ready) { //1
                    System.out.println(num + num); //2
                }
                System.out.println("read thread ...");
            }
        }
    }

    public static class WriteThread extends Thread {
        @Override
        public void run() {
            num = 2; //3
            ready = true; //4
            System.out.println("writeThread set over");
        }
    }

    private static int num = 0;
    private static boolean ready = false;

    public static void main(String[] args) throws InterruptedException {
        ReadThread rt = new ReadThread();
        rt.start();

        WriteThread wt = new WriteThread();
        wt.start();

        Thread.sleep(10);
        rt.interrupt();
        System.out.println("main exit");
    }

}
