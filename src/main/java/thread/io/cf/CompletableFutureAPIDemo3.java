package thread.io.cf;

import java.util.concurrent.CompletableFuture;

/*
 * 对计算结果进行消费
 */
public class CompletableFutureAPIDemo3 {
    public static void main(String[] args) {
//        ExecutorService threadPool = Executors.newFixedThreadPool(3);
//        CompletableFuture.supplyAsync(() -> {
//            return 1;
//        }, threadPool).thenApply(f -> {
//            System.out.println("222");
//            return f + 2;
//        }).thenApply(f -> {
//            System.out.println("333");
//            return f + 3;
//        }).thenAccept(f -> System.out.println(f));
//
//        System.out.println(Thread.currentThread().getName() + "-----主线程做其他任务");
//        threadPool.shutdown();

        System.out.println(CompletableFuture.supplyAsync(()->"resultA").thenRun(()->{}).join());
        System.out.println(CompletableFuture.supplyAsync(()->"resultA").thenAccept(r-> System.out.println("dfdsfa" + r)).join());
        System.out.println(CompletableFuture.supplyAsync(()->"resultA").thenApply(r-> r + " resultB").join());
    }
}
