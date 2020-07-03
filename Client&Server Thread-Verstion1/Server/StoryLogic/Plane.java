package Server.StoryLogic;

import Server.ServerHelper;

import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Queue;

/*
 * The airplane will receive the singal from the clock thread to start boarding passengers zone by zone when it's time to fly.
 * However, groupHandler will be responsible to board passengers synchronously (group by group and zone by zone). Meanwhile, the
 * airplane will wait for the signal from groupHandler to board the next group of passengers.
 * The sleep time for flying is a bit long (it'a few hours fly though). Just wait and the plane will land :).
 * */

public class Plane extends Thread {

    private Queue<Passenger> minHeap;
    private NotificationObject[] threeZones;
    public final NotificationObject lock;
    public boolean isReadyToBoardPassengers = false;
    public ServerHelper serverHelper;

    public Plane(NotificationObject[] threeZones) {
        setName("Flight CS-344715");
        //Using lambda expression introduced in Java 8 (replacement of anonymous class) to specify the minHeap based on the seat number
        this.minHeap = new PriorityQueue<>((p1, p2) -> {
            int seatNumber1 = p1.ticket.seatNumber;
            int seatNumber2 = p2.ticket.seatNumber;
            return seatNumber1 - seatNumber2; //ascending order
        });
        this.threeZones = threeZones;
        lock = new NotificationObject(getName()); }

    @Override
    public void run() {
        try {
            msg("Cleaning the plan... get ready  for the flight");
            Thread.sleep(2000);
            msg("The plane is ready to fly. Waiting for signal to board passengers...");
            synchronized (this) {
                isReadyToBoardPassengers = true;
                wait();
            }
            Thread.sleep(1000);
            msg("Boarding passengers zone by zone");
            boardPassengersZoneByZone();
            flying();
            msg("\n\nTwo hours have passed");
            landed();
            cleaningAndClosing();


            msg("exit");
            try {
                serverHelper.closeAllConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void cleaningAndClosing() throws InterruptedException {
        msg("Cleaning and closing the plane...");
        Thread.sleep(2000);
        msg("Journey is done.----------------------------------------------");
    }

    private void landed() throws InterruptedException {
        msg("I hope you all have enjoyed the flight. Now please hear your call and leave the plane carefully...\n\n\n");
        Thread.sleep(1000);
        msg("Everyone is leaving the airplane in ascending order of your seat\n\n\n");

        Thread.sleep(1000);
        while (!minHeap.isEmpty()) {
            Passenger p = callingPassengerWithSmallestSeatNumber(); //calling the passenger with the smallest seat number
            synchronized (p.notificationObject) {
                msg(p.getName() + " at seat number: " + p.ticket.seatNumber + ". Goodbye and see you again");
                p.notificationObject.notify();
                msg("Waiting for " + p.getName() + " to leave the plane");
            }
            synchronized (lock) {
                lock.wait();
            }
        }
    }

    private void flying() throws InterruptedException {
        msg("\n\n---------------Ladies and gentlemen. Please make sure to fasten your seat belt and enjoy the flight...\n" +
                "FLight CS-344715 officially departure------------------------\n---------------The plane is now flying------------\n\n");
        Thread.sleep(20000);
    }

    private void boardPassengersZoneByZone() throws InterruptedException {
        Utility.isGateOpen = false;
        for (NotificationObject zone : threeZones) {
            synchronized (zone) {
                msg("Boarding all passengers in zone number: " + zone.zoneNumber + "----------------------------\n");
                zone.notifyAll();
            }
            synchronized (lock) {
                lock.wait();
            }
        }
    }


    public static long time = System.currentTimeMillis();

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "] " + getName() + ": " + m);
        try {
            serverHelper.sendMessageToClient(m);
            serverHelper.readMessageFromClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Passenger callingPassengerWithSmallestSeatNumber() {
        return minHeap.poll();
    }

    public boolean isEmpty() {
        return minHeap.isEmpty();
    }

    public synchronized void letPassengerEnterThePlan(Passenger p) {
        minHeap.add(p);
    }
}
