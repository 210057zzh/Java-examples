package Assignment3;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.CountDownLatch;


/**
 * @author 21005
 */
public class ServerThread extends Thread {
    public static final Object allAdded = new Object();
    public static boolean finished = false;
    public static Instant start;
    public Lock assigning = new ReentrantLock();
    public Condition assigned = assigning.newCondition();
    public LinkedList<Trade> trades = new LinkedList<>();
    double balance;
    double gain = 0;
    private Integer traderid;
    private PrintWriter pw;
    public Socket socket;
    static CountDownLatch count;
    boolean waiting = false;

    public ServerThread(Socket s, Trader trader) {
        try {
            socket = s;
            pw = new PrintWriter(s.getOutputStream());
            this.traderid = Integer.valueOf(trader.id);
            this.balance = Double.valueOf(trader.balance);
        } catch (IOException ioe) {
            System.out.println("ioe in ServerThread constructor: " + ioe.getMessage());
        }
    }

    public void sendMessage(String message) throws IOException {
        pw.println(message);
        pw.flush();
    }

    private void sendStockMessage(boolean assign, Trade trade, Duration duration, String startOrFinished, int sf) throws IOException {
        String addorsell;
        boolean in;
        if (trade.numStocks > 0) {
            addorsell = "purchase";
            in = true;
        } else {
            addorsell = "sale";
            trade.numStocks *= -1;
            in = false;
        }
        if (assign) {
            sendMessage(String.format("[%02d:%02d:%02d:%03d] assigned " + addorsell +
                            " of " + trade.numStocks + " of " + trade.ticker,
                    duration.toHours(),
                    duration.toMinutesPart(),
                    duration.toSecondsPart(),
                    duration.toMillisPart()));
            if (in) {
                sendMessage(String.format("Total cost estimate = $%.2f*%d = %.2f",
                        trade.price, trade.numStocks, trade.price * trade.numStocks));
            } else {
                sendMessage(String.format("Total gain estimate = $%.2f*%d = %.2f",
                        trade.price, trade.numStocks, trade.price * trade.numStocks));
            }
        } else {
            sendMessage(String.format("[%02d:%02d:%02d:%03d] " + startOrFinished + ' ' + addorsell +
                            " of " + trade.numStocks + " of " + trade.ticker,
                    duration.toHours(),
                    duration.toMinutesPart(),
                    duration.toSecondsPart(),
                    duration.toMillisPart()));
            if (in) {
                if (sf == 1) {
                    sendMessage(String.format("Total cost = $%.2f*%d = $%.2f",
                            trade.price, trade.numStocks, trade.price * trade.numStocks));
                } else if (sf == 0) {
                    sendMessage(String.format("Remaining Balance = $%.2f-$%.2f = $%.2f",
                            balance, trade.price * trade.numStocks, balance - trade.price * trade.numStocks));
                    balance -= (trade.price * trade.numStocks);
                }
            } else {
                if (sf == 1) {
                    sendMessage(String.format("Total gain = $%.2f*%d = $%.2f",
                            trade.price, trade.numStocks, trade.price * trade.numStocks));
                } else if (sf == 0) {
                    sendMessage(String.format("Profit earned till now = $%.2f+$%.2f = $%.2f",
                            gain, trade.price * trade.numStocks, gain + trade.price * trade.numStocks));
                    gain += (trade.price * trade.numStocks);
                }
            }
        }
        if (!in) {
            trade.numStocks *= -1;
        }
    }

    @Override
    public void run() {
        try {
            sendMessage("this is trader " + traderid);
            synchronized (allAdded) {
            }
            while (true) {
                assigning.lock();
                if (trades.isEmpty()) {
                    try {
                        waiting = true;
                        assigned.await();
                        waiting = false;
                        if (finished) {
                            return;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (finished) {
                    return;
                }
                Duration duration = Duration.between(start, Instant.now());
                for (Trade trade : trades) {
                    sendStockMessage(true, trade, duration, "", -1);
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
                    sendStockMessage(false, target, duration, "start", 1);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    duration = Duration.between(start, Instant.now());
                    sendStockMessage(false, target, duration, "finished", 0);
                    count.countDown();
                }
                assigning.unlock();
            }
        } catch (IOException ioe) {
            System.out.println("ioe in ServerThread.run(): " + ioe.getMessage());
        }
    }
}