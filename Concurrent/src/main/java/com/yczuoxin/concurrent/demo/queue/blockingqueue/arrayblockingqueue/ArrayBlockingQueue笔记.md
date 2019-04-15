# ArrayBlockingQueue笔记

## 概述

`ArrayBlockingQueue` 是一个利用独占锁（`ReentrantLock`）实现的阻塞队列，其底层是一个有界数组（Array），由于移动数组的位置代价过大，其利用 `takeIndex` 的移动来保证查找头节是理论上数组的队列头部，利用 `putIndex` 保证插入的节点位置理论上是数组队列队尾。此队列用了一个全局的独占锁，粒度大，性能差

```java
/** The queued items */
final Object[] items;

/** items index for next take, poll, peek or remove */
int takeIndex;

/** items index for next put, offer, or add */
int putIndex;

/** Number of elements in the queue */
int count;

/** Main lock guarding all access */
final ReentrantLock lock;

/** Condition for waiting takes */
private final Condition notEmpty;

/** Condition for waiting puts */
private final Condition notFull;
```



## 方法分析

### ArrayBlockingQueue(int capacity)

构造函数初始化数组的大小，因为 `ArrayBlockingQueue` 是一个有界队列



### ArrayBlockingQueue(int capacity,  boolean fair)

构造函数初始化数组的大小，并且独占锁是公平还是非公平的，默认是非公平的



### offer(E e)

插入指定元素在队列的尾部，如果插入成功，返回 true，如果失败，返回 false

```java
public boolean offer(E e) {
    // 非空判定
    checkNotNull(e);
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        // 判断是否队列已满
        if (count == items.length)
            // 满了就返回 false
            return false;
        else {
            // 没满就把该元素放入队列尾部并返回 false
            enqueue(e);
            return true;
        }
    } finally {
        lock.unlock();
    }
}
```



### enqueue(E x)

插入节点，`ArrayBlockingQueue` 是使用 `putIndex` 的移动来找到插入的位置

```java
private void enqueue(E x) {
    // 拿到队列并用副本保存
    final Object[] items = this.items;
    // 将下标为 putIndex 的数组放入 x 值
    items[putIndex] = x;
    // 先将 putIndex 自增，即下一个插入节点的下标
    // 然后判断插入后是不是到了队列尾端了，即 ++putIndex == items.length
    if (++putIndex == items.length)
        // 满了就将 putIndex 赋值为 0，重头开始插入节点
        putIndex = 0;
    // 队列中个数 +1
    count++;
    notEmpty.signal();
}
```



### add(E e)

其调用的是父类 `AbstractQueue` 的 `add(E e)` 方法，底层调用的是 `offer(E e)` 方法，要么插入成功返回 true，要么抛出异常，不会反回 false



### put(E e)

插入一个节点在队列尾部，如果队列已满，则阻塞挂起，等待队列有空间，如果在阻塞期间有其他线程中止了该线程，就抛出异常

```java
public void put(E e) throws InterruptedException {
    checkNotNull(e);
    final ReentrantLock lock = this.lock;
    lock.lockInterruptibly();
    try {
        // 如果队列已满就阻塞
        while (count == items.length)
            notFull.await();
        enqueue(e);
    } finally {
        lock.unlock();
    }
}
```



### poll()

取出头部节点，如果没有，就返回 null

```java
public E poll() {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        return (count == 0) ? null : dequeue();
    } finally {
        lock.unlock();
    }
}
```



### dequeue()

移除需要移除的节点，`ArrayBlockingQueue` 是使用 `takeIndex` 的移动来保证需要删除节点的位置

```java
private E dequeue() {
    // 拿到数组副本
    final Object[] items = this.items;
    // 拿到需要移除的节点下标
    E x = (E) items[takeIndex];
    items[takeIndex] = null;
    if (++takeIndex == items.length)
        takeIndex = 0;
    count--;
    if (itrs != null)
        itrs.elementDequeued();
    notFull.signal();
    return x;
}
```



### tale()

取出头部节点，如果没有节点，则阻塞挂起等待插入节点并唤起线程，如果在等待唤醒时被中止等待，则抛出异常

```java
public E take() throws InterruptedException {
    final ReentrantLock lock = this.lock;
    lock.lockInterruptibly();
    try {
        while (count == 0)
            notEmpty.await();
        return dequeue();
    } finally {
        lock.unlock();
    }
}
```



### peek()

找到头部节点，如果没有节点，则返回 null

```java
public E peek() {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        // 直接拿到对应角标的值
        return itemAt(takeIndex); // null when queue is empty
    } finally {
        lock.unlock();
    }
}
```

