package thread.io.cf;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/*
 * 获取结果和触发计算
 */
public class CompletableFutureAPIDemo1 {
    public static void main(String[] args) {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "abc";
        });

//        System.out.println(completableFuture.get());
//        System.out.println(completableFuture.get(2, TimeUnit.SECONDS));
        System.out.println(completableFuture.join());

        System.out.println(completableFuture.getNow("xxx"));
        System.out.println(completableFuture.complete("xxx") + "\t" + completableFuture.join());
    }
}
