package com.yczuoxin.concurrent.demo.basis.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Unsafe 的测试，其需要 Bootstrap ClassLoader 加载，
 * 但是很明显 UnsafeTest 是由 App ClassLoader 加载的
 * 所以需要用反射来获取
 */
public class UnsafeTest {

    /**
     * 获取 Unsafe 的实例
    */
    static final Unsafe unsafe;

    /**
     * 记录变量 state 在类 TestUnsafe 中的偏移值
    */
    static final long stateOffset;

    /**
     * 创建一个变量 state 并初始化为 0
    */
    private volatile long state = 0;

    static {
        try {
            // 只能通过反射拿到 Unsafe，由于虚拟机做了验证，所以不能直接 new 出来
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
            // 获取 state 变量在 TestUnsafe 中的偏移值
            stateOffset = unsafe.objectFieldOffset(UnsafeTest.class.getDeclaredField("state"));
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            throw new Error(e);
        }
    }

    public static void main(String[] args) {
        // 创建实例，并且设置 state 值为 1
        UnsafeTest test = new UnsafeTest();
        // 调用创建的 unsafe 实例的 compareAndSwapInt 方法，将值 0 更改为 1
        System.out.println(unsafe.compareAndSwapInt(test, stateOffset, 0, 1));
    }
}