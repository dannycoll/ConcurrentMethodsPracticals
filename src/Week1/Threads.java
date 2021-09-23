package Week1;

class threadOne extends Thread{
    public void run(){
        System.out.println("Hello from thread one!");
    }
}

class threadTwo extends Thread{
    public void run(){
        System.out.println("Hello from thread two!");
    }
}

public class Threads {
    public static void main(String[] args) {
        (new threadOne()).start();
    }
}
