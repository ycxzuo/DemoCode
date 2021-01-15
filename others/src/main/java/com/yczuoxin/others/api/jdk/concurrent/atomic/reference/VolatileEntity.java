package com.yczuoxin.others.api.jdk.concurrent.atomic.reference;

public class VolatileEntity {

    // 此处的 name 字段不能使用 private 修饰，否则会抛出
    protected volatile String name;

    public VolatileEntity() {
        this.name = "VolatileEntity";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "VolatileEntity{" +
                "name='" + name + '\'' +
                '}';
    }
}
