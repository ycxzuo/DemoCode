package com.yczuoxin.others.spring.proxy.my;

public class Tank implements Moveable{
    @Override
    public void move() {
        System.out.println("Tank Method invoke!");
    }
}
