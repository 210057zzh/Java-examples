package Lab10;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

public class Server {
    // to do --> data structure to hold server threads 
    ArrayList<ServerThread> serverThreads = new ArrayList<>();

    public Server(int port) {
        // to do --> implement your constructor
        try {
            System.out.println("Binding to port " + port);
            ServerSocket ss = new ServerSocket(port);
            System.out.println("Bound to port " + port);
            while (true) {
                Socket s = ss.accept();
                System.out.println("Connection from: " + s.getInetAddress());
                ServerThread st = new ServerThread(s);
                serverThreads.add(st);
            }
        } catch (IOException ioe) {
            System.out.println("ioe in ChatRoom constructor: " + ioe.getMessage());
        }


    }

    public static void main(String[] args) {
        // to do --> implement your main()
        new Server(6789);
    }
}
