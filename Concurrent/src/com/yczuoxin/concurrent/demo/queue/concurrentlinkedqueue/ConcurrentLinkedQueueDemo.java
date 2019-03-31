package com.yczuoxin.concurrent.demo.queue.concurrentlinkedqueue;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * ConcurrentLinkedQueue 是线程安全的无边界非阻塞队列，其底层是单向链表
 */
public class ConcurrentLinkedQueueDemo {

    public static void main(String[] args) {
        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue();
        queue.add("aaa");
    }

}
