package com.yczuoxin.others.api.jdk.concurrent.atomic.reference;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class AtomicReferenceFieldUpdaterDemo {
    public static void main(String[] args) {
        AtomicReferenceFieldUpdater<VolatileEntity, String> updater = AtomicReferenceFieldUpdater.newUpdater(VolatileEntity.class, String.class, "name");
        VolatileEntity entity = new VolatileEntity();
        updater.compareAndSet(entity, "AtomicReferenceFieldUpdaterDemo", "AtomicReferenceFieldUpdaterDemo");
        System.out.println(entity);
        updater.compareAndSet(entity, "VolatileEntity", "AtomicReferenceFieldUpdaterDemo");
        System.out.println(entity);
    }
}
