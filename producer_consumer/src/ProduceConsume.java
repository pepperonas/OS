class ProduceConsume {

    public static void main(String[] args) {
        Buffer b = new Buffer();

        new Producer(b, 1).start();
        new Producer(b, 101).start();
        new Producer(b, 201).start();
        new Consumer(b).start();
        new Consumer(b).start();
        new Consumer(b).start();
    }
}


class Buffer {

    private boolean available = false;
    private int data;


    synchronized void put(int x) {
        while (available) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        data = x;
        available = true;
        notifyAll();
    }


    synchronized int get() {
        while (!available) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        available = false;
        notifyAll();
        return data;
    }
}


class Consumer extends Thread {

    private Buffer buffer;


    Consumer(Buffer b) {
        buffer = b;
    }


    public void run() {
        for (int i = 0; i < 100; i++) {
            int x = buffer.get();
            System.out.println("got " + x);
        }
    }
}


class Producer extends Thread {

    private Buffer buffer;
    private int start;


    Producer(Buffer b, int s) {
        buffer = b;
        start = s;
    }


    public void run() {
        for (int i = start; i < start + 100; i++) {
            buffer.put(i);
        }
    }
}