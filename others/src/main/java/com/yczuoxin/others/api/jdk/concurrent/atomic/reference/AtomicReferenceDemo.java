package com.yczuoxin.others.api.jdk.concurrent.atomic.reference;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDemo {
    public static void main(String[] args) {
        VolatileEntity entity = new VolatileEntity();
        AtomicReference<VolatileEntity> reference = new AtomicReference<>(entity);
        VolatileEntity newEntity = new VolatileEntity();
        newEntity.setName("demo");
        reference.compareAndSet(entity, newEntity);
        System.out.println(reference.get());
    }
}
