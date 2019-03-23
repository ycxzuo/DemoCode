# AQS笔记

## 概述

AQS 是 `AbstractQueueSynchronized` 抽象同步队列的简称，它是实现同步器的基础组件，并发包中锁的底层就是使用 AQS 实现的。AQS 是一个 FIFO 的双向队列，其内部通过节点 `head` 和 `tail` 记录队首和队尾元素，队列元素的类型为 Node。



## 源码

### state（字段）

单一的状态信息，可以通过 `getState()`、`setState()`、`compareAndSetState()` 函数修改其值，对于不同的实现有不同的意义，如

* ReentrantLock：		 当前线程获取锁的可重入次数
* ReentrantReadWriteLock：高 16 位表示获取读锁的次数，低 16 位表示获取写锁的次数
* semaphore：                      当前可用信号的个数
* CountDownLatch：            计数器当前的值

对 `state` 操作是 AQS 县城同步的关键。根据 `state` 是否属一个线程，操作 `state`的方式分为**独占方式**和**共享方式**。



#### 独占方式

如果一个线程获取到了资源，就会标记这个线程获取到了，其他线程尝试操作 `state` 获取资源时会发现当前该资源不是自己持有的，就会在获取失败后被阻塞

#### 共享方式

当多个线程去请求资源时通过 CAS 方式竞争获取资源，当一个线程获取到了资源后，另一个线程再次去获取时，如果当前资源还能满足它的需要，则当前线程需要使用 CAS 方式进行获取即可，如果不满足，则把当前线程放入阻塞队列，比如资源不足以满足需求



### 队列

#### 入队操作

当 一个线程获取锁失败后该线程会被转换为 `Node` 节点，然后就会使用 `enq(Node node)` 方法将该节点插入到 AQS 的阻塞队列， JDK9 之前使用的是递归确保节点正确的增加

```java
private Node enq(Node node) {
        for (;;) {
            // 将当前节点 tail 指向 oldTail
            Node oldTail = tail;
            // 如果当前节点不为空，说明队列已经被初始化
            if (oldTail != null) {
                // 使用 VarHandle 以原子操作将当前节点的 prev 指向 tail
                node.setPrevRelaxed(oldTail);
                // 使用 CAS 尝试将 oldTail 设置为 node
                if (compareAndSetTail(oldTail, node)) {
                    oldTail.next = node;
                    return oldTail;
                }
            } else {
                // 初始化同步队列
                initializeSyncQueue();
            }
        }
    }
```





### Node（内部类）

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



### ConditionObject（内部类）

用来结合锁实现线程同步，`ConditionObject` 可以直接访问 AQS 对象内部的变量。`ConditionObject` 是条件变量，每个条件变量对应一个条件队列（单向链表队列），其用来存放调用条件变量的 `await()` 方法后被阻塞的线程，这个条件队列的头、尾元素分别为 `firstWaiter` 和 `lastWaiter`

#### await()

与 `Object.await()` 方法作用类似，同时也需要获取到锁，否则会抛出 `IllegalMonitorStateException` ，调用该方法会在内部新建一个类型为 `Node.CONDITION` 的 `Node` 节点，然后插入条件变量维护的条件队列的尾部，并释放锁

#### signal()

与 `Object.notify()` 方法作用类似，同时也需要获取到锁，否则会抛出 `IllegalMonitorStateException` ，调用该方法会把条件队列面队头的一个线程节点从条件队列里面移除并放入 AQS 的阻塞队列里面，然后激活这个线程 。 

#### 条件变量 ConditionObject

如示例代码，`lock.newCondition()` 的作用其实是 new 了一个在 AQS 内部声明的 `ConditionObject` 对象 ， `ConditionObject` 是 AQS 的内部类 ，可以访问 AQS 内部的变量（例如状态变量 `state` ）和方法。在每个条件变量内部都维护了 一个条件队列（单向链表，FIFO），用来存放调用条件变量的 `await()` 方法时被阻塞的线程

该队列与 AQS 队列不同，AQS 是其自己维护的 `Node` 双向链表，而 `ConditionObject` 是维护的自己内部的一个条件队列（ `Node` 的单向链表 ）



### 独占方式资源的获取和释放

#### 获取

当一个线程调用 `acquire(int arg)` 方法获取独占资源时，会首先使用 `tryAcquire()` 方法尝试获取资源（设置状态变量 `state` 的值），成功则直接返回，失败则将当前线程封装为类型为 Node.EXCLUSIVE 的 Node 节点后，插入到 AQS 阻塞队列的尾部，并调用 `LockSupport.park(this) ` 挂起自己

#### 释放

当一个线程调用 `release(int arg)` 方法释放独占资源时，会尝试使用 `tryRelease()` 方法释放资源（设置状态变量 `state` 的值），然后调用 `LockSupport.unpark(thread)` 方法激活 AQS 队列里面被阻塞的一个线程（ thread ）。被激活的线程则使用  `tryAcquire()` 尝试，看当前状态变量 `state` 的值是否能满足自己的需要，满足则该线程被激活，然后继续向下执行，否则还是会被放入 AQS 队列并挂起

#### 注意

AQS 类没有提供可用的  `tryAcquire()`  方法和 `tryRelease()` 方法，正如 AQS 是锁阻塞，和同步器的基础框架一样，`tryAcquire()`  方法和 `tryRelease()` 方法需要由具体的子类来实现。子类在实现 `tryAcquire()`  方法和 `tryRelease()` 方法时，要根据具体场景使用 CAS 算法尝试修改 `state` 状态值，成功返回 true，失败返回 false。子类还需定义，在调用 `acquire(int arg)` 和 `release(int arg)` 方法时，`state` 状态值的增减代表什么含义



### 共享方式资源的获取和释放

#### 获取

当线程调用 `acquireShared(int arg)` 方法获取共享资源时，会首先使用 `tryAcquireShared()` 尝试获取资源（设置状态变量 `state` 的值），成功则直接返回，失败则将当前线程封装为类型为 Node.SHARED 的 Node 节点后，插入到 AQS 阻塞队列的尾部，并调用 `LockSupport.park(this) ` 挂起自己

#### 释放

当一个线程调用 `releaseShard(int arg)` 方法释放共享资源时，会尝试使用 `tryReleaseShared()` 方法释放资源（设置状态变量 `state` 的值），然后调用 `LockSupport.unpark(thread)` 方法激活 AQS 队列里面被阻塞的一个线程（ thread ）。被激活的线程则使用  `tryAcquireShared()` 尝试，看当前状态变量 `state` 的值是否能满足自己的需要，满足则该线程被激活，然后继续向下执行，否则还是会被放入 AQS 队列并挂起

#### 注意

与独占方式相同



### isHeldExclusively()

基于 AQS 实现的锁除了需要重写上面介绍的方法外，还需要重写 `isHeldExclusively()` 方法，来判断锁时被当前线程独占还是共享



### Interruptibly

方法名带 `Interruptibly` 关键字的方法要对中断进行响应，获取资源时或者获取资源失败被挂起时，其他线程中断了该线程，那么该线程会抛出 `InterruptedException` 异常而返回

方法名不带 `Interruptibly` 关键字的方法被其他线程中断后，不会因为中断而抛出异常，它还是获取资源或者挂起



## 自定义同步器

