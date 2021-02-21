package Lab3;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Messenger extends Thread {
    public MessageQueue messageQueue;
    Random random;

    Messenger(MessageQueue messageQueue) {
        super();
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            messageQueue.addMessage(i + "s message");
            System.out.println(Util.getCurrentTime() + " Sent " + i + "s message");
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(0, 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
