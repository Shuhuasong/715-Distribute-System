package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class PassengerClient extends Thread {
    public static long time = System.currentTimeMillis();
    private static int port;
    private static String address;
    Socket socket;
    DataOutputStream dos;
    DataInputStream dis;
    String clientName;
    String message;
    public PassengerClient(String address, int port, String clientName){
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
                    //System.out.println("HostName: " + hostname + " port: " + port);
                    listen = true;
                } catch (ConnectException ignored) {
                }
            }
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            msg(" has Connected with server");

//            try{
//                String message = "";
//                while(!message.equals("End")){
//                    message = dis.readUTF();
//                    msg(message);
//                    dos.writeUTF(clientName + " has received: " + message);
//                }
//            }catch (IOException exception){ }
            msg("Ask Server Excuted ArriveAirport");
            dos.writeUTF("Excuted ArriveAirport");
            message = dis.readUTF();
            msg("Response From Server: "+ message);

            msg("Ask Server Excuted enjoyOnFlight");
            dos.writeUTF("Excuted ArraveAirport");
            message = dis.readUTF();
            msg("Response From Server: "+ message);

            msg("Ask Server Excuted disembarkPlane");
            dos.writeUTF("Excuted ArraveAirport");
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
