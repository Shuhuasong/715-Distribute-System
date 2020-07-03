package Client;

import Server.MainServer;

import java.io.IOException;

public class Main {
    private static String name = "MainServer client";
    public static int total_passengers = 30;
    public static int maxLineSize = 3;
    public static int groupNum = 4;
    public final static int flightCapacity = 30;

    public static void main(String[] args) throws IOException, InterruptedException {
        ClientHelper helper = new ClientHelper(MainServer.port, MainServer.hostname, "flight attedant client");
        Thread flight_attentDent = new Thread(helper);
        flight_attentDent.start();
        Thread.sleep(1000);

        for (int i = 0; i < total_passengers; i++) {
            ClientHelper clientHelper = new ClientHelper(MainServer.port, MainServer.hostname, "passenger helper");
            Thread passenger = new Thread(clientHelper);
            passenger.start();
            Thread.sleep(1000);
        }

        ClientHelper clientHelper = new ClientHelper(MainServer.port, MainServer.hostname, "counter client " + 1);
        Thread counter = new Thread(clientHelper);
        counter.start();
        Thread.sleep(1000);
    }


    public static long time = System.currentTimeMillis();

    public static void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "] " + name + ": " + m);
    }
}
