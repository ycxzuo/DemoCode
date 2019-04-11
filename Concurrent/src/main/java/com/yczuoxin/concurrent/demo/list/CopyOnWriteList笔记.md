# CopyOnWriteList笔记

## 概述

`CopyOnWriteList` 是一个写时复制的策略保证 list 的一致性，所以在其**增删改**的操作中都使用了独占锁 `ReentrantLock` 来保证某个时间只有一个线程能对 list 数组进行修改。其底层是对数组的修改，调用 `Arrays.copyarray()` 方法进行对数组的复制，在底层还是调用的 C++ 去进行的数组的复制 `System.copyarray()`



## 修改

在修改时如果需要修改的元素和之前元素值相同，会调用 `setAarray(elements)` 方法将之前的数组又塞回去，看似很多余，其实之中暗藏玄机。 `array` 字段是被 `volutile` 修饰，所以调用 `setArray()` 方法会是缓存行内的 `array` 字段缓存失败，并防止指令重拍，即 `happens-before` 原理



## 弱一致性的迭代器

当调用迭代器 `iterator()` 方法时，实际上会返回一个 `COWIterator` 对象，`COWIterator` 对象会拿到当前的 `array` 保存在 `snapshot` 变量中，`cursor` 是遍历的游标。

虽然 `snapshot` 是指针引用，但是，叫快照不符合语义，但是在对 list 进行增删改操作时，其实是操作的是复制出来的新数组，所以 `snapshot` 不会改变，所以其迭代器是弱一致性的。 