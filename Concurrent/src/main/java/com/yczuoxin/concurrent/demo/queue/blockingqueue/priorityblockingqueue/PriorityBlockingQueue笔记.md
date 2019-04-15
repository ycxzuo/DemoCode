# PriorityBlockingQueue笔记

## 概述

带优先级的阻塞队列，其底层是一个数组，该队列会自动扩容，默认会使用对象的 `compareTo` 方法进行比较，也可以自定义 `comparator`

```java
    /** Default array capacity. */
    private static final int DEFAULT_INITIAL_CAPACITY = 11;

    /** The maximum size of array to allocate. Some VMs reserve some header words in an array. */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /**
     * Priority queue represented as a balanced binary heap: the two
     * children of queue[n] are queue[2*n+1] and queue[2*(n+1)].  The
     * priority queue is ordered by comparator, or by the elements'
     * natural ordering, if comparator is null: For each node n in the
     * heap and each descendant d of n, n <= d.  The element with the
     * lowest value is in queue[0], assuming the queue is nonempty.
     */
    private transient Object[] queue;

    /** The number of elements in the priority queue. */
    private transient int size;

    /** The comparator, or null if priority queue uses elements' natural ordering. */
    private transient Comparator<? super E> comparator;

    /** Lock used for all public operations */
    private final ReentrantLock lock;

    /** Condition for blocking when empty */
    private final Condition notEmpty;

    /** Spinlock for allocation, acquired via CAS.自旋锁保证只有一个线程进行扩容 0：没有进行扩容   1：扩容已经开始*/
    private transient volatile int allocationSpinLock;

    /**
     * A plain PriorityQueue used only for serialization,
     * to maintain compatibility with previous versions
     * of this class. Non-null only during serialization/deserialization.
     */
    private PriorityQueue<E> q;
```



## 方法分析

### 构造函数

使用默认的构造参数，会创建一个容量为 11，使用元素的 `compareTo` 方法

```java
public PriorityBlockingQueue() {
    this(DEFAULT_INITIAL_CAPACITY, null);
}
```

```java
public PriorityBlockingQueue(int initialCapacity) {
    this(initialCapacity, null);
}
```

```java
public PriorityBlockingQueue(int initialCapacity,
                             Comparator<? super E> comparator) {
    if (initialCapacity < 1)
        throw new IllegalArgumentException();
    this.lock = new ReentrantLock();
    this.notEmpty = lock.newCondition();
    this.comparator = comparator;
    this.queue = new Object[initialCapacity];
}
```



### tryGrow(Object[] array, int oldCap)

该方法是 `PriorityBlockingQueue` 的扩容方法

```java
private void tryGrow(Object[] array, int oldCap) {
    // 先释放锁，这里不释放锁也是没有问题的，但是扩容时间是比较耗时的，如果不释放锁，其他线程就会阻塞，降低并发性能，所以使用 CAS 保证只有一个线程扩容成功
    lock.unlock();
    Object[] newArray = null;
    // 还没有开始扩容
    if (allocationSpinLock == 0 &&
        // CAS 操作将 allocationSpinLock 的值变为 1
        UNSAFE.compareAndSwapInt(this, allocationSpinLockOffset,
                                 0, 1)) {
        try {
            // 扩容方案，如果阈值小于 64，则 n*2+2 的扩容（这样扩容比较快），如果大于等于 64，则扩容为之前的两倍（右移一位）
            int newCap = oldCap + ((oldCap < 64) ?
                                   (oldCap + 2) : // grow faster if small
                                   (oldCap >> 1));
            // 如果新的阈值已经大于阈值最大值
            if (newCap - MAX_ARRAY_SIZE > 0) {    // possible overflow
                // 最小扩容阈值为旧的阈值 +1
                int minCap = oldCap + 1;
                // 如果最小扩容阈值已经超过了 int 的取值范围或者大于了阈值最大值，直接抛出 OOM 错误
                if (minCap < 0 || minCap > MAX_ARRAY_SIZE)
                    throw new OutOfMemoryError();
                // 新阈值直接给与最大阈值
                newCap = MAX_ARRAY_SIZE;
            }
            // 如果新阈值
            if (newCap > oldCap && queue == array)
                newArray = new Object[newCap];
        } finally {
            // 无论扩容成功或者失败，都会将 allocationSpinLock 的值变回 0
            allocationSpinLock = 0;
        }
    }
    // 如果 CAS 操作失败，则让出 CPU使用权让扩容线程先获取锁，如果先获取了锁，会走到调用该方法外部循环重新释放锁
    if (newArray == null)
        Thread.yield();
    lock.lock();
    // 如果扩容成功，并且原队列等于传入的队列（即没有被修改过）
    if (newArray != null && queue == array) {
        // 将新的队列指向 queue 队列扩容
        queue = newArray;
        // 将之前的数组复制到新数组
        System.arraycopy(array, 0, newArray, 0, oldCap);
    }
}
```



