# LinkedBlockingQueue笔记

## 概述

`LinkedBlockingQueue` 是一个利用独占锁（`ReentrantLock`）实现的阻塞队列，其底层是一个单向链表（静态内部类 `Node`），默认构造器中，其队列的长度为 int 的最大值，如果队列满了，后续插入节点阻塞，并视情况是否丢弃，该队列有两个独占锁，对头节点操作使用一个独占锁，对尾节点操作使用一个独占锁，来存放被阻塞的线程

```java
/** Current number of elements */
private final AtomicInteger count = new AtomicInteger();

/** Lock held by take, poll, etc */
private final ReentrantLock takeLock = new ReentrantLock();

/** Wait queue for waiting takes */
private final Condition notEmpty = takeLock.newCondition();

/** Lock held by put, offer, etc */
private final ReentrantLock putLock = new ReentrantLock();

/** Wait queue for waiting puts */
private final Condition notFull = putLock.newCondition();
```



## 方法解析

### put(E e)

在队列的尾部插入一个节点，如果队列未满，直接插入；如果队列已满，则阻塞当前线程直至队列有空间，如果等待期间被其他线程设置了中断标志，则抛出异常并返回，**该方法无返回值**

```java
public void put(E e) throws InterruptedException {
    // 队列中不允许插入 null
    if (e == null) throw new NullPointerException();
    // 方法中一般都是预先初始化值为负数
    int c = -1;
    Node<E> node = new Node<E>(e);
    final ReentrantLock putLock = this.putLock;
    final AtomicInteger count = this.count;
    // 中断后会抛异常返回
    putLock.lockInterruptibly();
    try {
        // 如果队列已经满了，就放入 notFull 等待队列中阻塞挂起，等待有空间再唤醒
        while (count.get() == capacity) {
            notFull.await();
        }
        // 将该节点设置为尾节点
        enqueue(node);
        // 获取之前队列空间的节点数量并将其原子性的 +1
        c = count.getAndIncrement();
        // 如果插入后还有空间，则唤醒其他插入阻塞的线程
        if (c + 1 < capacity)
            notFull.signal();
    } finally {
        putLock.unlock();
    }
    // 如果之前队列没有节点，则唤醒读取线程
    if (c == 0)
        // 唤起被 notEmpty 阻塞的线程
        signalNotEmpty();
}
```



### offer(E e)

与 `put(E e)` 方法类似，队列有空间时，简单加入到队列尾部，并返回 true；**只是在队列满的时候，直接丢弃该节点**，并返回 false

```java
public boolean offer(E e) {
    if (e == null) throw new NullPointerException();
    final AtomicInteger count = this.count;
    // 队列满了的话直接返回 false
    if (count.get() == capacity)
        return false;
    int c = -1;
    Node<E> node = new Node<E>(e);
    final ReentrantLock putLock = this.putLock;
    putLock.lock();
    try {
        // 再次确认队列是否是满的，避免在获取锁之前有线程已经插入一个节点
        if (count.get() < capacity) {
            enqueue(node);
            c = count.getAndIncrement();
            if (c + 1 < capacity)
                notFull.signal();
        }
    } finally {
        putLock.unlock();
    }
    if (c == 0)
        signalNotEmpty();
    return c >= 0;
}
```



### offer(E e, long timeout, TimeUnit unit)

与 `offer(E e)` 方法类似，只是如果队列满了不是立即丢弃该节点，而是**等待一段时间**后如果队列还是满的就直接丢弃，传入 <= 0 的值时，与不带时间参数的方法一直

```java
public boolean offer(E e, long timeout, TimeUnit unit)
    throws InterruptedException {
    if (e == null) throw new NullPointerException();
    // 将时间切换为纳秒单位
    long nanos = unit.toNanos(timeout);
    int c = -1;
    final ReentrantLock putLock = this.putLock;
    final AtomicInteger count = this.count;
    putLock.lockInterruptibly();
    try {
        while (count.get() == capacity) {
            // 如果纳秒数为小于等于 0 的值，就直接丢弃
            if (nanos <= 0)
                return false;
            nanos = notFull.awaitNanos(nanos);
        }
        enqueue(new Node<E>(e));
        c = count.getAndIncrement();
        if (c + 1 < capacity)
            notFull.signal();
    } finally {
        putLock.unlock();
    }
    if (c == 0)
        signalNotEmpty();
    return true;
}
```



### take()

取出队列中第一个节点，如果队列是空的，则阻塞挂起，等待节点入队，如果等待被唤起过程中被中止了，并抛出异常

```java
public E take() throws InterruptedException {
    E x;
    int c = -1;
    final AtomicInteger count = this.count;
    final ReentrantLock takeLock = this.takeLock;
    takeLock.lockInterruptibly();
    try {
        while (count.get() == 0) {
            notEmpty.await();
        }
        x = dequeue();
        c = count.getAndDecrement();
        if (c > 1)
            notEmpty.signal();
    } finally {
        takeLock.unlock();
    }
    if (c == capacity)
        signalNotFull();
    return x;
}
```



### poll()

取出队列中第一个节点，如果队列是空的，则直接返回 false

```java
public E poll() {
    final AtomicInteger count = this.count;
    if (count.get() == 0)
        return null;
    E x = null;
    int c = -1;
    final ReentrantLock takeLock = this.takeLock;
    takeLock.lock();
    try {
        if (count.get() > 0) {
            x = dequeue();
            c = count.getAndDecrement();
            if (c > 1)
                notEmpty.signal();
        }
    } finally {
        takeLock.unlock();
    }
    if (c == capacity)
        signalNotFull();
    return x;
}
```



### poll(long timeout, TimeUnit unit)

与 `poll()` 方法类似，只是如果队列是空不直接返回 null ，而是**等待一段时间**后如果队列还是空的就直接返回 null，传入 <= 0 的值时，与不带时间参数的方法一直



### peek()

获取头部节点，但是不移除它，如果队列是空的，则直接返回 null，此方法不会被阻塞

```java
public E peek() {
    // 队列是空，直接返回 null
    if (count.get() == 0)
        return null;
    final ReentrantLock takeLock = this.takeLock;
    takeLock.lock();
    try {
        // 获取头部节点，head 是哨兵节点
        Node<E> first = head.next;
        // 避免在拿到锁时，队列成空，造成空指针异常
        if (first == null)
            return null;
        else
            // 返回头部节点的值
            return first.item;
    } finally {
        takeLock.unlock();
    }
}
```



### remove(Object o)

移除一个指定的元素，如果有该节点，则删除该节点并返回 true，否则返回 false

```java
public boolean remove(Object o) {
    // 不能插入 null 值，所以也不会存在 null
    if (o == null) return false;
    // 双重锁加锁
    fullyLock();
    try {
        // 遍历队列直至找到该值
        for (Node<E> trail = head, p = trail.next;
             p != null;
             trail = p, p = p.next) {
            if (o.equals(p.item)) {
                // 从队列中移除
                unlink(p, trail);
                return true;
            }
        }
        return false;
    } finally {
        // 释放双重锁，并且顺序与加锁顺序相反
        fullyUnlock();
    }
}
```

