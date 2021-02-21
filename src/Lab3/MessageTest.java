package Lab3;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageTest {
    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            MessageQueue messageQueue = new MessageQueue();
            Messenger messenger = new Messenger(messageQueue);
            Subscriber subscriber = new Subscriber(messageQueue);
            ExecutorService executorService = Executors.newFixedThreadPool(2);
            executorService.execute(messenger);
            executorService.execute(subscriber);
            executorService.shutdown();
            while (!executorService.isTerminated()) {
                Thread.yield();
            }
            System.out.println();
        }
    }
}
