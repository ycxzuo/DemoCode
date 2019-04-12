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
        // 如果 q == null，p 是最后一个节点
        if (q == null) {
            // CAS 操作将 p 的下一个节点由 null 切换为 新插入的节点，如果失败，说明是其他线程同时进入将节点插入到了尾节点上，重新进入该方法
            if (p.casNext(null, newNode)) {
                // 判断是否是第一次进这个方法，即当前节点 p 是否已经被更改过了
                if (p != t) 
                    // 切换 tail 节点的位置
                    casTail(t, newNode);  
                return true;
            }
        }
        // 此时说明 p 节点已经被删除
        else if (p == q)
            // 重新找哨兵
            p = (t != (t = tail)) ? t : head;
        else
            // 找到最后一节点赋值给 p
            p = (p != t && t != (t = tail)) ? t : q;
    }
}
```

tail 节点不一定是最后一个节点，它会跳跃着更新，做多会滞留4个节点



### poll()

弹出队列首节点的值

```java
public E poll() {
    restartFromHead:
    // 外层循环，无限循环，用于如果并发问题首节点已经被弹出
    for (;;) {
        // 内层循环，用于找到并删除首节点
        for (Node<E> h = head, p = h, q;;) {
            // 找到当前时间的首节点
            E item = p.item;
			// 如果当前节点的值不为空，并且 CAS 操作成功
            if (item != null && p.casItem(item, null)) {
                // 重新找到首节点
                if (p != h)
                    updateHead(h, ((q = p.next) != null) ? q : p);
                return item;
            }
            // 如果是空队列
            else if ((q = p.next) == null) {
                updateHead(h, p);
                // 直接返回 null
                return null;
            }
            // 如果当前节点已经被删除
            else if (p == q)
                // 重新进入内层循环
                continue restartFromHead;
            else
                // 开始遍历下一个节点
                p = q;
        }
    }
}
```



### peek()

返回首节点的值，跟 poll() 方法相比少了移除操作



### first()

返回首节点，跟 peek() 方法相比返回的是一个节点而不是值



### updateHead(Node, Node)

在更新头节点的时候原来的head就会自己指向自己，成为哨兵

```java
final void updateHead(Node<E> h, Node<E> p) {
    // 如果 h != p 就把 p 设置为 head 节点 
    if (h != p && casHead(h, p))
        // 通过 CAS 函数设置 h 的下一个节点为 h 自身，该设置可能会延迟执行
        h.lazySetNext(h);
}
```



## 学习文档

[慕课网](https://www.imooc.com/article/details/id/26439)