package com.yczuoxin.concurrent.demo.queue.blockingqueue.synchronousqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class SynchronousQueueDemo {
    static BlockingQueue<Integer> queue = new SynchronousQueue<>();

    public static void main(String[] args) throws InterruptedException {
        Thread putThreadOne = new Thread(SynchronousQueueDemo::putElement, "putThread");
        Thread putThreadTwo = new Thread(SynchronousQueueDemo::putElement2, "putThread2");
        Thread putThreadThree = new Thread(SynchronousQueueDemo::putElement3, "putThread3");
        Thread takeThreadOne = new Thread(SynchronousQueueDemo::takeElement, "takeThread");
        Thread takeThreadTwo = new Thread(SynchronousQueueDemo::takeElement, "takeThread");
        Thread takeThreadThree = new Thread(SynchronousQueueDemo::pollElement, "takeThread2");

        takeThreadOne.start();
        TimeUnit.SECONDS.sleep(1);
        putThreadOne.start();

        System.out.println("---------------------------------------------------------");
        putThreadOne.start();
        putThreadTwo.start();
        TimeUnit.SECONDS.sleep(2);
        putThreadThree.start();
        TimeUnit.SECONDS.sleep(5);
        takeThreadTwo.start();
        TimeUnit.SECONDS.sleep(2);
        takeThreadThree.start();
        putThreadThree.start();

        putThreadThree.join();
    }

    private static void putElement() {
        System.out.println("prepare to put element");
        try {
            queue.put(1);
        } catch (InterruptedException e1) {
            Thread.currentThread().interrupt();
        }
        System.out.println("put thread termination");
    }

    private static void putElement2() {
        System.out.println("prepare to put element");
        try {
            queue.put(2);
        } catch (InterruptedException e1) {
            Thread.currentThread().interrupt();
        }
        System.out.println("put thread termination");
    }

    private static void putElement3() {
        System.out.println("prepare to put element");
        try {
            queue.put(3);
        } catch (InterruptedException e1) {
            Thread.currentThread().interrupt();
        }
        System.out.println("put thread termination");
    }

    private static void takeElement() {
        System.out.println("prepare to take element");
        try {
            System.out.println(queue.take());
        } catch (InterruptedException e1) {
            Thread.currentThread().interrupt();
        }
        System.out.println("take thread termination");
    }

    private static void pollElement() {
        System.out.println("prepare to take element");
        try {
            System.out.println(queue.poll(2000L,TimeUnit.MILLISECONDS));
        } catch (InterruptedException e1) {
            Thread.currentThread().interrupt();
        }
        System.out.println("take thread termination");
    }
}
