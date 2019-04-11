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
  * CANCELLED(1)：  线程被取消了
  * SIGNAL(-1)：         线程需要被唤醒
  * CONDITION(-2)：  线程在条件队列里面等待，因为调用了 `Condition.await()` 而被阻塞
  * PROPAGATE(-3)：释放共享资源时需要通知其它节点
  * 0:                        以上都没有
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

其中 `tryAcquireShared()` 的返回值有 3 种

* 0 -> 表示当前线程获取共享锁成功，但它后续的线程是无法继续获取的，也就是不需要把它后面等待的节点唤醒
* 正数 -> 表示当前线程获取共享锁成功且它后续等待的节点也有可能继续获取共享锁成功，也就是说此时需要把后续节点唤醒让它们去尝试获取共享锁
* 负数 -> 表示获取锁失败，需要进入等待队列

```java
private void doAcquireShared(int arg) {
    // 创建一个新结点（共享模式），加入到队尾
    final Node node = addWaiter(Node.SHARED);
    boolean failed = true;
    try {
        boolean interrupted = false;
        for (;;) {
            // 拿到前驱节点
            final Node p = node.predecessor();
            // 前驱节点是头节点
            if (p == head) {
                // 尝试获取共享锁
                int r = tryAcquireShared(arg);
                // 获取成功
                if (r >= 0) {
                    // 将该节点设置为头节点
                    setHeadAndPropagate(node, r);
                    // 将引用值为 null，方便垃圾收集器回收
                    p.next = null; // help GC
                    if (interrupted)
                        selfInterrupt();
                    failed = false;
                    return;
                }
            }
            // 前继节点判断当前线程是否应该被阻塞，如果前继节点处于CANCELLED状态，则顺便删除这些节点重新构造队列
            if (shouldParkAfterFailedAcquire(p, node) &&
                // 把当前线程挂起，从而阻塞住线程的调用栈
                parkAndCheckInterrupt())
                interrupted = true;
        }
    } finally {
        if (failed)
            cancelAcquire(node);
    }
}
```



#### 释放

当一个线程调用 `releaseShard(int arg)` 方法释放共享资源时，会尝试使用 `tryReleaseShared()` 方法释放资源（设置状态变量 `state` 的值），然后调用 `LockSupport.unpark(thread)` 方法激活 AQS 队列里面被阻塞的一个线程（ thread ）。被激活的线程则使用  `tryAcquireShared()` 尝试，看当前状态变量 `state` 的值是否能满足自己的需要，满足则该线程被激活，然后继续向下执行，否则还是会被放入 AQS 队列并挂起

```java
public final void acquireShared(int arg) {
    if (tryAcquireShared(arg) < 0)
        doAcquireShared(arg);
}
```

```java
private void doReleaseShared() {
    // 自旋
    for (;;) {
        // 唤醒操作由头结点开始，注意这里的头节点已经是上面新设置的头结点了，其实就是唤醒上面新获取到共享锁的节点的后继节点
        Node h = head;
        if (h != null && h != tail) {
            int ws = h.waitStatus;
            // 如果当前节点是 SIGNAL 意味着，它正在等待一个信号，或者说，它在等待被唤醒
            if (ws == Node.SIGNAL) {
                // 重置 waitStatus 标志位
                if (!compareAndSetWaitStatus(h, Node.SIGNAL, 0))
                    continue;            // loop to recheck cases
                // 重置成功后,唤醒下一个节点
                unparkSuccessor(h);
            }
            // 如果本身头结点的 waitStatus 是出于重置状态（waitStatus==0）的
            else if (ws == 0 &&
                     // 将其设置为传播状态。意味着需要将状态向后一个节点传播
                     !compareAndSetWaitStatus(h, 0, Node.PROPAGATE))
                continue;                // loop on failed CAS
        }
        // 如果头结点没有发生变化，表示设置完成，退出循环
        // 如果头结点发生变化，比如说其他线程获取到了锁，为了使自己的唤醒动作可以传递，必须进行重试
        if (h == head)                   // loop if head changed
            break;
    }
}
```



#### 注意

与独占方式相同

```java

```

### isHeldExclusively()

基于 AQS 实现的锁除了需要重写上面介绍的方法外，还需要重写 `isHeldExclusively()` 方法，来判断锁时被当前线程独占还是共享



### Interruptibly

