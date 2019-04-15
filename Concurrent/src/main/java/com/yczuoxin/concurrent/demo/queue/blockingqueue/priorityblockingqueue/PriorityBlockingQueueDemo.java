package com.yczuoxin.concurrent.demo.queue.blockingqueue.priorityblockingqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class PriorityBlockingQueueDemo {
    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new PriorityBlockingQueue<>(2);
        queue.offer(2);
        queue.offer(4);
        queue.offer(3);
        queue.offer(1);
        System.out.println(queue);
    }
}
