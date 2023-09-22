package thread.io.cf;

import java.util.concurrent.*;

public class CompletableFutureUseDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " --come in");
            int result = ThreadLocalRandom.current().nextInt(10);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            result = result / 0;
            System.out.println("---1s 出结果" + result);
            return result;
        }, threadPool).whenComplete((v, e) -> {
            if (e == null) {
                System.out.println("----计算完成，更新系统updateVa: " + v);
            }
        }).exceptionally(e -> {
            e.printStackTrace();
            System.out.println("异常情况：" + e.getCause() + "\t" + e.getMessage());
            return null;
        });

        // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭：暂停3s线程
//        try {
//            TimeUnit.SECONDS.sleep(3);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        threadPool.shutdown();
    }

    private static void future1() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " --come in");
            int result = ThreadLocalRandom.current().nextInt(10);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("---1s 出结果" + result);
            return result;
        });

        System.out.println(Thread.currentThread().getName() + " 主线程做其他事情。。");
        System.out.println(completableFuture.get());
    }
}
