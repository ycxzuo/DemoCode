package com.yczuoxin.jvm.demo.methodinvoke.dispatch;

/**
 * 静态分派，静态分派发生在编译阶段，分派的动作不是由虚拟机来执行的
 * 静态分派选择版本由静态类型（或外观类型）来决定
 * 编译器虽然能确定出方法的重载版本，但重载版本并不是唯一的，只能确定一个更加合适的版本
 * @author yczuoxin
 */
public class StaticDispatch {

    static abstract class Human {
    }

    static class Man extends Human {
    }

    static class Woman extends Human {
    }

    static void sayHello (Human guy) {
        System.out.println("hello guy");
    }

    static void sayHello (Man guy) {
        System.out.println("hello man");
    }

    static void sayHello (Woman guy) {
        System.out.println("hello woman");
    }

    public static void main(String[] args) {
        // 实际类型变化
        Human man = new Man();
        Human woman = new Woman();
        StaticDispatch dispatch = new StaticDispatch();
        dispatch.sayHello(man);
        dispatch.sayHello(woman);
        // 静态类型变化
        dispatch.sayHello((Man) man);
        dispatch.sayHello((Woman) woman);
    }
}
