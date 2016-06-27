class ParkingGarageOperation {

    public static void main(String[] args) {
        ParkingGarage parkingGarage = new ParkingGarage(10);
        for (int i = 1; i <= 40; i++) {
            new Car("Car " + i, parkingGarage);
        }
    }
}


class Car extends Thread {

    private ParkingGarage parkingGarage;


    Car(String name, ParkingGarage p) {
        super(name);
        this.parkingGarage = p;
        start();
    }


    public void run() {

        while (true) {
            try {
                sleep((int) (Math.random() * 10000)); // drive before parking
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            parkingGarage.enter();
            System.out.println(getName() + ": entered (" + parkingGarage.getPlaces() + ")");
            try {
                try {
                    sleep((int) (Math.random() * 20000)); // stay into the garage
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                parkingGarage.leave();
                System.out.println(getName() + ": left (" + parkingGarage.getPlaces() + ")");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}


class ParkingGarage {

    private int places;


    ParkingGarage(int places) {
        if (places < 0) {
            places = 0;
        }
        this.places = places;
    }


    synchronized void enter() { // enter parking garage
        while (places == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        places--;
    }


    synchronized void leave() { // leave parking garage
        places++;
        notify();
    }


    int getPlaces() {
        return places;
    }

}




