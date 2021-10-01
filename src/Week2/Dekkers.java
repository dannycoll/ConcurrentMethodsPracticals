package Week2;

import java.util.Random;

public class Dekkers {

    public static volatile int favouredProcess = 1;
    public static volatile boolean processOneWants = false;
    public static volatile boolean processTwoWants = false;

    public static void main (String[] args) {
        Process1 thread1 = new Process1();
        Process2 thread2 = new Process2();

        thread1.start();
        thread2.start();
    }
}

class Process1 extends Thread {
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
        System.out.println("1 entering preprotocol");
        yield();
        Dekkers.processOneWants = true;
        yield();
        while (Dekkers.processTwoWants) {
            yield();
            if (Dekkers.favouredProcess == 2) {
                yield();
                Dekkers.processOneWants = false;
                yield();
                while (Dekkers.favouredProcess == 2){
                    yield();
                };
                Dekkers.processOneWants = true;
                yield();
            }
        }
        yield();
        System.out.println("1 leaving preprotocol");
    }

    public void criticalSection() {
        System.out.println("1 entering critical");
        Dekkers.favouredProcess = 2;
        System.out.println("1 leaving critical");
    }

    public void postProtocol() {
        Dekkers.processOneWants = false;
    }
}

class Process2 extends Thread {
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
        System.out.println("2 entering preprotocol");
        yield();
        Dekkers.processTwoWants = true;
        yield();
        while (Dekkers.processOneWants) {
            yield();
            if (Dekkers.favouredProcess == 1) {
                yield();
                Dekkers.processTwoWants = false;
                yield();
                while (Dekkers.favouredProcess == 1){
                    yield();
                };
                Dekkers.processTwoWants = true;
                yield();
            }
        }
        yield();
        System.out.println("2 leaving preprotocol");
    }

    public void criticalSection() {
        System.out.println("2 entering critical");
        Dekkers.favouredProcess = 1;
        System.out.println("2 leaving critical");
    }

    public void postProtocol() {
        Dekkers.processTwoWants = false;
    }
}