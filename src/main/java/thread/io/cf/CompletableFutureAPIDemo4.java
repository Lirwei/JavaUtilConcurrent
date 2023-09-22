package thread.io.cf;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/*
 * 对计算速度进行选用
 */
public class CompletableFutureAPIDemo4 {
    public static void main(String[] args) {
        CompletableFuture<String> playA = CompletableFuture.supplyAsync(() -> {
            System.out.println("A come in");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "playA";
        });

        CompletableFuture<String> playB = CompletableFuture.supplyAsync(() -> {
            System.out.println("B come in");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "playB";
        });

        CompletableFuture<String> resultFuture = playA.applyToEither(playB, f -> {
            return f + " is winner";
        });

        System.out.println(Thread.currentThread().getName() + "\t" + "----: " + resultFuture.join());
    }
}
