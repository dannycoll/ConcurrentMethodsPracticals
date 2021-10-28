package Week3;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.Random;

class Time {
    public static void delay( int msec ) {
        // Pause thread for specified number of milliseconds
        try {
            Thread.sleep( msec );
        } catch( InterruptedException e ) {
            Thread.currentThread().interrupt();
        }
    }
}

class TestAndSetMutex {
    private AtomicInteger c;
    TestAndSetMutex() {
	    c = new AtomicInteger();
    }

    public void pre_protocol(AtomicInteger Li) {
        do {
            Li.set(c.getAndSet(1));
        } while(Li.get() != 0);
    }

    public void post_protocol() {
        c.set(0);
    }
}

class TestAndSet {
    public static void main (String[] args) {
        TestAndSetMutex tas_mutex = new TestAndSetMutex();
        Process thread1 = new Process( tas_mutex, 1 );
        Process thread2 = new Process( tas_mutex, 2 );
        Process thread3 = new Process( tas_mutex, 3 );

        thread1.start();
        thread2.start();
        thread3.start();
    }
}

class Process extends Thread {
    private Random rnd = new Random();
    private TestAndSetMutex mux;
    private int id;
    private AtomicInteger Li;

    Process( TestAndSetMutex mux_, int id_ ) {
        mux = mux_;
        id = id_;
        Li = new AtomicInteger();
    }

    public void run() {
        while (true) {
            nonCriticalSection();
            preProtocol();
            criticalSection();
            postProtocol();
        }
    }

    public void nonCriticalSection() {
        //System.out.println(id + " nc: Entering nonCritical section");
        Time.delay(rnd.nextInt(20));

        //System.out.println(id + " nc: Leaving nonCritical section");
    }

    public void preProtocol() {
        //System.out.println(id + " prep: Entering preProtocol section");
        mux.pre_protocol(Li);
    	Time.delay(rnd.nextInt(20));
        //System.out.println(id + " prep: Leaving preProtocol section");
    }

    public void criticalSection() {
         System.out.println(id + " cs: Entering critical section");
         System.out.println(id + " cs: In critical section");
         Time.delay(rnd.nextInt(20));
         System.out.println(id + " cs: Leaving critical section");
    }

    public void postProtocol() {
        Time.delay(rnd.nextInt(20));
        //System.out.println(id + " postp: Entering postProtocol section");
        mux.post_protocol();
        //System.out.println(id + " postp: Leaving postProtocol section");
    }
}
