package com.yczuoxin.datastructure.demo;

import com.yczuoxin.datastructure.structure.Queue;

public class QueueDemo {
    public static void main(String[] args) {
        Queue<String> queue = new Queue<>();
        queue.offer("1");
        queue.offer("2");
        queue.offer("3");
        queue.offer("4");
        queue.offer("5");
        queue.offer("6");
        queue.offer("7");
        queue.offer("8");
        queue.offer("9");
        queue.offer("10");

        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.peek());
        System.out.println(queue.poll());

        queue.offer("11");
        queue.offer("12");
        queue.offer("13");

        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());

        System.out.println(queue.size());
    }
}
