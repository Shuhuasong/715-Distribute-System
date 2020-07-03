package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class FlightAttendentClient extends Thread {
    public static long time = System.currentTimeMillis();
    private static int port;
    private static String address;
    Socket socket;
    DataOutputStream dos;
    DataInputStream dis;
    String clientName;
    String message;
    public FlightAttendentClient(String address, int port, String clientName){
        this.address = address;
        this.port = port;
        this.clientName = clientName;
    }

    public void  run (){
        try {
            boolean listen = false;
            while (!listen) {
                try {
                    socket = new Socket(address, port);
                    listen = true;
                } catch (ConnectException ignored) {
                }
            }
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            msg(" has Connected with server");

            msg("Ask Server Excuted ScanPassengerAtPlaneDoor");
            dos.writeUTF("Excuted ScanPassengerAtPlaneDoor");
            message = dis.readUTF();
            msg("Response From Server: "+ message);

            msg("Ask Server Excuted anounceClosePlaneDoor");
            dos.writeUTF("Excuted anounceClosePlaneDoor");
            message = dis.readUTF();
            msg("Response From Server: "+ message);

            msg("Ask Server Excuted arriveDestination");
            dos.writeUTF("Excuted arriveDestination");
            message = dis.readUTF();
            msg("Response From Server: "+ message);

            dis.close();
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis()-time) + "]" + clientName + " : " + m);
    }

}
