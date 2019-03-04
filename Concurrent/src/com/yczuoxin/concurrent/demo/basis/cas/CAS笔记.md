# CAS操作

## 概念

CAS 即`Compare and Swap`，是 JDK提供的非阻塞原子性操作，他通过硬件保证了比较——更新操作的原子性。JDK 的 Unsafe 类提供了一系列的 `compareAndSwap*`方法，以`compareAndSwapLong()`为例

```java
boolean compareAndSwapLong(Object obj, long valueOffset, long expect, long update)
```

* obj -> 对象内存位置
* valueOffset -> 对象中的内存的偏移量
* expect -> 变量预期的值
* update -> 新的值



## 问题

CAS 有一个经典的 ABA 问题，其产生是因为变量的状态值产生了环形转换，就是变量的值可以从 A 到 B，然后再从 B 到 A。

如果变量的值只能朝着一个方向转换，比如 A 到 B，B 到 C，不构成环形就不会出现问题。JDK 中的 AtomicStampedReference 类给每个变量的状态值都配备了一个时间戳，从而避免了 ABA 问题的产生