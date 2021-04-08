package Lab10;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


public class ServerThread extends Thread {
    // to do --> private variables for the server thread
    BufferedReader bufferedReader;
    PrintStream printStream;
    Socket socket;

    public ServerThread(Socket s) {
        try {
            // to do --> store them somewhere, you will need them later
            bufferedReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            printStream = new PrintStream(new BufferedOutputStream(s.getOutputStream()));
            socket = s;

            // to do --> complete the implementation for the constructor
            this.start();

        } catch (IOException ioe) {
            System.out.println("ioe in ServerThread constructor: " + ioe.getMessage());
        }
    }

    // to do --> what method are we missing? Implement the missing method


    @Override
    public void run() {
        try {
            StringBuilder requestBuilder = new StringBuilder();
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                requestBuilder.append(s + "\r\n");
                if (s.isEmpty()) {
                    break;
                }
            }
            String request = requestBuilder.toString();
            String[] requestsLines = request.split("\r\n");
            String[] requestLine = requestsLines[0].split(" ");
            String path = requestLine[1];
            Path filePath = getFilePath("src/Lab10" + path);
            if (Files.exists(filePath)) {
                // file exist
                String contentType = guessContentType(filePath);
                sendResponse(socket, "200 OK", contentType, Files.readAllBytes(filePath));
            } else {
                // 404
                byte[] notFoundContent = "".getBytes();
                sendResponse(socket, "404 Not Found", "text/html", notFoundContent);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }

    }

    private static String guessContentType(Path filePath) throws IOException {
        return Files.probeContentType(filePath);
    }

    private static Path getFilePath(String path) {
        return Paths.get(path);
    }

    private static void sendResponse(Socket client, String status, String contentType, byte[] content) throws IOException {
        OutputStream clientOutput = client.getOutputStream();
        clientOutput.write(("HTTP/1.1 " + status + "\r\n").getBytes());
        if (!"404 Not Found".equals(status)) {
            clientOutput.write(("ContentType: " + contentType + "\r\n").getBytes());
            clientOutput.write("\r\n".getBytes());
            clientOutput.write(content);
            clientOutput.write("\r\n\r\n".getBytes());
        }
        clientOutput.flush();
        client.close();
    }
}
