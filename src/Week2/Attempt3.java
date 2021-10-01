package Week2;

/*===========================================================
    Java version of the thrid mutal exclusion solution.
    This algorithm can deadlock.

    (1)  Run the program and observe that mutual exclusion is
         statisified.
    (2)  Remove the comments. The yield instruction should force
         interleaving in the preProtocols resulting in deadlock.

    G McClements 30/6/99; PLK February 2000; NSS July 2001;
    HV September 2014.
===========================================================*/
import java.util.Random;

class Time3 {
    public static void delay( int msec ) {
        // Pause thread for specified number of milliseconds
        try {
            Thread.sleep( msec );
        } catch( InterruptedException e ) {
            Thread.currentThread().interrupt();
        }
    }
}

class Attempt3 {

    public static volatile int c1 = 1;
    public static volatile int c2 = 1;

    public static void main (String[] args) {

        P5 thread1 = new P5();
        P6 thread2 = new P6();

        thread1.start();
        thread2.start();
    }
}

class P5 extends Thread {
    Random rnd = new Random();

    public void run() {
        while (true) {
            nonCriticalSection();
            preProtocol();
            criticalSection();
            postProtocol();
        }
    }

    public void nonCriticalSection() {
    }

    public void preProtocol() {
        Attempt3.c1 = 0;
        yield();
        while (Attempt3.c2 != 1) {
            System.out.println("1 prep: in preProtocol loop");
            Time4.delay(rnd.nextInt(120));
        }
    }

    public void criticalSection() {
        System.out.println("1 cs: Entering critical section ");
        System.out.println("1 cs: In critical section ");
        Time4.delay(rnd.nextInt(120));
        System.out.println("1 cs: Leaving critical section ");
    }

    public void postProtocol() {
        Attempt3.c1 = 1;
        Time4.delay(rnd.nextInt(120));
    }
}

class P6 extends Thread {
    Random rnd = new Random();

    public void run() {
        while (true) {
            nonCriticalSection();
            preProtocol();
            criticalSection();
            postProtocol();
        }
    }

    public void nonCriticalSection() {
    }

    public void preProtocol() {
        Attempt3.c2 = 0;
        yield();
        while (Attempt3.c1 != 1) {
             System.out.println("2 prep: In preProtocol loop ");
             Time4.delay(rnd.nextInt(120));
        }
    }

    public void criticalSection() {
        System.out.println("2 cs: Entering critical section");
        System.out.println("2 cs: In critical section");
        Time4.delay(rnd.nextInt(120));
        System.out.println("2 cs: Leaving critical section");
    }

    public void postProtocol() {
        Attempt3.c2 = 1;
        Time4.delay(rnd.nextInt(120));
    }
}

