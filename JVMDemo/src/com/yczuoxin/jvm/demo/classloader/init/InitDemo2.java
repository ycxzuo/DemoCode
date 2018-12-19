package com.yczuoxin.jvm.demo.classloader.init;

import com.yczuoxin.jvm.demo.classloader.bean.SuperClass;

/**
 *  -XX:+TraceClassLoading
 *  发现父类也并没有初始化，但是这段代码里面出发了另一个名为[Lcom.yczuoxin.jvm.demo.classloader.bean.SuperClass的类的初始化阶段，
 *  对于用户代码而言，这并不是一个合法的类名称，它是一个由虚拟机自动生成的，直接继承与java.lang.Object的子类，创建动作由字节码指令newarray触发
 */
public class InitDemo2 {
    public static void main(String[] args) {
        SuperClass[] scarr = new SuperClass[10];
    }
}