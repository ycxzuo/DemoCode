# LongAdder笔记

## 产生背景

`AtomicLong`在高并发下大量线程会同时去竞争更新同一个原子变量，但是由于同时只有一个线程的 CAS 操作会成功，这就造成大量线程竞争失败后，会通过无限循环不断进行自旋尝试 CAS 操作，这会白白浪费 CPU 资源



## 优化方式

`LongAdder`将一个变量分解为多个 Cell 变量，让同样多的线程去竞争多个资源，多个线程在竞争同一个 Cell 原子变量时如果失败了，他并不是在当前 Cell 变量上一直自旋，而是尝试在其他 Cell 的变量上进行 CAS 尝试。这个改变增加了当前线程重试 CAS 成功的可能性，在获取`LongAdder`当前值时，是把所有 Cell 变量的 value 值累加后再加上 base 返回Cell 是其父类`Striped64`的一个内部类



## Cell 的惰性加载优化方案

维护了一个延迟初始化的原子性更新数组（默认情况下 Cell 数组是 null）和一个基值变量 base。由于 Cells 占用的内存是相对比较大的，所以一开始并不创建它，而是在需要时创建。
一开始判断 Cell 数组是 null 并且并发线程较少时，所有的累加操作都是对 base 变量进行的。保持 Cell 数组的大小为 2 的 N 次方，在初始化时 Cell 数组中的 Cell 元素个数为 2，数组里面的变量实体是 Cell 类型。Cell 类型是 `AtomicLong`的一个改进，用来减少缓存的争用，也就是解决伪共享问题



## Cell 类的字节填充

对于大多数孤立的多个原子操作进行字节填充浪费的，因为原子性操作都是无规律的分散在内存中的（也就是说多个原子性变量内存地址是不连续的），多个原子变量被放入同一个缓存行的可能性很小。但是原子性数组的内存地址是连续的，所以数组内的多个元素能经常共享缓存行，因此这里使用`@sun.misc.Contended`注解对 Cell 类进行字节填充，防止多个元素共享一个缓存行，在性能上是提升



## 其他

`add()`是进行增加的方法，如果前面的增加方法执行成功，则不会执行`longAccumulate()`方法，其中会有 Cell 的初始化和扩容的步骤