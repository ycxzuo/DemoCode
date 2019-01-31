package com.yczuoxin.jvm.demo.methodinvoke.dynamictypelanguage;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodHandles.lookup;

/**
 * 利用 MethodHandle 实现 Java 的动态类型语言功能
 * @author yczuoxin
 */
public class MethodHandleDemo {
    static class ClassA {
        public void println(String s) {
            System.out.println("ClassA execute " + s);
        }
    }

    public static void main(String[] args) throws Throwable {
        Object obj = System.currentTimeMillis() % 2 == 0 ? System.out : new ClassA();
        // 无论 obj 最终是哪个实现类，下面这句都能正确调用到 println 方法
        getPrintlnMH(obj).invokeExact("yczuoxin");
    }

    private static MethodHandle getPrintlnMH(Object receiver) throws Throwable {
        // MethodType：代表“方法类型”，包含了方法的返回值(methodType()的第一个参数)和
        // 具体参数(methodType()的第二个及以后的参数)
        MethodType mt = MethodType.methodType(void.class, String.class);
        // lookup()方法来自于methodHandles.lookup,这句的作用是在指定类中查找符合给定的
        // 方法名称、方法类型，并且符合调用权限的方法句柄
        // 因为这里调用的是一个虚方法，按照 Java 语言的规则，方法第一个参数是隐式的，
        // 代表该方法的接收者，也即是 this 指向的对象，这个参数以前是放在参数列表中进行传递的，
        // 而现在提供了 bingTo()发那个发来完成这件事情
        return lookup().findVirtual(receiver.getClass(), "println", mt).bindTo(receiver);
    }
}
