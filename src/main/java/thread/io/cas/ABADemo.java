package thread.io.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

public class ABADemo {

    static AtomicInteger atomicInteger = new AtomicInteger(100);
    static AtomicStampedReference<Integer> stampedReference = new AtomicStampedReference<>(100, 1);

    public static void main(String[] args) {
        new Thread(() -> {
            int stamp = stampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + "\t 首次版本号：" + stamp);

            // 暂停500ms，保证后面的t4线程初始化拿到的初次版本号一致
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            stampedReference.compareAndSet(100, 101, stampedReference.getStamp(), stampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + "\t 2次版本号：" + stampedReference.getStamp());

            stampedReference.compareAndSet(101, 100, stampedReference.getStamp(), stampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + "\t 3次版本号：" + stampedReference.getStamp());

        }, "t3").start();
        new Thread(() -> {
            int stamp = stampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + "\t 首次版本号：" + stamp);

            // 暂停1s，等待上面t3线程，发生ABA问题
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            boolean b = stampedReference.compareAndSet(100, 2023, stamp, stamp + 1);
            System.out.println(b + "\t" + stampedReference.getReference() + "\t" + stampedReference.getStamp());

        }, "t4").start();

    }

    static void abaHappen() {
        new Thread(() -> {
            atomicInteger.compareAndSet(100, 101);
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            atomicInteger.compareAndSet(101, 100);
        }, "t1").start();


        new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(atomicInteger.compareAndSet(100, 2023) + "\t" + atomicInteger.get());
        }, "t2").start();
    }

}
