package Assignment3;

import Test.ChatClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author 21005
 */
public class Client {

    private BufferedReader br;
    private PrintWriter pw;

    public Client(String hostname, int port) {
        try {
            System.out.println("Trying to connect to " + hostname + ":" + port);
            Socket s = new Socket(hostname, port);
            System.out.println("Connected to " + hostname + ":" + port);
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            while (true) {
                String line = br.readLine();
                System.out.println(line);
            }
        } catch (IOException ioe) {
            System.out.println("Server Stopped");
        }
    }

    public static void main(String[] args) {
        Client c = new Client("localhost", 9999);
    }
}
