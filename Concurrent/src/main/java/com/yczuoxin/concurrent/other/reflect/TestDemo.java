package com.yczuoxin.concurrent.other.reflect;

import com.yczuoxin.concurrent.demo.basis.bean.Person;

import java.util.List;

public class TestDemo {

    private int age;

    private String name;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean print(List<Person> person){
        System.out.println("age : " + person.get(0).getAge() + ", name : " + person.get(0).getName());
        return true;
    }

    public void test1(Person[] classes) {
        return;
    }
}
