//package com.yczuoxin.concurrent.demo.basis.contended;
//
//import sun.misc.Contended;
//
///**
// * public final static class FilledLong {
// *      public volatile long value = 0L;
// *      public long p1, p2, p3, p4, p5, p6
// * }
// *
// * 假如缓存行为 64 宇节，那么我们在 FilledLong 类里面填充了 6 个 long 类型的变
// * 量，每个 long 类型变量占用 8 字节，加上 value 变量的 8 字节总共 56 字节。另外，这里
// * FilledLong 是一个类对象，而类对象的字节码的对象头占用 8 字节，所以一个 FilledLong
// * 对象实际会占用 64 字节的内存，这正好可以放入一个缓存行。
// *
// * 该注解是对类进行字节填充
// *
// * 该注解可以修饰类，也可以修饰变量，例如 Thread 类中的
// *  The current seed for a ThreadLocalRandom
// *  @sun.misc.Contended("tlr")
// *  long threadLocalRandomSeed;
// *
// *  Probe hash value; nonzero if threadLocalRandomSeed initialized
// *  @sun.misc.Contended("tlr")
// *  int threadLocalRandomProbe;
// *
// *  Secondary seed isolated from public ThreadLocalRandom sequence
// *  @sun.misc.Contended("tlr")
// *  int threadLocalRandomSecondarySeed;
// *
// *  该注解只能用在 Java 核心包中，如果用户类想使用该注解，需要添加 JVM 参数
// *      -XX: -RestrictContended
// *  填充宽度默认是 128，可以使用 JVM 参数自定义宽度
// *      -XX: ContendedPaddingWidth
// *
// */
//public class ContendedDemo {
//    @Contended
//    public final static class FilledLong {
//        public volatile long value = 0L;
//    }
//}
