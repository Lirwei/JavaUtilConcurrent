package thread.io.atomics;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

class Var {
    public volatile Boolean isInit = Boolean.FALSE;

    AtomicReferenceFieldUpdater<Var, Boolean> referenceFieldUpdater =
            AtomicReferenceFieldUpdater.newUpdater(Var.class, Boolean.class, "isInit");

    public void init() {
        if (referenceFieldUpdater.compareAndSet(this, Boolean.FALSE, Boolean.TRUE)) {
            System.out.println(Thread.currentThread().getName() + "\t ---start init, need 2s.");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "\t ---over init.");
        } else {
            System.out.println(Thread.currentThread().getName() + "\t 已经有线程在进行初始化工作...");
        }
    }
}

public class AtomicReferenceFieldUpdaterDemo {
    public static void main(String[] args) {
        Var var = new Var();

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                var.init();
            }, String.valueOf(i)).start();
        }


    }
}