方法名带 `Interruptibly` 关键字的方法要对中断进行响应，获取资源时或者获取资源失败被挂起时，其他线程中断了该线程，那么该线程会抛出 `InterruptedException` 异常而返回

方法名不带 `Interruptibly` 关键字的方法被其他线程中断后，不会因为中断而抛出异常，它还是获取资源或者挂起



### acquireQueued()

在等待队列中排队拿号（中间没其它事干可以休息），直到拿到号后再返回

```java
final boolean acquireQueued(final Node node, int arg) {
    // 标记是否成功拿到资源
    boolean failed = true;
    try {
        // 标记等待过程中是否被中断过
        boolean interrupted = false;
        // 自旋
        for (;;) {
            // 拿到前驱节点
            final Node p = node.predecessor();
            // 如果前驱是 head，那么该节点便有资格去尝试获取资源（头节点释放完资源唤醒或者被 interrupt）
            if (p == head && tryAcquire(arg)) {
                // 前继出队，将头节点设置为自己
                setHead(node);
                // 之前头节点 head 已置为 null，此处再将 head.next 置为 null，就是为了方便 GC 回收以前的 head 结点。也就意味着之前拿完资源的结点出队了
                p.next = null; // help GC
                failed = false;
                // 返回等待过程中是否被中断过
                return interrupted;
            }
            // 如果自己可以休息了，就进入 waiting 状态，直到被 unpark()
            if (shouldParkAfterFailedAcquire(p, node) &&
                parkAndCheckInterrupt())
                //如果等待过程中被中断过，哪怕只有那么一次，就将 interrupted 标记为 true
                interrupted = true;
        }
    } finally {
        if (failed)
            cancelAcquire(node);
    }
}
```



### setHeadAndPropagate(Node, int)

两个入参，一个是当前成功获取共享锁的节点，一个就是 `tryAcquireShared()` 方法的返回值，它可能大于0也可能等于0

```java
private void setHeadAndPropagate(Node node, int propagate) {
    // 当前头节点
    Node h = head; 
    // 设置新的头节点，即把当前获取到锁的节点设置为头节点
    setHead(node);
    // propagate > 0 表示调用方指明了后继节点需要被唤醒
    // 头节点后面的节点需要被唤醒（waitStatus < 0），不论是之前的头结点还是新的头结点
    if (propagate > 0 || h == null || h.waitStatus < 0 ||
        (h = head) == null || h.waitStatus < 0) {
        Node s = node.next;
        if (s == null || s.isShared())
            // 如果当前节点的后继节点是共享类型或者没有后继节点，则进行唤醒
            // 这里可以理解为除非明确指明不需要唤醒（后继等待节点是独占类型），否则都要唤醒
            doReleaseShared();
    }
}
```



### shouldParkAfterFailedAcquire(Node, Node)

此方法主要用于检查状态，看看自己是否真的可以进入 waiting 状态

```java
private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
    int ws = pred.waitStatus;
    if (ws == Node.SIGNAL)
		// 如果前继的节点状态为 SIGNAL，表明当前节点需要 unpark，则返回成功，此时 acquireQueued 方法的 parkAndCheckInterrupt 将导致线程阻塞
        return true;
    if (ws > 0) {
        // 果前继节点状态为 CANCELLED，说明前置节点已经被放弃，则找到一个最近的非取消的前继节点，返回 false，acquireQueued 方法的无限循环将递归调用该方法，直至满足前继的节点状态为 SIGNAL，返回成功并导致线程阻塞
        do {
            node.prev = pred = pred.prev;
        } while (pred.waitStatus > 0);
        pred.next = node;
    } else {
        // 此时等待状态一定是 0 或者 PROPAGATE（释放共享资源时需要通知其它节点），则设置前继的状态为 SIGNAL，返回 false 后进入 acquireQueued 的无限循环，直至满足前继的节点状态为 SIGNAL，返回成功并导致线程阻塞
        compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
    }
    return false;
}
```



### parkAndCheckInterrupt()

如果线程找好安全休息点后，那就可以安心去休息了。此方法就是让线程去休息，真正进入等待状态

```java
private final boolean parkAndCheckInterrupt() {
    // 调用 park() 使线程进入 waiting 状态
    LockSupport.park(this);
    // 如果被唤醒，查看自己是不是被中断的
    return Thread.interrupted();
}
```
