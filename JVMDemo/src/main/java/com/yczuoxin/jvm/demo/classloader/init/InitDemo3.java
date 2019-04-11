package com.yczuoxin.jvm.demo.classloader.init;

import com.yczuoxin.jvm.demo.classloader.bean.ConstClass;

/**
 * 虽然在 InitDemo3 中使用到了 ConstClass 类中的常量HELLO_WORLD，但其实在编译阶段通过常量传播优化，
 * 已经将“hello world”值存储到了 InitDemo3 类的常量池中。此时 InitDemo3 与 ConstClass 没有任何联系
 */
public class InitDemo3 {
    public static void main(String[] args) {
        System.out.println(ConstClass.HELLO_WORLD);
    }
}