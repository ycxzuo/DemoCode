# ConcurrentLinkedQueue笔记

## 概念

`ConcurrentLinkedQueue` 是线程安全的无边界非阻塞队列，其底层是单向链表



## 源码

### 内部类 Node

其中有两个被 `volutile` 修饰属性

* `item` -> 节点内元素
* `next` -> 链表结构指向的下个节点

方法

* ```java
  boolean casItem(E cmp, E val)
  ```

  利用 CAS 操作将 `item` 的偏移量换为 val 值

* ```java
  void lazySetNext(Node<E> val)
  ```

  

* ```java
  boolean casNext(Node<E> cmp, Node<E> val)
  ```

  利用 CAS 操作切换下个节点 `next` 的偏移量换为 val 值



## 底层

### offer(E e)

在队列尾部插入元素 e，由于 `ConcurrentLinkedQueue ` 是无边界队列，所以该方法不会返回 false

```java
public boolean offer(E e) {
    checkNotNull(e);
    final Node<E> newNode = new Node<E>(e);
	
    for (Node<E> t = tail, p = t;;) {
        Node<E> q = p.next;
        if (q == null) {
            // p 是最后一个节点
            if (p.casNext(null, newNode)) {
                // Successful CAS is the linearization point
                // for e to become an element of this queue,
                // and for newNode to become "live".
                if (p != t) // hop two nodes at a time
                    casTail(t, newNode);  // Failure is OK.
                return true;
            }
            // Lost CAS race to another thread; re-read next
        }
        else if (p == q)
            // We have fallen off list.  If tail is unchanged, it
            // will also be off-list, in which case we need to
            // jump to head, from which all live nodes are always
            // reachable.  Else the new tail is a better bet.
            p = (t != (t = tail)) ? t : head;
        else
            // Check for tail updates after two hops.
            p = (p != t && t != (t = tail)) ? t : q;
    }
}
```



