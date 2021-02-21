package Lab3;

import java.util.concurrent.ThreadLocalRandom;

public class Subscriber extends Thread {
    MessageQueue messageQueue;

    Subscriber(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        super.run();
        for (int i = 0; i < 20; i++) {
            String buffer = messageQueue.getMessage();
            if ("".equals(buffer)) {
                i--;
                System.out.println(Util.getCurrentTime() + " No Message yet");
            } else {
                System.out.println(Util.getCurrentTime() + " Received " + buffer);
            }
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(0, 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
