# AQS笔记

## 概述

AQS 是 `AbstractQueueSynchronized` 抽象同步队列的简称，它是实现同步器的基础组件，并发包中锁的底层就是使用 AQS 实现的。AQS 是一个 FIFO 的双向队列，其内部通过节点 `head` 和 `tail` 记录队首和队尾元素，队列元素的类型为 Node。



## state（字段）

单一的状态信息，可以通过 `getState()`、`setState()`、`compareAndSetState()` 函数修改其值，对于不同的实现有不同的意义，如

* ReentrantLock：		 当前线程获取锁的可重入次数
* ReentrantReadWriteLock：高 16 位表示获取读锁的次数，低 16 位表示获取写锁的次数
* semaphore：                      当前可用信号的个数
* CountDownLatch：            计数器当前的值

对 `state` 操作是 AQS 县城同步的关键。根据 `state` 是否属一个线程，操作 `state`的方式分为**独占方式**和**共享方式**。



### 独占方式

如果一个线程获取到了资源，就会标记这个线程获取到了，其他线程尝试操作 `state` 获取资源时会发现当前该资源不是自己持有的，就会在获取失败后被阻塞

### 共享方式

当多个线程去请求资源时通过 CAS 方式竞争获取资源，当一个线程获取到了资源后，另一个线程再次去获取时，如果当前资源还能满足它的需要，则当前线程需要使用 CAS 方式进行获取即可，如果不满足，则把当前线程放入阻塞队列，比如资源不足以满足需求



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



## 独占方式资源的获取和释放

### 获取

当一个线程调用 `acquire(int arg)` 方法获取独占资源时，会首先使用 `tryAcquire()` 方法尝试获取资源（设置状态变量 `state` 的值），成功则直接返回，失败则将当前线程封装为类型为 Node.EXCLUSIVE 的 Node 节点后，插入到 AQS 阻塞队列的尾部，并调用 `LockSupport.park(this) ` 挂起自己

### 释放

当一个线程调用 `release(int arg)` 方法释放独占资源时，会尝试使用 `tryRelease()` 方法释放资源（设置状态变量 `state` 的值），然后调用 `LockSupport.unpark(thread)` 方法激活 AQS 队列里面被阻塞的一个线程（ thread ）。被激活的线程则使用  `tryAcquire()` 尝试，看当前状态变量 `state` 的值是否能满足自己的需要，满足则该线程被激活，然后继续向下执行，否则还是会被放入 AQS 队列并挂起

### 注意

AQS 类没有提供可用的  `tryAcquire()`  方法和 `tryRelease()` 方法，正如 AQS 是锁阻塞，和同步器的基础框架一样，`tryAcquire()`  方法和 `tryRelease()` 方法需要由具体的子类来实现。子类在实现 `tryAcquire()`  方法和 `tryRelease()` 方法时，要根据具体场景使用 CAS 算法尝试修改 `state` 状态值，成功返回 true，失败返回 false。子类还需定义，在调用 `acquire(int arg)` 和 `release(int arg)` 方法时，`state` 状态值的增减代表什么含义



### 共享方式资源的获取和释放

