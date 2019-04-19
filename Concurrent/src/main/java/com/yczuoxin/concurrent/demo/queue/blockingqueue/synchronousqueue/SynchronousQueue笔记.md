# SynchronousQueue笔记

## 概述

`SynchronousQueue` 是一个比较特殊的阻塞队列，它只有使用 `put()` 和 `take()` 方法在一个线程插入一个元素时会阻塞，必须等待另一个线程取走这个元素，反过来也是一样。其内部有一个 `Transfer` 抽象类作为核心数据结构，内部由支持公平队列 `TransferQueue` 和非公平队列 `TransferStack` 

```java
/** 为栈和队列提供公用 API */
abstract static class Transferer<E> {
    /** 操作 put 或者 take 方法 */
    abstract E transfer(E e, boolean timed, long nanos);
}

/** CPU 线程数 */
static final int NCPUS = Runtime.getRuntime().availableProcessors();

/** 在时间等待阻塞之前执行自旋操作次数，其不随 CPU 线程数增加而增加（经验） */
static final int maxTimedSpins = (NCPUS < 2) ? 0 : 32;

/** 没有时间等待的阻塞则会进行时间等待中自旋次数的 16 倍，因为他不需要跟等待时间做对比，速度更快 */
static final int maxUntimedSpins = maxTimedSpins * 16;

/** 自旋比用时间阻塞要快的纳秒数，这是个粗略值 */
static final long spinForTimeoutThreshold = 1000L;
```



## TransferStack 双向栈

### 字段

```java
/** 此时是取出一个值出队列 */
static final int REQUEST    = 0;
/** 此时是插入一个值入队列  */
static final int DATA       = 1;
/** 队列此时可以 take 值也可以 put 值 */
static final int FULFILLING = 2;
```



### 内部类 SNode

#### 字段

```java
volatile SNode next;        // next node in stack
volatile SNode match;       // the node matched to this to control park/unpark data; or null for REQUESTs
volatile Thread waiter;
Object item;				// 这两个字段不需要 volatile 关键字修饰因为它们总是在原子性操作前写，之后读
int mode;
```



### 方法

#### transfer(E e, boolean timed, long nanos)

三个基本操作算法

1. 如果当前栈为空或者包含与给定节点模式相同的节点，尝试将节点压入栈内，并等待一个匹配节点，最后返回匹配节点或者null（如果被取消）
2. 如果当前栈包含于给定节点模式互补的节点，尝试将这个节点打上 FULFILLING 标记，然后压入栈中，和相应的节点进行匹配，然后将两个节点（当前节点和互补节点）弹出栈，并返回匹配节点的数据。匹配和删除动作不是必须要做的，因为其他线程会执行 3
3. 如果栈顶已经存在一个 FULFILLING （正在满足其他节点）的节点，帮助这个节点完成匹配和移除（出栈）的操作。然后继续执行（主循环）。这部分代码基本和 2 的代码一样，只是不会返回节点的数据

```java
E transfer(E e, boolean timed, long nanos) {
    SNode s = null;
    // 根据传入的 e 值判断是 take(REQUEST) 操作还是 put(DATA) 操作
    int mode = (e == null) ? REQUEST : DATA;
    for (;;) {
        // 头节点赋值给 h
        SNode h = head;
        // 头节点是 null 或者模式与前一个节点操作相同
        if (h == null || h.mode == mode) {
            // 如果是计时操作并且已经超时
            if (timed && nanos <= 0) {      // can't wait
                // 如果栈顶节点存在但是被取消
                if (h != null && h.isCancelled())
                    // 将栈顶节点弹出
                    casHead(h, h.next);
                else
                    // 否则直接返回 null 让线程挂起
                    return null;
            // 构建新的节点 s，放到栈顶
            } else if (casHead(h, s = snode(s, e, h, mode))) {
                // 这里会阻塞，等到其他线程唤起或者被取消而唤起
                SNode m = awaitFulfill(s, timed, nanos);
                // 如果 m == s，则代表该节点已经被取消了
                if (m == s) {
                    // 弹出这个节点，然后返回 null
                    clean(s);
                    return null;
                }
                // 匹配完成，设置 s 为栈顶的后续节点
                if ((h = head) != null && h.next == s)
                    casHead(h, s.next);
                return (E) ((mode == REQUEST) ? m.item : s.item);
            }
        // 此时进入已经有了不同的模式并且另一个模式已经有节点在栈内
        // 判断此时栈顶元素是否已经匹配成功，不成功则进入逻辑，否则进入后面的 else
        } else if (!isFulfilling(h.mode)) {
            // 如果栈顶元素已经取消
            if (h.isCancelled())
                // 弹出此时栈顶元素并使下一个元素顶上，然后再进入循环
                casHead(h, h.next);0
            // 此时把 s 作为栈顶元素，把之前的栈顶元素 h 作为 s 的下一个节点（要去匹配的节点）
            else if (casHead(h, s=snode(s, e, h, FULFILLING|mode))) {
                for (;;) { // loop until matched or waiters disappear
                    // m 是 s 需要去匹配的元素
                    SNode m = s.next;       // m is s's match
                    // 如果此时 m 为 null，说明之前的栈顶元素已经被匹配弹出
                    if (m == null) {
                        // 把栈顶和 s 设 null，重新调用方法
                        casHead(s, null);
                        s = null;
                        break;
                    }
                    // 拿到原栈顶的下一个节点
                    SNode mn = m.next;
                    // 尝试去做匹配操作
                    if (m.tryMatch(s)) {
                        // 将匹配好的一组元素弹出，然后将原栈顶的下一个元素作为栈顶
                        casHead(s, mn);
                        // 返回原先 put 的元素
                        return (E) ((mode == REQUEST) ? m.item : s.item);
                    } else
                        // 匹配失败，直接删除 m 节点
                        s.casNext(m, mn);
                }
            }
        // 此时栈顶 h 正在匹配过程
        } else {
            // m 是 h 的匹配元素
            SNode m = h.next;
            // 如果 m 为 null，则说明其他元素已经将 m 匹配走了
            if (m == null)
                // 弹出 h 元素
                casHead(h, null);
            else {
                // 获取 m 的下一个元素
                SNode mn = m.next;
                // 协助匹配 m 和 h 元素
                if (m.tryMatch(h))
                    casHead(h, mn);
                // 匹配失败，直接删除 m 节点
                else 
                    h.casNext(m, mn);
            }
        }
    }
}
```

