package com.yczuoxin.concurrent.demo.basis.lock;

/**
 * 独占锁
 * 独占锁可以保证任何时候都只有一个线程能得到锁，ReentrantLock 就是以独占的方式实现的，
 * 它是一种悲观锁，由于每次访问资源都先加上互斥锁，这限制了并发性，因为读操作并不会影响数据的一致性，
 * 而独占锁只允许在同一时间有一个线程读取数据，其他线程都必须等待当前线程释放锁才能进行读取
 */
public class ExclusiveLock {
}
