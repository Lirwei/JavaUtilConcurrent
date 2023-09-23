package thread.io.tl;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

class MyObject {

    @Override
    protected void finalize() throws Throwable {
        System.out.println("---invoke finalize method!---");
    }
}

public class ReferenceDemo {

    public static void main(String[] args) {
//        strongReference();
//        weakReference();
        softReference();
//        phantomReference();
    }

    // -Xms=10M -Xmm=10M
    static void phantomReference() {
        MyObject myObject = new MyObject();
        ReferenceQueue<MyObject> referenceQueue = new ReferenceQueue<>();
        PhantomReference<MyObject> objectPhantomReference = new PhantomReference<>(myObject, referenceQueue);

        List<byte[]> list = new ArrayList<>();
        new Thread(() -> {
            while (true) {
                list.add(new byte[1 * 1024 * 1024]);
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(objectPhantomReference.get() + "\t" + "list add ok.");
            }
        }, "A").start();

        new Thread(() -> {
            while (true) {
                Reference<? extends MyObject> reference = referenceQueue.poll();
                if (reference != null) {
                    System.out.println("----有虚对象回收加入了队列");
                    break;
                }
            }
        }, "B").start();
    }

    static void weakReference() {
        SoftReference<MyObject> softReference = new SoftReference<>(new MyObject());
        //System.out.println("-----softReference:"+softReference.get());

        System.gc();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("-----gc after内存够用: " + softReference.get());

        try {
            byte[] bytes = new byte[20 * 1024 * 1024];//20MB对象
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("-----gc after内存不够: " + softReference.get());
        }
    }

    static void softReference() {
        SoftReference<MyObject> softReference = new SoftReference<>(new MyObject());
        System.out.println("---gc before 内存够用：" + softReference);

        try {
            byte[] bytes = new byte[1024 * 1024 * 10];
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("---gc after 内存不够用：" + softReference);
        }
    }

    static void strongReference() {
        MyObject myObject = new MyObject();
        System.out.println("myObject: " + myObject);

        myObject = null;
        System.gc();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("myObject: " + myObject);
    }

}
