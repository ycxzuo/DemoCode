package com.yczuoxin.concurrent.demo.list;


import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWriteListDemo {

    public static void main(String[] args) throws InterruptedException {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("hello");
        list.add("world");
        list.add("welcome");
        list.add("to");
        list.add("China");

        Thread threadOne = new Thread(() -> {
            list.set(1,"Java");
            list.remove(2);
            list.remove(3);
        });

        // 在线程启动前获取迭代器
        Iterator<String> it = list.iterator();

        // 启动线程
        threadOne.start();

        // 等待线程执行完毕
        threadOne.join();

        // 遍历元素
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

}
