package Week3;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

class FetchAndAddMutex {
    private AtomicInteger dispenser;
    private AtomicInteger current;

    FetchAndAddMutex(){
        dispenser = new AtomicInteger();
        current = new AtomicInteger();
    }

    public void pre_protocol(AtomicInteger ticket) {
        fetchAndAdd(dispenser,1,ticket);
        while (ticket.get() != current.get()) {}
    }

    public void post_protocol(AtomicInteger ignored) {
        fetchAndAdd(current,1, ignored);
    }

    public void fetchAndAdd(AtomicInteger var, int value, AtomicInteger old) {
        old.set(var.get());
        var.addAndGet(value);
    }
}
class FetchAndAdd {
    public static void main (String[] args) {
        FetchAndAddMutex faa_mutex = new FetchAndAddMutex();
        ProcessF thread1 = new ProcessF( faa_mutex, 1 );
        ProcessF thread2 = new ProcessF( faa_mutex, 2 );
        ProcessF thread3 = new ProcessF( faa_mutex, 3 );

        thread1.start();
        thread2.start();
        thread3.start();
    }
}

class ProcessF extends Thread {
    private Random rnd = new Random();
    private FetchAndAddMutex mux;
    private int id;
    private AtomicInteger ticket;
    private AtomicInteger ignored;

    ProcessF( FetchAndAddMutex mux_, int id_ ) {
        mux = mux_;
        id = id_;
        ticket = new AtomicInteger();
        ignored = new AtomicInteger();
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
        mux.pre_protocol(ticket);
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
        mux.post_protocol(ignored);
        //System.out.println(id + " postp: Leaving postProtocol section");
    }
}