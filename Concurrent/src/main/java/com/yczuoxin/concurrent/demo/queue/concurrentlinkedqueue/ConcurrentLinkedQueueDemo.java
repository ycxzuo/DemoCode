package com.yczuoxin.concurrent.demo.queue.concurrentlinkedqueue;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * ConcurrentLinkedQueue 是线程安全的无边界非阻塞队列，其底层是单向链表
 */
public class ConcurrentLinkedQueueDemo {

    private static Queue<String> queue = new ConcurrentLinkedQueue<>();
    // private static Queue<String> queue = new LinkedList<>();// 会出现 java.util.ConcurrentModificationException

    public static void main(String[] args) {
        Thread threadOne = new Thread(ConcurrentLinkedQueueDemo::operator,"ThreadOne");
        Thread threadTwo = new Thread(ConcurrentLinkedQueueDemo::operator,"ThreadTwo");

        threadOne.start();
        threadTwo.start();
    }

    private static void operator(){
        int i = 0;
        while (i++ < 6) {
            String val = Thread.currentThread().getName() + i;
            queue.add(val);
            printAll();
        }
    }

    private static void printAll(){
        String value;

        Iterator iter = queue.iterator();
        while (iter.hasNext()) {
            value = (String) iter.next();
            System.out.print(value + ",");
        }
        System.out.println();
    }
}
