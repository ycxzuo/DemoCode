package com.yczuoxin.concurrent.demo.lock.AQS.ReentrantReadWriteLock;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockTest {

    private volatile boolean cacheValid = false;
    private int currentValue = 0;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock readLock = lock.readLock();
    private Lock writeLock = lock.writeLock();

    /**
     * 测试用例
     * @throws InterruptedException
     */
    public void testLockDowngrading() throws InterruptedException {
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(2);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));
        for (int i = 0; i < 2; i ++){
            int finalI = i;
            executor.execute(new Thread(() ->  {
                try {
                    start.await();
                    TimeUnit.SECONDS.sleep(finalI);
                    System.out.println("after sleep " + finalI + " seconds, execute " + Thread.currentThread().getName());
                    cacheValid = false;
                    processCachedDataDownGrading(finalI);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    end.countDown();
                }
            },"thread-" + finalI));
        }
        start.countDown();
        end.await();
    }

    /**
     * 锁降级过程
     * @param num
     */
    private void processCachedDataDownGrading(int num){
        readLock.lock();
        if(!cacheValid){
            //必须先释放写锁
            readLock.unlock();
            writeLock.lock();
            try{
                //在更新数据之前做二次检查
                if(!cacheValid){
                    System.out.println(Thread.currentThread().getName() + " has updated!");
                    //将数据更新为和线程值相同，以便验证数据
                    currentValue = num;
                    cacheValid = true;
                    readLock.lock();
                }
            }finally {
                writeLock.unlock();
            }
        }
        process();
    }

    /**
     * 无锁降级的过程
     * @param num
     */
    private void processCachedData(int num){
        readLock.lock();
        if(!cacheValid){
            readLock.unlock();
            writeLock.lock();
            try{
                if(!cacheValid){
                    System.out.println(Thread.currentThread().getName() + " has updated!");
                    currentValue = num;
                    cacheValid = true;
                }
            }finally {
                writeLock.unlock();
            }
        }
        process();
    }

    private void process() {
        try {
            //模拟5秒的处理时间，并打印出当前值，在这个过程中cacheValid可能被其他线程修改，锁降级保证其他线程写锁被阻塞，数据不被改变
            TimeUnit.SECONDS.sleep(5);
            System.out.println(Thread.currentThread().getName() + ": " + currentValue);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (lock.getReadHoldCount() > 0) {
                readLock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReadWriteLockTest test = new ReadWriteLockTest();
        test.testLockDowngrading();
    }
}
