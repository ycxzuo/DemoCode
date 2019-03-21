# AQS笔记

## 概述

AQS 是 `AbstractQueueSynchronized` 抽象同步队列的简称，它是实现同步器的基础组件，并发包中锁的底层就是使用 AQS 实现的。AQS 是一个 FIFO 的双向队列，其内部通过节点 `head` 和 `tail` 记录队首和队尾元素，队列元素的类型为 Node。



## state（字段）

单一的状态信息，可以通过 `getState()`、`setState()`、`compareAndSetState()` 函数修改其值，对于不同的实现有不同的意义，如

* ReentrantLock：		 当前线程获取锁的可重入次数
* ReentrantReadWriteLock：高 16 位表示获取读锁的次数，低 16 位表示获取写锁的次数
* semaphore：                      当前可用信号的个数
* CountDownLatch：            计数器当前的值



## Node（内部类）

队列元素的类型

* Thread：	 存放进入 AQS 队列里面的线程
* SHARED：     标记该线程是获取共享资源时被阻塞
* EXCLUSIVE：标记该线程是获取独占资源时被阻塞
* waitStatus：    记录当前线程等待状态
  * CANCELLED：  线程被取消了
  * SIGNAL：          线程需要被唤醒
  * CONDITION：   线程在条件队列里面等待
  * PROPAGATE： 释放共享资源时需要通知其它节点
* prev：              记录当前节点的前驱节点
* next：              记录当前节点的后继节点



## ConditionObject（内部类）

用来结合锁实现线程同步，`ConditionObject` 可以直接访问 AQS 对象内部的变量。`ConditionObject` 是条件变量，每个条件变量对应一个条件队列（单向链表队列），其用来存放调用条件变量的 `await()` 方法后被阻塞的线程，这个条件队列的头、尾元素分别为 `firstWaiter` 和 `lastWaiter`

