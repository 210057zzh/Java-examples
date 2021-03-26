package Assignment3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author 21005
 */
public class Client {

    private BufferedReader br;
    private final Scanner scanner = new Scanner(System.in);

    public Client() {
        boolean started = false;
        try {
            System.out.println("Welcome to SalStocks v2.0!");
            System.out.print("Enter the server hostname: ");
            String hostname = scanner.nextLine().strip();
            System.out.print("Enter the server port: ");
            Integer port = scanner.nextInt();
            System.out.println("Trying to connect to " + hostname + ":" + port);
            Socket s = new Socket(hostname, port);
            System.out.println("Connected to " + hostname + ":" + port);
            started = true;
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    throw new IOException();
                }
                System.out.println(line);
            }
        } catch (IOException ioe) {
            if (started) {
                System.out.println("Server Stopped");
            } else {
                System.out.println("Network error, server not found");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entry Incorrect");
        }
    }

    public static void main(String[] args) {
        Client c = new Client();
    }
}
