package Client;

import Server.MainServer;

import java.io.IOException;

public class MainClient {

    public static long time = System.currentTimeMillis();
    public final static int totalPassenger = 30;
    //private static int port = 3306;
    //private static String hostName = "127.0.0.1";

    public static void main(String[] args) throws InterruptedException, IOException {

        msg("Clients start now");


        for (int i = 1; i <= totalPassenger; i++) {
//            SubClient passengerClient = new SubClient(MainServer.hostName, MainServer.port, String.valueOf(i) + "th Passenger Client ");
//            Thread passenger = new Thread(passengerClient);
//            passenger.start();
            new PassengerClient(MainServer.hostName, MainServer.port, String.valueOf(i) + "th Passenger Client ").start();
            //passenger.start();
            Thread.sleep(1000);
        }


        //Counter
        for(int i=1; i<=2; i++){

           new KioskCounterClient( MainServer.hostName, MainServer.port, i + "th Kiosk Counter Client").start();

            Thread.sleep(1000);
        }



        //Flight Attendence

       new FlightAttendentClient(MainServer.hostName, MainServer.port, "Flight Attendence Client").start();
        Thread.sleep(500);


    }

    private  static void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis()-time) + "]"  + "Main Client: " + m);
    }

}


