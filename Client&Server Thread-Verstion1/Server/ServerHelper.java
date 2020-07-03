package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerHelper {
    private DataOutputStream dos;
    private DataInputStream dis;
    private String name;

    public ServerHelper(DataOutputStream dos, DataInputStream dis, String name) {
        this.dos = dos;
        this.dis = dis;
        this.name = name;
    }

    public void sendMessageToClient(String message) throws IOException {
        this.dos.writeUTF(message);
    }

    public void readMessageFromClient() throws IOException {
        String message = this.dis.readUTF();
        msg(message);
    }

    public void closeAllConnection() throws IOException {
        dis.close();
        dos.close();
    }


    public void msg(String m) {
        System.out.println(name + m);
    }


}