### siftUpComparable(int k, T x, Object[] array)

利用元素的 compareTo 方法进行排序，排序方式是将其顺序排成一个二叉树方式

```java
private static <T> void siftUpComparable(int k, T x, Object[] array) {
    Comparable<? super T> key = (Comparable<? super T>) x;
    // 如果队列长度大于 0，则进行排序，否则直接插入
    while (k > 0) {
        // 相当于找到二叉树堆的根节点（最小值节点）
        int parent = (k - 1) >>> 1;
        Object e = array[parent];
        if (key.compareTo((T) e) >= 0)
            break;
        array[k] = e;
        k = parent;
    }
    array[k] = key;
}
```

![图示](http://wx4.sinaimg.cn/large/0060lm7Tly1g23h7sanlwj30by0k4754.jpg)



### offer(E e)

插入一个非 null 元素，永远返回 true，`put(E e)` 和 `add(E e)` 都是调用的 `offer(E e) 方法`

```java
public boolean offer(E e) {
    if (e == null)
        throw new NullPointerException();
    final ReentrantLock lock = this.lock;
    lock.lock();
    int n, cap;
    Object[] array;
    // 自旋操作，如果原队列和新队列长度相同
    while ((n = size) >= (cap = (array = queue).length))
        tryGrow(array, cap);
    try {
        Comparator<? super E> cmp = comparator;
        // 如果没有传入比较方法
        if (cmp == null)
            // 使用元素的比较方法进行二叉树堆排序
            siftUpComparable(n, e, array);
        else
            // 使用传入的比较方法进行二叉树堆排序
            siftUpUsingComparator(n, e, array, cmp);
        size = n + 1;
        notEmpty.signal();
    } finally {
        lock.unlock();
    }
    return true;
}
```



### siftDownComparable(int k, T x, Object[] array, int n)



```java
private static <T> void siftDownComparable(int k, T x, Object[] array, int n) {
    if (n > 0) {
        Comparable<? super T> key = (Comparable<? super T>)x;
        int half = n >>> 1;           // loop while a non-leaf
        while (k < half) {
            int child = (k << 1) + 1; // assume left child is least
            Object c = array[child];
            int right = child + 1;
            if (right < n &&
                ((Comparable<? super T>) c).compareTo((T) array[right]) > 0)
                c = array[child = right];
            if (key.compareTo((T) c) <= 0)
                break;
            array[k] = c;
            k = child;
        }
        array[k] = key;
    }
}
```





### poll()

弹出最小的元素（二叉树堆的根节点），没有节点就返回 null

```java
public E poll() {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        // 返回最小的元素
        return dequeue();
    } finally {
        lock.unlock();
    }
}
```



### dequeue()

弹出最小的元素（二叉树堆的根节点），没有节点就返回 null

```java
private E dequeue() {
    // 先算出弹出后的队列长度
    int n = size - 1;
    // 如果 n < 0，表示之前队列是空的，直接返回 null
    if (n < 0)
        return null;
    else {
        // 获取队列数组的副本
        Object[] array = queue;
        // 取出头部节点
        E result = (E) array[0];
        // 取出队尾
        E x = (E) array[n];
        // 将队尾元素设置为 null
        array[n] = null;
        Comparator<? super E> cmp = comparator;
        if (cmp == null)
            siftDownComparable(0, x, array, n);
        else
            siftDownUsingComparator(0, x, array, n, cmp);
        size = n;
        return result;
    }
}
```

