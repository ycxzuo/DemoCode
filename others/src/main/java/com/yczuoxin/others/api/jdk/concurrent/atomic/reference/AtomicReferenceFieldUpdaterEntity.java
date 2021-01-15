package com.yczuoxin.others.api.jdk.concurrent.atomic.reference;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class AtomicReferenceFieldUpdaterEntity {

    private volatile String name;

    private AtomicReferenceFieldUpdater<AtomicReferenceFieldUpdaterEntity, String> updater = AtomicReferenceFieldUpdater.newUpdater(AtomicReferenceFieldUpdaterEntity.class, String.class, "name");

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void compareAndSet(String expect, String update) {
        updater.compareAndSet(this, expect, update);
    }

    @Override
    public String toString() {
        return "AtomicReferenceFieldUpdaterEntity{" +
                "name='" + name + '\'' +
                '}';
    }

    public static void main(String[] args) {
        AtomicReferenceFieldUpdaterEntity entity = new AtomicReferenceFieldUpdaterEntity();
        entity.setName("AtomicReferenceFieldUpdaterEntity");
        entity.compareAndSet("test", "test");
        System.out.println(entity);
        entity.compareAndSet("AtomicReferenceFieldUpdaterEntity", "test");
        System.out.println(entity);
    }
}
