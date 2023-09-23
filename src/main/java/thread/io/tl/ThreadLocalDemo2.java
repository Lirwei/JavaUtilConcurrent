package thread.io.tl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class MyData {
    ThreadLocal<Integer> threadLocalField = ThreadLocal.withInitial(() -> 0);

    public void add() {
        threadLocalField.set(1 + threadLocalField.get());
    }
}

public class ThreadLocalDemo2 {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        MyData myData = new MyData();

        try {
            for (int i = 0; i < 10; i++) {
                executorService.submit(() -> {
                    try {
                        Integer beforeInt = myData.threadLocalField.get();
                        myData.add();
                        Integer afterInt = myData.threadLocalField.get();
                        System.out.println(Thread.currentThread().getName() + "\t" + "beforeInt:" + beforeInt + "\t afterInt: " + afterInt);
                    } finally {
                        myData.threadLocalField.remove(); // 必须对ThreadLocal进行回收
                    }
                });
            }
        } finally {
            executorService.shutdown();
        }

    }
}
