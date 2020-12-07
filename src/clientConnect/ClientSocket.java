package clientConnect;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocket {
    public static Socket s = null;
    private static ClientSocket currentSocket = null;

    public ClientSocket(String ip, int port) throws IOException {
        s = new Socket(ip, port);
        currentSocket = this;
    }

    public synchronized static ClientSocket getClientSocket() throws IOException {
        if (currentSocket == null)
        {
        	currentSocket = null;
        }

        return currentSocket;
    }

    public void send(String message) throws IOException {
        PrintWriter out = new PrintWriter(s.getOutputStream());
        out.println(message);
        out.flush();
    }

    public void stop() {
        try
        {
            this.send("bye");
            this.s.close();
        }
        catch (IOException e) {}
    }
}
