package com.yczuoxin.concurrent.demo.queue.blockingqueue.priorityblockingqueue;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;

public class PriorityBlockingQueueDemo {

    static class Demo implements Comparable<Demo> {

        private int priority = 0;
        private String order;

        private void setOrder(String order) {
            this.order = order;
        }

        private int getPriority() {
            return priority;
        }

        private void setPriority(int priority) {
            this.priority = priority;
        }

        @Override
        public int compareTo(Demo o) {
            if (this.priority >= o.getPriority()) {
                return 1;
            } else {
                return -1;
            }
        }

        private void print() {
            System.out.println(order + "->" + priority);
        }

    }

    public static void main(String[] args) {
        PriorityBlockingQueue<Demo> queue = new PriorityBlockingQueue<>();
        Random random = new SecureRandom();
        System.out.println("insert order");
        for (int i = 0; i < 10; i++) {
            Demo demo = new Demo();
            demo.setPriority(random.nextInt(10));
            System.out.print(demo.getPriority() + ", ");
            demo.setOrder("order: " + i);
            queue.offer(demo);
        }

        System.out.println();
        System.out.println("queue order");
        queue.forEach(p -> System.out.print(p.getPriority() + ", "));
        System.out.println();

        while (!queue.isEmpty()) {
            Demo demo = queue.poll();
            if (null != demo) {
                demo.print();
            }
        }
    }
}
