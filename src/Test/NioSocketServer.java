package Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class NioSocketServer {
    public NioSocketServer() {
        try {
            // Create an AsynchronousServerSocketChannel that will listen on port 5000
            final AsynchronousServerSocketChannel listener =
                    AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(5000));

            // Listen for a new request, set up completion handler
            listener.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
                // anonymous inner class
                @Override
                public void completed(AsynchronousSocketChannel ch, Void att) {
                    // Accept the next connection
                    listener.accept(null, this);

                    // Greet the client
                    ch.write(ByteBuffer.wrap("Hello, I am Echo Server 2020, let's have an engaging conversation!\n".getBytes()));

                    // Allocate a byte buffer (4K) to read from the client
                    ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
                    try {
                        // Read the first line, wait for 60 sec. then throw exception
                        
                        int bytesRead = ch.read(byteBuffer).get(60, TimeUnit.SECONDS);

                        boolean running = true;
                        while (bytesRead != -1 && running) {
                            System.out.println("bytes read: " + bytesRead);

                            // Make sure that we have data to read
                            if (byteBuffer.position() > 2) {
                                // Make the buffer ready to read, 
                                // and prepare channel to write
                                byteBuffer.flip();

                                // Convert the buffer into a line
                                byte[] lineBytes = new byte[bytesRead];
                                byteBuffer.get(lineBytes, 0, bytesRead); // read the bytes
                                String line = new String(lineBytes);

                                // Debug
                                System.out.println("Message: " + line);

                                // Echo back to the caller - write to the channel
                                ch.write(ByteBuffer.wrap(line.getBytes()));

                                // Prepare buffer for reading
                                byteBuffer.clear();

                                // Read the next line
                                bytesRead = ch.read(byteBuffer).get(60, TimeUnit.SECONDS);
                            } else {
                                // An empty line signifies the end of the conversation in our protocol
                                running = false;
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        // The user exceeded the 60 second timeout, so close the connection
                        ch.write(ByteBuffer.wrap("Good Bye\n".getBytes()));
                        System.out.println("Connection timed out, closing connection");
                    }

                    System.out.println("End of conversation");
                    try {
                        // Close the connection if we need to
                        if (ch.isOpen()) {
                            ch.close();
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

                @Override
                public void failed(Throwable exc, Void att) {
                    ///...
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        NioSocketServer server = new NioSocketServer();
        try {
            Thread.sleep(120000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
