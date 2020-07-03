package Server;

import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Queue;
import Client.MainClient;
import Server.SubServer;

public class FlightAttendant extends Thread{

    private Queue<Passenger>  passengersQ;
    private BoardPass[] zoneObjects;
    public final BoardPass lock;

    public boolean canLoadPassenger = false;
    public static int num=0;
    public static boolean gateOpen = true;
    public static int totalPassengers = 0;

    public static int groupNum = 0;
    public static long time = System.currentTimeMillis();
    public SubServer subserver;


    public FlightAttendant(BoardPass[] zoneObjects,SubServer subserver){
        setName("Flight Attendant");
        this.passengersQ = new PriorityQueue<>((p1, p2)->p1.ticket.seatNumber-p2.ticket.seatNumber);//Ascending order of seatNumber
        lock = new BoardPass(getName());
        this.zoneObjects = zoneObjects;
        this.subserver = subserver;
    }


    public void msg(String m) throws IOException {
        System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
        subserver.readMessageFromClient();
        subserver.sendMessageToClient(m);
    }

    public void run(){

        try {

            Thread.sleep(31000);
            msg("Please enter the airplane Zone by one Zone");
            msg("Scan Passenger's Board Pass Zone by Zone");
            scanPassengerAtPlaneDoor();
            if(totalPassengers >=  MainServer.totalPassengers){
                msg("Please Go home and rebook the flight ticket.");
            }
            msg("The airplane is too full right now, we gonna close the door.");
            anounceClosePlnaeDoor();
            for(int i=0;i<5;i++) {
                msg("Plane is flying............");
                Thread.sleep(1000);
            }


            arriveDestination();

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        try {
            msg("End");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            subserver.closeAllConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void scanPassengerAtPlaneDoor() throws InterruptedException, IOException {  //call them zone by zone

        for(int i=0; i<3; i++){

            synchronized (zoneObjects[i]){
                totalPassengers += zoneObjects.length * 10;
                msg("Scan Passenger's Board Pass with zone number " + zoneObjects[i].threadName);
//               zoneObjects[i].notifyAll();
                sleep(1000);
                msg("All passengers with zone number "+ zoneObjects[i].threadName+" already got in ");

            }
        }
    }

    public synchronized  void passengerGoInPlane(Passenger p){
        passengersQ.offer(p);
    }

    public void anounceClosePlnaeDoor() throws InterruptedException, IOException {
        msg("The airplane is going to close the door and take off, Please seat well on the seat.");
        Thread.sleep(1000);
    }


    public void  notifyPasengerAscendingOrder() throws InterruptedException, IOException {
        msg("Notify Passenger to leave the airplane by ascending order of seatNumber");

        for(int i=1;i<zoneObjects.length+1;i++) {
            sleep(2000);
            msg("Zone "+i+" is leaving from airplane");

        }
        sleep(1000);
        msg("All zone are already left");
    }

    public void arriveDestination() throws InterruptedException, IOException {
        msg(" The airplane is arriving the destination, every one please leave in ascending order");
        Thread.sleep(1000);
//        while(!passengersQ.isEmpty()){
        notifyPasengerAscendingOrder();
//        }
    }


}
