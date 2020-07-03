package Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Vector;
import Client.MainClient;
import Server.SubServer;

public class Passenger extends Thread{
    Queue<Integer> q=new LinkedList<>();
    public int passengerId;
    public Vector<BoardPass> line1;
    public Vector<BoardPass>  line2;
    public BoardPass[] zoneObjects;
    public FlightAttendant flight;
    public List<BoardPass> passengerWaitForBoardPass;
    public BoardPass boardpass;
    public BoardPass.Ticket ticket;
    public SubServer subServer;
    public HashMap<Integer,Integer> map;
    public Passenger(int passengerId, Vector<BoardPass> line1, Vector<BoardPass> line2, BoardPass[] zoneObjects, FlightAttendant flight,List<BoardPass> passengerWaitForBoardPass,SubServer subServer
    ){
        setName("Passenger " + passengerId);
        this.passengerId = passengerId;
        this.boardpass =  new BoardPass(getName());
        this.line1 = line1;
        this.line2 = line2;
        this.zoneObjects = zoneObjects;
        this.flight = flight;
        this.passengerWaitForBoardPass = passengerWaitForBoardPass;
        this.map=map;
        this.subServer = subServer;

    }

    @Override
    public void run(){

        try {
            msg("Arrive at the airport");
            Thread.sleep(500);
            arriveAiport();
//            getBoardingPass();
            Thread.sleep(1000);

            //  enterAirplane();
            enjoyOnFlight();
            disembarkPlane();

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        try {
            msg("End");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            subServer.closeAllConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //Methods
    public void msg(String m) throws IOException {
        System.out.println("["+(System.currentTimeMillis()-MainServer.time)+"] "+getName()+": "+m);
        try{
            subServer.sendMessageToClient(m);
            subServer.readMessageFromClient();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public synchronized void  arriveAiport() throws InterruptedException, IOException { //checking in kiosk to print boarding pass
        msg("Arriving the counters eventually");
        Random rand = new Random();
        int numLine = rand.nextInt(2)+1; //get the number 1-3
        q.add(passengerId);
        synchronized(q) {
            if(!q.isEmpty()) {
                if(numLine == 1){
                    waitInLine(numLine, line1);
                }else{
                    waitInLine(numLine, line2);
                }
            }
        }

    }
    public void waitInLine(int lineNum, Vector<BoardPass>  line) throws InterruptedException, IOException { //block on a different object(same zone =same object)
        synchronized (boardpass){
            while(line.size() == MainServer.numCounter){
                msg("Line "+lineNum+" is too full, please come back later");
                passengerWaitForBoardPass.add(boardpass);
                boardpass.wait();
            }
            line.add(boardpass);

            msg("You are waiting in " + lineNum + " line");
            boardpass.wait();
        }
    }


    public void enjoyOnFlight() throws InterruptedException, IOException { ////All other passengers get entertained on the flight in transit to their destination (use wait).
        synchronized (boardpass){
            msg("I am on the airplane right now");
            boardpass.wait();
        }
    }


    public void disembarkPlane() throws InterruptedException, IOException { //passengers are asked (notified) to leave the plane in ascending order of their seat number
        msg("Disembark from the airplane");
        Thread.sleep(1000);
        synchronized(flight.lock){
            flight.lock.notify(); //last passenger notify flight attendant
        }
    }




}
