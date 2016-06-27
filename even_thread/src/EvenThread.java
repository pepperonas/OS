import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class EvenThread extends Thread {

    private EvenSynchronized evenSynchronized;
    private EvenSemaphore evenSemaphore;


    EvenThread(EvenSynchronized evenSynchronized) {
        this.evenSynchronized = evenSynchronized;
    }


    EvenThread(EvenSemaphore evenSemaphore) {
        this.evenSemaphore = evenSemaphore;
    }


    public void run() {
        if (evenSynchronized != null) {
            System.out.println("Synchronized");
            for (int i = 1; i <= 1000; i++) {
                System.out.println(getName() + ": " + evenSynchronized.next());
            }
        }

        if (evenSemaphore != null) {
            System.out.println("Semaphore");
            for (int i = 1; i <= 1000; i++) {
                System.out.println(getName() + ": " + evenSemaphore.next());
            }
        }
    }


    public static void main(String[] args) {
        int select = showMenu();
        if ((select & 1) == 1) testSynchronized();
        if ((select & 2) == 2) testSemaphore();
    }


    private static int showMenu() {
        System.out.println("[1] Synchronized\n[2] Semaphore\n[3] Both");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("SELECT: ");
        try {
            String s = br.readLine();
            return Integer.parseInt(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }


    private static void testSynchronized() {
        EvenSynchronized evenSynchronized = new EvenSynchronized();
        new EvenThread(evenSynchronized).start();
        new EvenThread(evenSynchronized).start();
    }


    private static void testSemaphore() {
        Semaphore semaphore = new Semaphore(1);
        EvenSemaphore evenSemaphore = new EvenSemaphore(semaphore);
        new EvenThread(evenSemaphore).start();
        new EvenThread(evenSemaphore).start();
    }
}


class EvenSynchronized {

    private int n = 0;


    synchronized int next() { // POST: next is always even
        ++n;
        try {
            Thread.sleep((long) (Math.random() * 10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ++n;
        return n;
    }
}


class EvenSemaphore {

    private int n = 0;
    private Semaphore semaphore;


    EvenSemaphore(Semaphore semaphore) {
        this.semaphore = semaphore;
    }


    int next() {
        semaphore.p(); // DOWN to lock
        ++n;
        try {
            Thread.sleep((long) (Math.random() * 10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ++n;
        semaphore.v(); // UP to unlock
        return n;
    }
}


class Semaphore {

    private int value;


    Semaphore(int init) {
        if (init < 0)
            init = 0;
        value = init;
    }


    /**
     * DOWN
     */
    synchronized void p() {
        while (value == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        value--;
    }


    /**
     * UP
     */
    synchronized void v() {
        value++;
        notify();
    }
}