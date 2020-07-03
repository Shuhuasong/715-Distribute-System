package Server;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.Vector;
import Client.MainClient;
import Server.SubServer;

public class KioskCounter extends Thread{
    public int counterId;
    public final BoardPass.Ticket ticket;
    public final  List<BoardPass> passengerWaitForBoardPass;
    public final Vector<BoardPass> waitingLine;
    public int counterNumber;
    private SubServer subserver;
    public static long time = System.currentTimeMillis();


    public KioskCounter(int counterId, BoardPass.Ticket ticket,  List<BoardPass>  passengerWaitForBoardPass, Vector<BoardPass>  waitingLine,
                        int counterNumber, SubServer subserver){
        this.counterId = counterId;
        setName("Counter" + counterId);
        this.passengerWaitForBoardPass=  passengerWaitForBoardPass;
        this.waitingLine = waitingLine;
        this.ticket = ticket;
        this.counterNumber = counterNumber;
        this.subserver = subserver;
    }


    //Method
    @Override
    public void run(){
        while(FlightAttendant.totalPassengers < MainServer.totalPassengers){
            if(!this.waitingLine.isEmpty()){
                try {
                    Thread.sleep(1000);
                    assistPassenger(counterId, waitingLine);
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }

            }else{

                try {
                    notifyOtherPassenger();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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

    public void call() {

    }


    public void msg(String m) throws IOException {
        System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
        subserver.sendMessageToClient(m);
        subserver.readMessageFromClient();
    }


    public void assistPassenger(int lineNum, Vector<BoardPass> waitingLine) throws InterruptedException, IOException { //give seat number and zone number to passenger
        msg(" Assist Passengr at line number: " + lineNum);
        if(waitingLine.size()>0){
            BoardPass newBoardPass = waitingLine.firstElement();
            Thread.sleep(1000);
            synchronized (waitingLine.firstElement()) {
                newBoardPass.ticket = ticket.getTicket();

                msg("Your seat number is " + newBoardPass.ticket.seatNumber + ", and your zone number is " + newBoardPass.ticket.zoneNumber);
                counterNumber++;
                newBoardPass.notify();
            }
        }else{
            notifyOtherPassenger();
        }
    }

    public synchronized void notifyOtherPassenger() throws IOException {// ask other passenger to wait in line

        if(passengerWaitForBoardPass.size() > 0 && counterNumber < MainServer.numCounter){
            synchronized (passengerWaitForBoardPass.get(0)){
                passengerWaitForBoardPass.remove(0).notify();
                msg("The waiting line is not full, the next passenger can wait in line");
            }
        }

    }

}
