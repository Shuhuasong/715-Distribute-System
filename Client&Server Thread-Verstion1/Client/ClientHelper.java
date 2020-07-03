package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

public class ClientHelper implements Runnable {
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private static long time = System.currentTimeMillis();
    private String name;
    private String hostname;
    private int port;

    public ClientHelper(int port, String hostname, String name) throws IOException {
        this.name = name;
        this.port = port;
        this.hostname = hostname;
    }

    public String getName() {
        return name;
    }

    @Override
    public void run() {
        //send to server
        try {
            establishConnectionToServer(hostname, port);
//            dos.writeUTF("hello from " + getName());
            String message = "";
            while (!message.equals("exit")) {
                message = dis.readUTF();
                msg(message);
                //write confirmation
                dos.writeUTF(getName() + " confirmed receving: " + message);
            }

            dis.close();
            dos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void establishConnectionToServer(String hostname, int port) throws IOException {
        boolean connected = false;
        while (!connected) {
            try {
                socket = new Socket(hostname, port);
                connected = true;
            } catch (ConnectException ignored) {
            }
        }
        dos = new DataOutputStream(socket.getOutputStream());
        dis = new DataInputStream(socket.getInputStream());
    }

    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "] " + this.getName() + ": " + m);
    }
}
