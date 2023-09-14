package thread.io.cf;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class FutureApiDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            TimeUnit.SECONDS.sleep(5);
            return "task1 over";
        });

        Thread thread = new Thread(futureTask, "t1");
        thread.start();

        System.out.println(Thread.currentThread().getName() + "\t do other task.");

        System.out.println(futureTask.get());
    }
}
