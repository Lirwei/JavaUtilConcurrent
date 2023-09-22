package thread.io.lock;

class Phone {
    public synchronized void sendMail() {
        System.out.println("----sendMail");
    }

    public synchronized void sendSMS() {
        System.out.println("----sendSMS");
    }

    public void hello() {
        System.out.println("hello");
    }
}

public class Lock8Demo {
    public static void main(String[] args) {
        Phone phone = new Phone();
        Phone phone2 = new Phone();
    }
}
