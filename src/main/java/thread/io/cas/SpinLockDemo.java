package thread.io.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class SpinLockDemo {

    AtomicReference<Thread> atomicReference = new AtomicReference<>();

    public void lock() {
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + "\t -- come in.");
        while (!atomicReference.compareAndSet(null, thread)) {

        }
    }

    public void unlock() {
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + "\t -- task over.");
        atomicReference.compareAndSet(thread, null);
    }

    public static void main(String[] args) {
        SpinLockDemo spinLockDemo = new SpinLockDemo();

        new Thread(() -> {
            spinLockDemo.lock();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            spinLockDemo.unlock();
        }, "A").start();

        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            spinLockDemo.lock();
            spinLockDemo.unlock();
        }, "B").start();
    }
}
