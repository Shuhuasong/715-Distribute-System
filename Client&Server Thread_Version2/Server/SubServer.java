package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class SubServer {
    public static long time = System.currentTimeMillis();
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private String clientName;

    public static int port = 3600;
    public static String hostName = "127.0.0.1";;

    public static int totalPassengers;
    public static int numCounter;
    public static int lineCapacity;

    public SubServer(DataOutputStream dos, DataInputStream dis, String clientName) {

        this.dos = dos;
        this.dis = dis;
        this.clientName = clientName;
    }


    public SubServer(Socket socket, DataOutputStream dos, DataInputStream dis,  int totalPassengers, int numCounter, int lineCapacity) {
        this.socket = socket;
        this.dos = dos;
        this.dis = dis;
        this.totalPassengers = totalPassengers;
        this.numCounter = numCounter;
        this.lineCapacity = lineCapacity ;
    }

//    public void run(){
//        msg("Sub Server Start");
//        BoardPass[] zoneObjects = new BoardPass[3];
//
//        zoneObjects[0] = new BoardPass(0, 1, "FirstZone");
//        zoneObjects[1] = new BoardPass(0, 2, "SecondZone");
//        zoneObjects[2] = new BoardPass(0, 3, "ThirdZonde");
//
//        List<BoardPass> passengerWaitForBoardPass = new ArrayList<>();
//
//        Vector<BoardPass> line1 = new Vector<>(lineCapacity); //the line1 waiting for the kiosk counter
//        Vector<BoardPass>  line2 = new Vector<>(lineCapacity); //the line2 waiting for the kiosk counter
//
//        BoardPass.Ticket ticket = new BoardPass.Ticket();
//
//        FlightAttendant flightAttendant = null;
//        Clock clock = new Clock(flightAttendant);

        //why we hava flightAttendant as parameter



    public void sendMessageToClient(String m) throws IOException {
        this.dos.writeUTF(m);
    }

    public void readMessageFromClient() throws IOException {
        String message = this.dis.readUTF();
        msg(message);
    }

    private void msg(String message) {
        System.out.println("[" + clientName + "]" + message);
    }

    public void closeAllConnection() throws IOException {
        dis.close();
        dos.close();
    }



}


