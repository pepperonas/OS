public class MutualExclusion {

    // cd /Users/martin/IdeaProjects/OS/mutual_exclusion/out/production/BSMutexThread/
    // java MutualExclusion 0
    // or
    // java MutualExclusion 1


    public static void main(String[] args) {
        int noThreadsInCriticalSection = 1;
        if (args.length != 1) {
            System.err.println("usage: java MutualExclusion <NoThreadsInCriticalSection >");
            System.exit(1);
        } else {
            noThreadsInCriticalSection = Integer.parseInt(args[0]);
        }
        Semaphore mutex = new Semaphore(noThreadsInCriticalSection);
        for (int i = 1; i <= 10; i++) {
            new MutexThread(mutex, "Thread " + i);
        }
    }
}


class MutexThread extends Thread {

    private Semaphore mutex;


    MutexThread(Semaphore mutex, String name) {
        super(name);
        this.mutex = mutex;
        start();
    }


    public void run() {
        while (true) {
            mutex.p();
            System.out.println("kritischen Abschnitt betreten: " + getName());
            try {
                sleep((int) (Math.random() * 100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mutex.v();
            System.out.println("kritischen Abschnitt verlassen: " + getName());
        }
    }
}


class Semaphore {

    private int value;


    Semaphore(int init) {
        if (init < 0) {
            init = 0;
        }
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