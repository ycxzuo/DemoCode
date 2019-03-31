# ReentrantReadWriteLock笔记

## 概述

为了满足读多写少的场景，`ReentrantReadWriteLock` 应运而生，采用读写分离的策略，允许多线程同时获取读锁，读写锁内部维护了一个 `ReadLock` 和一个 `WriteLock` ，他们依赖 `Sync` 实现具体功能，`Sync` 继承字 AQS，并且也提供了公平和非公平的实现，可以近似的理解为书无级别为 `Serializable` 串行化的数据库事务锁



## 字段含义

### state

AQS 中只维护了一个 `state` 值

* 用 state 高 16 表示读锁 `ReadLock`（共享锁），也就是获取到读锁的次数
* 用 state 低 16 表示写锁 `WriteLock`（可重入锁），也就是获取到写锁的线程可重入次数次数

### Thread  firstReader

记录第一个获取到**读锁**的线程，如果只有一个线程获取读锁，很明显，使用这样一个变量速度更快。

### int  firstReaderHoldCount

记录第一个获取到**读锁**的线程获取读锁的可重入次数，如果只有一个线程获取读锁，很明显，使用这样一个变量速度更快。

### HoldCounter  cachedHoldCount

记录**最后一个获取到读锁的线程获取读锁的可重入次数和该线程Id**，每当有新的线程获取到读锁，这个变量都会更新。这个变量的目的是，当最后一个获取读锁的线程重复获取读锁，或者释放读锁，就会直接使用这个变量，速度更快，相当于缓存。

### ThreadLocalHoldCounter   readHolds

保存当前线程重入**读锁**的次数的容器。在读锁重入次数为 0 时移除，`ThreadLocalHoldCounter` 继承自 `ThreadLocal` ，并重写了 `initialValue` 方法来返回一个 `HoldCounter` 对象



## 读锁 ReadLock

获取锁的过程：

1. 当线程调用 `acquireShared()` 申请获取锁资源时，如果成功，则进入临界区。
2. 当获取锁失败时，则创建一个共享类型的节点并进入一个 FIFO 等待队列，然后被挂起等待唤醒。
3. 当队列中的等待线程被唤醒以后就重新尝试获取锁资源，如果成功则唤醒后面还在等待的共享节点并把该唤醒事件传递下去，即会依次唤醒在该节点后面的所有共享节点，然后进入临界区，否则继续挂起等待。

释放锁过程：

1. 当线程调用 `releaseShared()` 进行锁资源释放时，如果释放成功，则唤醒队列中等待的节点，如果有的话。



### lock()



