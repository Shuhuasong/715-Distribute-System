package Server.StoryLogic;

import Client.Main;
import Server.ServerHelper;

import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class Passenger extends Thread {
    private List<NotificationObject> passengersAtAirport;
    private Queue<NotificationObject> waitingLine1;
    private Queue<NotificationObject> waitingLine2;
    public TicketGenerator.Ticket ticket;
    public final NotificationObject notificationObject;
    private NotificationObject[] threeZones;
    private Plane plane;
    private boolean missedFlight = false;
    private GroupHandler groupHandler;
    private ServerHelper serverHelper;
    public Passenger(int id, List<NotificationObject> passengersAtAirport, Queue<NotificationObject> waitingLine1, Queue<NotificationObject> waitingLine2, NotificationObject[] threeZones, Plane plane, int groupSize, ServerHelper serverHelper) {
        setName("Server.StoryLogic.Passenger " + id);
        this.notificationObject = new NotificationObject(getName());
        this.passengersAtAirport = passengersAtAirport;
        this.waitingLine1 = waitingLine1;
        this.waitingLine2 = waitingLine2;
        this.threeZones = threeZones;
        this.plane = plane;
        groupHandler = GroupHandler.getInstance(groupSize);
        this.serverHelper = serverHelper;
    }

    @Override
    public void run() {
        try {
            msg("driving to the airport");
            Thread.sleep(2000);
            arriveAiport();
            gotTheNewTicket();
            goToZone();
            if (!missedFlight) {
                synchronized (notificationObject) {
                    msg("Waiting in group");
                    groupHandler.groupNewPassenger(notificationObject); //
                    if (!notificationObject.permissionToBoard)
                        notificationObject.wait();
                } //
                enteringThePlan();
                leavingAirplane();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        msg(missedFlight ? "I am going home" : "Enjoying my destination Purell-Wonderland...Opps I don't even know why I booked my flight to here ??");

        msg("exit");
        try {
            serverHelper.closeAllConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void leavingAirplane() throws InterruptedException {
        msg("Leaving the airplane");
        Thread.sleep(1000);
        synchronized (plane.lock) {
            plane.lock.notify();
        }
        msg("I have left the airplane");
    }

    private void waitingOnTheAirPlane() throws InterruptedException {
        synchronized (notificationObject) {
            msg("It's going to be a long flight. I'm going to watch some movie...");
            msg("Watching movie...");
            notificationObject.wait();
        }
    }

    private void enteringThePlan() throws InterruptedException {
        msg("Entering the plane");
        if (ticket.zoneNumber == 1) {
            msg("Finding my seat: " + ticket.seatNumber);
            Thread.sleep(1000);
            plane.letPassengerEnterThePlan(this);
            msg("I have entered the plane. My seat number is: " + ticket.seatNumber);
            FlighAttendant.increaseGroup1BoardedCount(plane.lock);
            waitingOnTheAirPlane();

        } else if (ticket.zoneNumber == 2) {
            msg("Finding my seat: " + ticket.seatNumber);
            Thread.sleep(1000);
            plane.letPassengerEnterThePlan(this);
            msg("I have entered the plane");
            FlighAttendant.increaseGroup2BoardedCount(plane.lock);
            waitingOnTheAirPlane();

        } else {
            msg("Finding my seat: " + ticket.seatNumber);
            Thread.sleep(1000);
            plane.letPassengerEnterThePlan(this);
            msg("I have entered the plane");
            FlighAttendant.increaseGroup3BoardedCount(plane.lock);
            waitingOnTheAirPlane();
        }
    }


    private void goToZone() throws InterruptedException {
        msg("Walking to zone");
        Thread.sleep(1000);
        int zoneIndex = ticket.zoneNumber - 1; //subtract 1 because array starts at 0
        synchronized (threeZones[zoneIndex]) {
            if (Utility.isGateOpen) {
                if (ticket.zoneNumber == 1)
                    FlighAttendant.increaseCountGroup1();
                else if (ticket.zoneNumber == 2)
                    FlighAttendant.increaseCountGroup2();
                else FlighAttendant.increaseCountGroup3();

                msg("Arrived zone: " + ticket.zoneNumber + "Thank god, I didn't miss my flight. Waiting to board now...");
                threeZones[zoneIndex].wait();
            } else {
                missedFlight = true;
                msg("Oh no I missed my flight. I gotta rebook my flight");
            }
        }
    }

    private void gotTheNewTicket() {
        this.ticket = notificationObject.ticket;
        msg("yayyy. I finally got my boarding pass. The info is: " + notificationObject.ticket);
    }

    private void arriveAiport() throws InterruptedException {
        msg("Got to the airport");
        int line = new Random().nextInt(3 - 1) + 1;
        msg("going to line number: " + line);
        goToLine(line, (line == 1 ? waitingLine1 : waitingLine2));
    }

    private void goToLine(int line, Queue<NotificationObject> checkInLine) throws InterruptedException {
        synchronized (notificationObject) {
            while (checkInLine.size() == Main.maxLineSize) {
                msg("Line number " + line + " is full. Waiting at the airport");
                passengersAtAirport.add(notificationObject);
                notificationObject.wait(); // wait at the airport
            }
            msg("Waiting at line number: " + line);
            checkInLine.add(notificationObject);
            notificationObject.wait(); // wait in the check-in line
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
}