![描述图](http://wx1.sinaimg.cn/large/0060lm7Tly1g26x8qt19aj30cw0lft98.jpg)



#### awaitFulfill(SNode s, boolean timed, long nanos)

自旋或者阻塞此线程直到节点被取消或者匹配成功

```java
SNode awaitFulfill(SNode s, boolean timed, long nanos) {
    // 截点时间对应的毫秒值
    final long deadline = timed ? System.nanoTime() + nanos : 0L;
    // 获取当前线程
    Thread w = Thread.currentThread();
    // 如果 s 就是栈顶元素，或者栈顶元素正在匹配过程中，那么可以进行自旋
    int spins = (shouldSpin(s) ?
                 // 获取自旋次数
                 (timed ? maxTimedSpins : maxUntimedSpins) : 0);
    // 开始自旋
    for (;;) {
        // 查看线程中止状态
        if (w.isInterrupted())
            // 如果被中止，则取消当前操作
            // 把当前的 match 匹配从 null 变成 this，null 表示还未匹配成功，换成 this 则是 this 匹配自身，等于不再去匹配别的节点了
            s.tryCancel();
        // 获取 s 节点的匹配节点
        SNode m = s.match;
        // 判断是否匹配完成，或者取消
        if (m != null)
            // 匹配完成或者取消，则返回
            return m;
        // 是否设置超时
        if (timed) {
            nanos = deadline - System.nanoTime();
            // 如果超时了就取消掉
            if (nanos <= 0L) {
                s.tryCancel();
                continue;
            }
        }
        // 是否还需要自旋，并自旋次数 -1
        if (spins > 0)
            spins = shouldSpin(s) ? (spins-1) : 0;
        // 节点 s 是否有等待线程，如果没有，则设置为自己
        else if (s.waiter == null)
            s.waiter = w;
        // 如果没设置超时
        else if (!timed)
            // 挂起这个线程
            LockSupport.park(this);
        // 如果设置了超时
        else if (nanos > spinForTimeoutThreshold)
            // 挂起设置的时间
            LockSupport.parkNanos(this, nanos);
    }
}
```



#### tryMatch(SNode s)

尝试去匹配的方法

```java
boolean tryMatch(SNode s) {
    // 调用该方法的元素还没有匹配过
    if (match == null &&
        // 并且将 match 值切换为 s 成功
        UNSAFE.compareAndSwapObject(this, matchOffset, null, s)) {
        // 将获取阻塞的线程 w
        Thread w = waiter;
        // 如果线程不是 null(因为不能重复的唤醒)
        if (w != null) {
            // 把等待线程设置为 null
            waiter = null;
            // 唤起线程
            LockSupport.unpark(w);
        }
        // 返回成功
        return true;
    }
    // 如果被其他线程已经匹配过了，直接返回是不是匹配的自己
    return match == s;
}
```



#### clean(SNode s)

对于栈来说，我们可能需要 O(n) 的时间复杂度去遍历整个栈，然后确定节点可被移除，但这可以与访问栈的其他线程并行运行

```java
void clean(SNode s) {
    s.item = null;
    s.waiter = null；
    SNode past = s.next;
    if (past != null && past.isCancelled())
        past = past.next;
    // 找到有效 head
    SNode p;
    while ((p = head) != null && p != past && p.isCancelled())
        casHead(p, p.next);
    // 移除从原栈顶到 s 元素之间（包含 s）已取消节点的链接
    while (p != null && p != past) {
        SNode n = p.next;
        if (n != null && n.isCancelled())
            p.casNext(n, n.next);
        else
            p = n;
    }
}
```



