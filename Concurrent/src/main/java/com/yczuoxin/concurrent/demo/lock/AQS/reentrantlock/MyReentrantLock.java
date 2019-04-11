package com.yczuoxin.concurrent.demo.lock.AQS.reentrantlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 手写的可重入锁
 */
public class MyReentrantLock implements Lock {

    @Override
    public void lock() {
        sync.lock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }

    private final Sync sync = new Sync();

    private static class Sync extends AbstractQueuedSynchronizer {

        final void lock() {
            if (compareAndSetState(0, 1)){
                setExclusiveOwnerThread(Thread.currentThread());
            } else {
                acquire(1);
            }
        }

        @Override
        protected boolean tryAcquire(int acquire) {
            int c = getState();
            Thread current = Thread.currentThread();
            if (c == 0) {
                if (compareAndSetState(0, acquire)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            } else if (current == getExclusiveOwnerThread()) {
                int addC = c + acquire;
                if (addC < 0) {
                    throw new Error("Maximum lock count exceeded");
                }
                setState(addC);
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int acquire) {
            int c = getState() - acquire;
            Thread current = Thread.currentThread();
            if (getExclusiveOwnerThread() != current) {
                throw new IllegalMonitorStateException();
            }
            boolean free = false;
            if (c == 0) {
                setExclusiveOwnerThread(null);
                free = true;
            }
            setState(c);
            return free;
        }

        final Condition newCondition() {
            return new ConditionObject();
        }
    }

}
