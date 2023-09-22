package thread.io.cf;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/*
 * 对计算速度进行合并
 */
public class CompletableFutureAPIDemo5 {
    public static void main(String[] args) {
        CompletableFuture<Integer> playA = CompletableFuture.supplyAsync(() -> {
            System.out.println("A come in");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 10;
        });

        CompletableFuture<Integer> playB = CompletableFuture.supplyAsync(() -> {
            System.out.println("B come in");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 20;
        });

        CompletableFuture<Integer> resultFuture = playA.thenCombine(playB, (x, y) -> {
            System.out.println("开始合并");
            return x + y;
        });

        System.out.println(resultFuture.join());
    }
}
