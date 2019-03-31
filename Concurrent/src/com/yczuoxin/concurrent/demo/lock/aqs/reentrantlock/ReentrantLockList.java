package com.yczuoxin.concurrent.demo.lock.aqs.reentrantlock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ReentrantLockList {

    private List<String> list = new ArrayList<>(Arrays.asList("1","2","3"));

    private volatile MyReentrantLock lock = new MyReentrantLock();

    public void add(String e) {
        lock.lock();
        try {
            list.add(e);
            System.out.println(Thread.currentThread().getName() + " add " + e);
        } finally {
            lock.unlock();
        }
    }

    public void remove(String e) {
        lock.lock();
        try {
            list.remove(e);
            System.out.println(Thread.currentThread().getName() + " remove " + e);
        } finally {
            lock.unlock();
        }
    }

    public String get(int index) {
        lock.lock();
        try {
            String e = list.get(index);
            System.out.println(Thread.currentThread().getName() + " get " + e);
            return e;
        } finally {
            lock.unlock();
        }
    }

    public void set(String e, int index) {
        lock.lock();
        try {
            String oldElement = get(index);
            sleepOneSecond(1);
            remove(oldElement);
            sleepOneSecond(1);
            add(e);
            System.out.println(Thread.currentThread().getName() + " set " + e);
        } finally {
            lock.unlock();
        }
    }

    private void sleepOneSecond(int time) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
