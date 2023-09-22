package thread.io.atomics;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

class ClickNumber {
    int number = 0;
    AtomicLong atomicLong = new AtomicLong(0);
    LongAdder longAdder = new LongAdder();
    LongAccumulator longAccumulator = new LongAccumulator((x, y) -> x + y, 0);

    public synchronized void clickBySynchronized() {
        number++;
    }

    public void clickByAtomicLong() {
        atomicLong.getAndIncrement();
    }

    public void clickByLongAdder() {
        longAdder.increment();
    }

    public void clickByLongAccumulator() {
        longAccumulator.accumulate(1);
    }

}

public class AccumulatorCompareDemo {
    public static final int _1W = 100000;
    public static final int THREAD_NUMBER = 50;

    public static void main(String[] args) throws InterruptedException {
        ClickNumber clickNumber = new ClickNumber();
        long startTime;
        long endTime;

        CountDownLatch countDownLatch1 = new CountDownLatch(THREAD_NUMBER);
        CountDownLatch countDownLatch2 = new CountDownLatch(THREAD_NUMBER);
        CountDownLatch countDownLatch3 = new CountDownLatch(THREAD_NUMBER);
        CountDownLatch countDownLatch4 = new CountDownLatch(THREAD_NUMBER);

        startTime = System.currentTimeMillis();
        for (int t = 0; t < THREAD_NUMBER; t++) {
            new Thread(() -> {
                try {
                    for (int i = 0; i < _1W; i++) {
                        clickNumber.clickBySynchronized();
                    }
                } finally {
                    countDownLatch1.countDown();
                }
            }, "clickBySynchronized" + t).start();

        }
        countDownLatch1.await();

        endTime = System.currentTimeMillis();
        System.out.println("----[clickBySynchronized] costTime: " + (endTime - startTime) + "ms, click number: " + clickNumber.number);


        startTime = System.currentTimeMillis();
        for (int t = 0; t < THREAD_NUMBER; t++) {
            new Thread(() -> {
                try {
                    for (int i = 0; i < _1W; i++) {
                        clickNumber.clickByAtomicLong();
                    }
                } finally {
                    countDownLatch2.countDown();
                }
            }, "clickByAtomicLong " + t).start();

        }
        countDownLatch2.await();

        endTime = System.currentTimeMillis();
        System.out.println("----[clickByAtomicLong] costTime: " + (endTime - startTime) + "ms, click number: " + clickNumber.atomicLong.get());

        startTime = System.currentTimeMillis();
        for (int t = 0; t < THREAD_NUMBER; t++) {
            new Thread(() -> {
                try {
                    for (int i = 0; i < _1W; i++) {
                        clickNumber.clickByLongAdder();
                    }
                } finally {
                    countDownLatch3.countDown();
                }
            }, "clickByLongAdder" + t).start();

        }
        countDownLatch3.await();

        endTime = System.currentTimeMillis();
        System.out.println("----[clickByLongAdder] costTime: " + (endTime - startTime) + "ms, click number: " + clickNumber.longAdder.sum());

        startTime = System.currentTimeMillis();
        for (int t = 0; t < THREAD_NUMBER; t++) {
            new Thread(() -> {
                try {
                    for (int i = 0; i < _1W; i++) {
                        clickNumber.clickByLongAccumulator();
                    }
                } finally {
                    countDownLatch4.countDown();
                }
            }, "clickByLongAccumulator" + t).start();

        }
        countDownLatch4.await();

        endTime = System.currentTimeMillis();
        System.out.println("----[clickByLongAccumulator] costTime: " + (endTime - startTime) + "ms, click number: " + clickNumber.longAccumulator.get());

    }


}
