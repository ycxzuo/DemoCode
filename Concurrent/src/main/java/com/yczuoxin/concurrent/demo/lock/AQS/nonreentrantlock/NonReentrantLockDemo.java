package com.yczuoxin.concurrent.demo.lock.AQS.nonreentrantlock;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;

public class NonReentrantLockDemo {

    final static NonReentrantLock lock = new NonReentrantLock();
    // 消费队列
    final static Condition notFull = lock.newCondition();
    // 生产队列
    final static Condition notEmpty = lock.newCondition();
    // AQS 队列
    final static Queue<String> queue = new LinkedBlockingQueue<>();
    final static int queueSize = 10;

    public static void main(String[] args) {

        Thread producer = new Thread(() -> {
            lock.lock();
            try {
                // 队列满了则等待
                while (queueSize == queue.size()) {
                    notEmpty.await();
                }

                // 队列中放入元素
                queue.add("ele");

                // 唤醒消费线程
                notFull.signalAll();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 释放锁
                lock.unlock();
            }
        });

        Thread consumer = new Thread(() -> {
            lock.lock();
            try {
                // 队列满了则等待
                while (0 == queue.size()) {
                    notFull.await();
                }

                // 消费一个元素
                String ele = queue.poll();
                System.out.println(ele);

                // 唤醒生产线程
                notEmpty.signalAll();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 释放锁
                lock.unlock();
            }
        });

        producer.start();
        consumer.start();
    }
}
