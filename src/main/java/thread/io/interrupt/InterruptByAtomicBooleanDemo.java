package thread.io.interrupt;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class InterruptByAtomicBooleanDemo {
    static AtomicBoolean isStop = new AtomicBoolean(false);

    public static void main(String[] args) {
        new Thread(() -> {
            while (true) {
                if (isStop.get()) {
                    System.out.println(Thread.currentThread().getName() + "\t isStop now is true.");
                    break;
                }
                System.out.println("t1 -- hello volatile.");
            }
        }, "t1").start();

        try {
            TimeUnit.MILLISECONDS.sleep(20);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            isStop.set(true);
        }, "t2").start();
    }
}