![读锁步骤](<http://wx4.sinaimg.cn/large/0060lm7Tly1g1ikewo5g5j30hr0lsabg.jpg>)

获取读锁，如果当前没有其他线程持有写锁，则当前线程可以获取读锁，AQS 的状态值 `state` 的高 16 位的值会 +1，然后方法返回。否则如果其他一个线程持有写锁，则当前线程会被阻塞

```java
protected final int tryAcquireShared(int unused) {	
    // 当前线程
	Thread current = Thread.currentThread();
    int c = getState();
	// 判断写锁是否被占用
    if (exclusiveCount(c) != 0 &&
        getExclusiveOwnerThread() != current)
        // 写锁已经被其他线程占用
        return -1;
    // 获取读锁的次数
    int r = sharedCount(c);
    // 尝试获取读锁，多个线程只有一个会成功，不成功的进入 fullTryAcquireShared()进行重试
    // 这个方法要根据公平锁还是非公平锁去分析，后面讲解
    if (!readerShouldBlock() &&
        // 读锁进入次数小于最大次数
        r < MAX_COUNT &&
        // CAS 给 state 的高 16 位 +1
        compareAndSetState(c, c + SHARED_UNIT)) {
        // 如果第一个线程获取读锁
        if (r == 0) {
            // 将当前线程设置为第一个读锁线程
            firstReader = current;
            // 给次数赋值为 1
            firstReaderHoldCount = 1;
        // 如果当前线程是第一个获取读锁的线程
        } else if (firstReader == current) {
            // 获取锁次数 +1
            firstReaderHoldCount++;
        // 
        } else {
            // 获取最后一个获取锁的线程信息或记录其他线程读锁的可重入数
            HoldCounter rh = cachedHoldCounter;
            // 
            if (rh == null || rh.tid != getThreadId(current))
                cachedHoldCounter = rh = readHolds.get();
            else if (rh.count == 0)
                readHolds.set(rh);
            rh.count++;
        }
        return 1;
    }
    // 如果 CAS 设置失败，或者队列有等待的线程（公平情况下），死循环获取读锁。包含锁降级策略。
    return fullTryAcquireShared(current);
}
```

**非公平锁**的 `readerShouldBlock()` 如下

```java
final boolean readerShouldBlock() {
    return apparentlyFirstQueuedIsExclusive();
}
```

调用的 AQS 中的 `apparentlyFirstQueuedIsExclusive()` 方法

```java
final boolean apparentlyFirstQueuedIsExclusive() {
        Node h, s;
        return (h = head) != null &&
            (s = h.next)  != null &&
            !s.isShared()         &&
            s.thread != null;
}
```

如果头节点不为空，并头节点的下一个节点不为空，并且不是读锁，而是写锁、并且线程不为空，则返回 true ，因为不能让写锁一直等待读锁，这里实际上是一个优先级，如果队列中头部元素是写锁，那么读锁需要等待，避免写锁饥饿

**公平锁**的 `readerShouldBlock()` 如下

```java
final boolean readerShouldBlock() {
    return hasQueuedPredecessors();
}
```

调用的 AQS 的 `hasQueuedPredecessors()` 方法

```java
public final boolean hasQueuedPredecessors() {
    Node t = tail;
    Node h = head;
    Node s;
    return h != t &&
        ((s = h.next) == null || s.thread != Thread.currentThread());
}
```

若锁没有被任何线程锁拥有，则判断当前线程是不是队列（链表）中的第一个线程线程

* true，表示有其他线程先于当前线程等待获取锁，此时为了实现公平，保证等待时间最长的线程先获取到锁，不能执行 CAS ，CAS 可能会破坏公平性
* false，则相反，可以执行 CAS 更新同步状态尝试获取锁

```java
final int fullTryAcquireShared(Thread current) {
    HoldCounter rh = null;
    for (;;) {
        int c = getState();
        // 如果有其他线程获取了写锁，直接返回 -1
        if (exclusiveCount(c) != 0) {
            if (getExclusiveOwnerThread() != current)
                return -1;
        // 监测当前是否应该需要进入等待队列
        // 就算 readerShouldBlock 方法返回 true, 也不能返回 -1
        // 可能因为当前是公平模式或者队列的第一个等待线程正在等待写锁
        // 返回 -1 意味着当前线程将要进入等待队列
        // 如果当前线程正在持有读锁，且这次读锁的重入被放入等待队列，万一之前队列中有线程正在等待写锁，将会导致死锁
        // 另一种情况是当前线程正在持有写锁，且这次读锁的“降级申请”被放入等待队列，如果队列中之前有线程正在等待锁，不论等待的是写锁还是读锁，都将导致死锁
        } else if (readerShouldBlock()) {
            // 保证当前线程不是第二次获取读锁
            if (firstReader == current) {
      		// 如果不是当前线程
            } else {
                if (rh == null) {
                    rh = cachedHoldCounter;
                    if (rh == null || rh.tid != getThreadId(current)) {
                        // 从 ThreadLocal 中取出计数器
                        rh = readHolds.get();
                        // 如果该线程持有读锁的次数已经为 0
                        if (rh.count == 0)
                            readHolds.remove();
                    }
                }
                // 说明是上述刚初始化的 rh，所以直接去 AQS 中排队
                if (rh.count == 0)
                    return -1;
            }
        }
        // 如果读锁次数达到 65535 ，抛出异常
        if (sharedCount(c) == MAX_COUNT)
            throw new Error("Maximum lock count exceeded");
        // 尝试对 state 加 65536, 也就是设置读锁，实际就是对高 16 位 +1
        if (compareAndSetState(c, c + SHARED_UNIT)) {
            // 如果读锁是空闲的
            if (sharedCount(c) == 0) {
                // 设置第一个读锁
                firstReader = current;
                // 计数器为 1
                firstReaderHoldCount = 1;
            // 如果不是空闲的，查看第一个线程是否是当前线程
            } else if (firstReader == current) {
                // 更新计数器
                firstReaderHoldCount++;
            // 如果不是当前线程
            } else {
                if (rh == null)
                    rh = cachedHoldCounter;
                // 如果最后一个读计数器所属线程不是当前线程
                if (rh == null || rh.tid != getThreadId(current))
                     // 自己创建一个
                    rh = readHolds.get();
                else if (rh.count == 0)
                    readHolds.set(rh);
                // 对计数器 +1
                rh.count++;
                // 更新缓存计数器
                cachedHoldCounter = rh; // cache for release
            }
            return 1;
        }
    }
}
```

### 锁降级

#### 代码

```java
if (exclusiveCount(c) != 0) {
    if (getExclusiveOwnerThread() != current)
        return -1;
```

#### 概念


> > >	JDK定义
> > >	重入还允许从写入锁降级为读取锁，其实现方式是：先获取写入锁，然后获取读取锁，最后释放写入锁。但是，从读取锁升级到写入锁是不可能的

锁降级指的是写锁降级为读锁。如果当前线程拥有写锁，然后将其释放，最后再获取读锁，这种并不能称之为锁降级，锁降级指的是把持住（当前拥有的）写锁，再获取到读锁，随后释放（先前有用的）写锁的过程

#### 作用

锁降级中，读锁的获取的**目的是为了保证数据的可见性**。如果当前线程不获取读锁而是直接释放写锁，假设此时有另一个线程 T 获取了写锁并修改了数据，那么当前线程无法感知线程 T 的数据更新，如果当前线程获取读锁，则线程 T 会被阻塞，直到当前线程使用数据并释放读锁之后，线程 T 才能获取写锁进行数据更新。

### tryLock()

其调用的其实还是尝试获取写锁，代码与获取锁类似

* 如果有线程获取了写锁并且获取写锁的线程不是当前线程，返回 false
* 尝试 CAS 将 state 的高 16 位 +1
  * 失败则重新进入该方法
  * 成功
    * 第一次进入则将第一个获取读锁的线程设置为该线程，并将第一个获取读锁次数 +1
    * 如果进入的线程就是第一个获取读锁的线程，将第一个获取读锁次数 +1
    * 如果不是第一个获取读锁的线程，则将最后一个获取读锁的信息更新

### unlock()

共享的状态是可以被共享的，也就是意味着其他 AQS 队列中的其他节点也应能第一时间知道状态的变化

```java
public void lock() {
    sync.acquireShared(1);
}
```



## 写锁 WriteLock

### lock()

写锁是一个重入锁，如果已经获取了锁，再次获取只是简单的吧重入次数 +1 然后直接返回

```java
protected final boolean tryAcquire(int acquires) {
	Thread current = Thread.currentThread();
    int c = getState();
    int w = exclusiveCount(c);
    // 如果 c ！= 0，说明读锁或者写锁已经被某线程获取
    if (c != 0) {
        // 如果 w == 0，说明没有获取过写锁，那么就说明有线程已经获取到读锁
        // 如果当前线程不是写锁的拥有者
        // 返回 false
        if (w == 0 || current != getExclusiveOwnerThread())
            return false;
        // 该线程拥有写锁，判断可重入次数
        if (w + exclusiveCount(acquires) > MAX_COUNT)
            throw new Error("Maximum lock count exceeded");
        // 设置可重入次数 +1
        setState(c + acquires);
        return true;
    }
    // 如果还没有线程获取过读锁或者写锁
    // writerShouldBlock
    // 非公平锁永远返回 false，只要 CAS 抢占成功就返回 true
    // 公平锁会判断是否有其他线程先进入等待队列，如果有则放弃获取写锁权限
    if (writerShouldBlock() ||
        !compareAndSetState(c, c + acquires))
        return false;
    setExclusiveOwnerThread(current);
    return true;
}
```

### tryLock()

其调用的其实还是尝试获取写锁，代码与获取锁类似

* 如果当前线程获取写锁成功，则返回 true
* 如果已经有其他线程持有了写锁或者读锁，该方法直接返回 false，并且不会阻塞
* 如果当前线程已经获取了锁，则简单的 CAS 操作 +1 后直接返回 true

### unLock()

尝试释放锁，如果当前线程持有该锁，调用该方法让现场对该线程持有的 AQS 状态值 -1，如果 -1 后当前状态值为 0，则当前线程会释放锁，否则仅仅减一而已

```java
protected final boolean tryRelease(int releases) {
    // 是否当前线程获取独占锁
    if (!isHeldExclusively())
        throw new IllegalMonitorStateException();
    int nextc = getState() - releases;
    // 如果写锁次数已经为 0，这里不考虑读锁，因为获取写锁时，读锁肯定为 0
    boolean free = exclusiveCount(nextc) == 0;
    // 如果写锁的重入值为 0
    if (free)
        // 释放锁
        setExclusiveOwnerThread(null);
    // 更改状态值
    setState(nextc);
    return free;
}
```

