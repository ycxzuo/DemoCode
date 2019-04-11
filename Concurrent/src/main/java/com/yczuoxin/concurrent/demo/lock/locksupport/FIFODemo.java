package com.yczuoxin.concurrent.demo.lock.locksupport;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * 先进先出的锁
 */
public class FIFODemo {

    static class FIFOMutex {
        private final AtomicBoolean locked = new AtomicBoolean(false);

        /**
         * 线程队列
         */
        private final Queue<Thread> waiters = new ConcurrentLinkedQueue<>();

        /**
         * 加锁的方法
         */
        public void lock(){
            boolean wasInterrupted = false;
            Thread current = Thread.currentThread();
            waiters.add(current);

            // 判断是队首元素或者当前锁没有被其他的线程获取
            while (waiters.peek() != current || !locked.compareAndSet(false, true)){
                // 如果不是，则调用 park() 方法挂起自己
                LockSupport.park(this);
                // 如果 park() 方法是因为方法中断而返回（及被唤醒），则忽略中断，并重置中断标志
                // 并且做个标记，然后继续
                if (Thread.interrupted()) {
                    wasInterrupted = true;
                }
            }

            waiters.remove();
            // 判断标记，如果标记为 true，则中断该线程，使其中断标志恢复
            if (wasInterrupted) {
                current.interrupt();
            }
        }

        /**
         * 解锁的方法
         */
        public void unlock(){
            locked.set(false);
            LockSupport.unpark(waiters.peek());
        }
    }
}
