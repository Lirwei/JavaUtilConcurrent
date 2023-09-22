package thread.io.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReEntryLockDemo {

    public synchronized void m1() {
        System.out.println(Thread.currentThread().getName() + "\t --- m1");
        m2();
    }

    private synchronized void m2() {
        System.out.println(Thread.currentThread().getName() + "\t --- m2");
        m3();
    }

    private synchronized void m3() {
        System.out.println(Thread.currentThread().getName() + "\t --- m3");
    }

    static Lock lock = new ReentrantLock();

    public static void main(String[] args) {
//        ReEntryLockDemo lockDemo = new ReEntryLockDemo();
//        new Thread(() -> {
//            lockDemo.m1();
//        }).start();

        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "\t --- lock1");
                lock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + "\t --- lock2");
                    lock.lock();
                    try {
                        System.out.println(Thread.currentThread().getName() + "\t --- lock3");
                    } finally {
                        lock.unlock();
                    }
                } finally {
                    lock.unlock();
                }
            } finally {
                lock.unlock();
            }
        }).start();
    }


    public static void retryEntryM1() {
        final Object object = new Object();

        new Thread(() -> {
            synchronized (object) {
                System.out.println(Thread.currentThread().getName() + "\t --- 外层调用");
                synchronized (object) {
                    System.out.println(Thread.currentThread().getName() + "\t --- 中层调用");
                    synchronized (object) {
                        System.out.println(Thread.currentThread().getName() + "\t --- 内层调用");
                    }
                }
            }
        }).start();
    }

}
