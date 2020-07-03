package Server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainServer {

    public static long time = System.currentTimeMillis();

    public static int port = 3800;
    public static String hostName = "127.0.0.1";
    public static Socket socket;
    public static int totalPassengers = 30;
    public static int numCounter = 2;
    public static int lineCapacity = 10;


    private static void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "]" + " Main Server " + m);
    }

    public static void main(String[] args) throws IOException {

        msg("Sub Server Start");
        BoardPass[] zoneObjects = new BoardPass[3];

        zoneObjects[0] = new BoardPass(0, 1, "FirstZone");
        zoneObjects[1] = new BoardPass(0, 2, "SecondZone");
        zoneObjects[2] = new BoardPass(0, 3, "ThirdZonde");

        List<BoardPass> passengerWaitForBoardPass = new ArrayList<>();

        Vector<BoardPass> line1 = new Vector<>(lineCapacity); //the line1 waiting for the kiosk counter
        Vector<BoardPass> line2 = new Vector<>(lineCapacity); //the line2 waiting for the kiosk counter

        BoardPass.Ticket ticket = new BoardPass.Ticket();

        FlightAttendant flightAttendant = null;
        Clock clock = new Clock(flightAttendant);


        ServerUtility.passengerOnListen(line1, line2, zoneObjects, flightAttendant, passengerWaitForBoardPass);

        clock.start();

        ServerUtility.countersOnListen(ticket, line1, numCounter, passengerWaitForBoardPass);

        ServerUtility.flightAttendantOnListen(zoneObjects);
    }

}


