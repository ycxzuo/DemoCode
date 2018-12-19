package com.yczuoxin.concurrent.demo.future;

import com.yczuoxin.concurrent.demo.bean.Person;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureTest {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        FutureTask<Person> task = new FutureTask(() -> {
            System.out.println("first");
            Thread.sleep(3000);
            FutureTask<Person> task1 = new FutureTask<>(() -> {
                System.out.println("task1");
                Thread.sleep(1000);
                int i = 1 / 0;
                return new Person("bbbb", 12);
            });
            new Thread(task1).start();
            if (!task1.isDone()) {
                System.out.println("task1未结束");
            }
            Person person = task1.get();
            System.out.println(person.toString());
            System.out.println("ok");
            return new Person("aaa",11);
        });
        System.out.println("准备");
        new Thread(task).start();
        Thread.sleep(1000);
        if (!task.isDone()) {
            System.out.println("未结束");
        }
        Person p = task.get();
        System.out.println(p.toString());
    }
}
