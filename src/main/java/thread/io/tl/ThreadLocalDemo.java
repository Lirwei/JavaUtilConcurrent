package thread.io.tl;


import java.util.Random;
import java.util.concurrent.TimeUnit;

class House {
    int saleCount = 0;

    public synchronized void saleHouse() {
        ++saleCount;
    }

//    ThreadLocal<Integer> saleVolume = new ThreadLocal<Integer>() {
//        @Override
//        protected Integer initialValue() {
//            return 0;
//        }
//    };

    ThreadLocal<Integer> saleVolume = ThreadLocal.withInitial(() -> 0);

    public void saleVolumeByThreadLocal() {
        saleVolume.set(1 + saleVolume.get());
    }
}

public class ThreadLocalDemo {
    public static void main(String[] args) {
        House house = new House();
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                int size = new Random().nextInt(5) + 1;
                try {
                    for (int j = 0; j < size; j++) {
                        house.saleHouse();
                        house.saleVolumeByThreadLocal();
                    }
                } finally {
                    house.saleVolume.remove();
                }
                System.out.println(Thread.currentThread().getName() + "\t 售卖房子：" + size);
            }, String.valueOf(i)).start();
        }

        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(Thread.currentThread().getName() + "\t" + "共计卖出多少套： " + house.saleCount);
    }
}
