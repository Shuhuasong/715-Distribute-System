package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

import static Server.MainServer.totalPassengers;

public class ServerUtility {
    public static void passengerOnListen(Vector<BoardPass> line1, Vector<BoardPass> line2, BoardPass[] zoneObjects, FlightAttendant flightAttendant, List<BoardPass> passengerWaitForBoardPass) throws IOException {
        for(int i=1; i<=totalPassengers; i++){
            try(ServerSocket serverSocket = new ServerSocket(MainServer.port)){
                Socket socket = serverSocket.accept();
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                DataInputStream dis = new DataInputStream(socket.getInputStream());

                SubServer subServer = new SubServer(dos, dis, i + "th passenger helper");
                Passenger passenger = new Passenger(i, line1, line2,zoneObjects, flightAttendant , passengerWaitForBoardPass, subServer);
                passenger.start();
            }
        }
    }

    public static void countersOnListen(BoardPass.Ticket ticket, Vector<BoardPass> line1, int numCounter, List<BoardPass> passengerWaitForBoardPass) throws IOException {
        KioskCounter kiosk1[]=new  KioskCounter[2];
        for(int i=1;i<=2;i++) {
            try(ServerSocket serverSocket = new ServerSocket(MainServer.port)){
                Socket socket = serverSocket.accept();
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                DataInputStream dis = new DataInputStream(socket.getInputStream());

                SubServer subServer = new SubServer(dos, dis, i + "th kiosk counter");
                kiosk1[i] = new KioskCounter(i+1,ticket.getTicket(),passengerWaitForBoardPass, line1, numCounter, subServer);
                //kiosk1[i].subserver = subServer;
                kiosk1[i].start();
            }
        }
    }

    public static void flightAttendantOnListen(BoardPass[] zoneObjects) {
        try (ServerSocket serverSocket = new ServerSocket(MainServer.port)) {
            Socket socket = serverSocket.accept();
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            SubServer subServer = new SubServer(dos, dis, "flightAttendant");
            FlightAttendant flightAttendant = new FlightAttendant(zoneObjects, subServer);
            flightAttendant.subserver = subServer;
            flightAttendant.start();
            System.out.println("Flight Attendant Start!!");
            flightAttendant.scanPassengerAtPlaneDoor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
