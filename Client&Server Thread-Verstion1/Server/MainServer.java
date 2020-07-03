package Server;

import Server.StoryLogic.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static Client.Main.*;

public class MainServer {
    public static final int port = 8080;
    public static final String hostname = "localHost";


    public static void main(String[] args) throws IOException {
        List<NotificationObject> passengersAtAirport = new ArrayList<>();
        Queue<NotificationObject> waitingLine1 = new ArrayDeque<>(maxLineSize);
        Queue<NotificationObject> waitingLine2 = new ArrayDeque<>(maxLineSize);
        TicketGenerator ticketGenerator = new TicketGenerator();
        NotificationObject[] threeZones = {
                new NotificationObject("zone1", 1),
                new NotificationObject("zone2", 2),
                new NotificationObject("zone3", 3)
        };

        Plane plane;

        plane = new Plane(threeZones);

        //spawn

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Socket connection = serverSocket.accept();
            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
            DataInputStream dis = new DataInputStream(connection.getInputStream());

            ServerHelper serverHelper = new ServerHelper(dos, dis, "PlaneHelper");
            plane.serverHelper = serverHelper;
            //spawn
            plane.start();
        }
        Clock clock = new Clock(plane);

        for (int i = 0; i < total_passengers; i++) {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                Socket connection = serverSocket.accept();
                DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                DataInputStream dis = new DataInputStream(connection.getInputStream());

                //spawn
                ServerHelper serverHelper = new ServerHelper(dos, dis, "passengerHelper" + i);
                Passenger passenger = new Passenger(i, passengersAtAirport, waitingLine1, waitingLine2, threeZones, plane, groupNum, serverHelper);
                passenger.start();
            }
        }

        clock.start();
        Counter counter1 = new Counter(1, passengersAtAirport, waitingLine1, ticketGenerator);
        counter1.start();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Socket connection = serverSocket.accept();
            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
            DataInputStream dis = new DataInputStream(connection.getInputStream());

            //spawn
            ServerHelper serverHelper = new ServerHelper(dos, dis, "CounterHelper number: " + 2);
            Counter counter = new Counter(2, passengersAtAirport, waitingLine2, ticketGenerator);
            counter.serverHelper = serverHelper;
            counter.start();
        }

    }

}
