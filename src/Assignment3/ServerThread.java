package Assignment3;

import Assignment2.Broker;
import Test.ChatRoom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 21005
 */
public class ServerThread extends Thread {
    public Lock assigning = new ReentrantLock();
    public static final Object allAdded = new Object();
    public static boolean finished = false;
    public static Instant start;
    private Integer traderid;
    private Integer balance;
    public LinkedList<Trade> trades = new LinkedList<>();
    private PrintWriter pw;

    public ServerThread(Socket s, Trader trader) {
        try {
            pw = new PrintWriter(s.getOutputStream());
            this.traderid = Integer.valueOf(trader.id);
            this.balance = Integer.valueOf(trader.balance);
        } catch (IOException ioe) {
            System.out.println("ioe in ServerThread constructor: " + ioe.getMessage());
        }
    }

    public void sendMessage(String message) throws IOException {
        pw.println(message);
        pw.flush();
    }

    private void sendStockMessage(boolean assign, Trade trade, Duration duration, String startOrFinished) throws IOException {
        String addorsell;
        if (trade.numStocks > 0) {
            addorsell = "purchase";
        } else {
            addorsell = "sale";
            trade.numStocks *= -1;
        }
        if (assign) {
            sendMessage(String.format("%02d:%02d:%02d:%03d assigned " + addorsell +
                            " of " + trade.numStocks + " of " + trade.ticker,
                    duration.toHours(),
                    duration.toMinutesPart(),
                    duration.toSecondsPart(),
                    duration.toMillisPart()));
            ;
        } else {
            sendMessage(String.format("%02d:%02d:%02d:%03d " + startOrFinished + ' ' + addorsell +
                            " of " + trade.numStocks + " of " + trade.ticker,
                    duration.toHours(),
                    duration.toMinutesPart(),
                    duration.toSecondsPart(),
                    duration.toMillisPart()));
        }
    }

    @Override
    public void run() {
        try {
            sendMessage("this is trader " + traderid);
            synchronized (allAdded) {
            }
            while (!finished) {
                assigning.lock();
                Duration duration = Duration.between(start, Instant.now());
                for (Trade trade : trades) {
                    sendStockMessage(true, trade, duration, "");
                }
                Trade target;
                while ((target = trades.pollFirst()) != null) {
                    Instant now = Instant.now();
                    duration = Duration.between(start, now);
                    if (duration.getSeconds() < target.time) {
                        try {
                            Thread.sleep(target.time * 1000L - duration.toMillis());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    duration = Duration.between(start, Instant.now());
                    sendStockMessage(false, target, duration, "start");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    duration = Duration.between(start, Instant.now());
                    sendStockMessage(false, target, duration, "finished");
                }
                assigning.unlock();
            }
        } catch (IOException ioe) {
            System.out.println("ioe in ServerThread.run(): " + ioe.getMessage());
        }
    }
}
