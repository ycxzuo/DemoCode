package com.yczuoxin.concurrent.demo.basis.lock;

/**
 * 可重入锁
 * 当一个线程获取一个被其他线程持有的独占锁时，该线程会被阻塞，
 * 那么当一个线程再次获取他自己已经获取的锁时是否还会被阻塞呢？
 * 如果不被阻塞，那么我们说该锁是可重入锁，也就是只要改线程获取了该锁，
 * 就可以无限次数（严格来说是有限次数）的进入该锁锁住的代码
 *
 * synchronized 也是一个可重入锁
 * 可重入锁原理是在锁内部维护一个线程标识，用来标识该锁目前被哪个线程占用，
 * 然后关联一个计数器，一开始计数器的值为 0，说明该所没有被任何线程占用，
 * 当一个线程获取了该锁，计数器的值就会变为 1，如果起来现场再来获取该锁时，
 * 会发现锁的所有者不是自己而被阻塞挂起，如果发现锁拥有者是自己，就会把计数器 +1，
 * 当释放锁后 -1，当计数器值为 0 时，所里面的线程标识被重置为 null，
 * 这时候被阻塞的线程会被唤醒来竞争获取该锁
 *
 */
public class ReentrantLockDemo {
    public synchronized void helloA() {
        System.out.println("Hello A");
    }

    public synchronized void helloB() {
        System.out.println("Hello B");
        helloA();
    }

    public static void main(String[] args) {
        ReentrantLockDemo demo = new ReentrantLockDemo();
        demo.helloB();
    }


}
