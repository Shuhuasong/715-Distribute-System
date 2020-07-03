package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import Server.MainServer;

/*public class SubClient implements Runnable {

    public static long time = System.currentTimeMillis();
    private static String address;
    private static int port;
    private static String clientName;
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    Server.MainServer server;



    public SubClient(String address, int port, String clientName) throws IOException {
        this.address = address;
        this.port = port;
        this.clientName = clientName;
    }

    public void run() {

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

            try{
                String message = "";
                while(!message.equals("End")){
                    message = dis.readUTF(); //??????
                    msg(message);
                    dos.writeUTF(clientName + " has received: " + message);
                }
            }catch (IOException exception){ }

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

 */


