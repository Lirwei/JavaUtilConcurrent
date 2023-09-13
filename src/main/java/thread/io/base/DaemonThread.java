package thread.io.base;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class DaemonThread {
    public static void main(String[] args) {
        CompletableFuture Ll = new CompletableFuture<>();

        Thread t1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t开始运行， " +
                    ((Thread.currentThread().isDaemon()) ? "守护线程" : "用户进程"));
            while (true) {

            }
        }, "t1");
        t1.setDaemon(true); // 将t1线程设置为守护线程
        t1.start();

        // 暂停几秒钟
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(Thread.currentThread().getName() + "\t ---end 主线程");
    }
}