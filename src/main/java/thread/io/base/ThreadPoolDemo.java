package thread.io.base;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolDemo {
    public static void main(String[] args) {
        Executors.newCachedThreadPool();
        Executors.newFixedThreadPool(10);
        Executors.newSingleThreadExecutor();

        new ThreadPoolExecutor(
                10,
                20,
                0L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10));
    }
}
