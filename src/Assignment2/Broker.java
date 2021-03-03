package Assignment2;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;

public class Broker extends Thread {
    private final String ticker;
    private static HashMap<String, ConcurrentLinkedQueue<Trade>> lists;
    private static Instant start;
    private static final Object allAdded = new Object();
    private static final Object outputting = new Object();

    public Broker(String ticker) {
        this.ticker = ticker;
    }

    @Override
    public void run() {
        synchronized (Broker.allAdded) {
        }
        Trade target;
        while ((target = lists.get(ticker).poll()) != null) {
            Instant now = Instant.now();
            Duration duration = Duration.between(start, now);
            String addorsell;
            if (target.numStocks > 0) {
                addorsell = "purchase";
            } else {
                addorsell = "sale";
                target.numStocks *= -1;
            }
            if (duration.getSeconds() < target.time) {
                try {
                    Thread.sleep(target.time * 1000L - duration.toMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            synchronized (outputting) {
                duration = Duration.between(start, Instant.now());
                System.out.printf("%02d:%02d:%02d:%03d Starting " + addorsell +
                                " of " + target.numStocks + " of " + target.ticker + "\n",
                        duration.toHours(),
                        duration.toMinutesPart(),
                        duration.toSecondsPart(),
                        duration.toMillisPart());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (outputting) {
                duration = Duration.between(start, Instant.now());
                System.out.printf("%02d:%02d:%02d:%03d Finished " + addorsell +
                                " of " + target.numStocks + " of " + target.ticker + "\n",
                        duration.toHours(),
                        duration.toMinutesPart(),
                        duration.toSecondsPart(),
                        duration.toMillisPart());
            }
        }
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        JsonParser jsonParser = new JsonParser(scan);
        ArrayList<Company> companies = jsonParser.ReadJson();
        CSVParser csvParser = new CSVParser(scan, companies);
        Broker.lists = csvParser.ReadCSV();
        ExecutorService poolExecutor = Executors.newCachedThreadPool();
        synchronized (Broker.allAdded) {
            for (Company company : companies) {
                for (int i = 0; i < Integer.parseInt(company.stockBrokers); i++) {
                    Broker broker = new Broker(company.ticker);
                    poolExecutor.execute(broker);
                }
            }
            Broker.start = Instant.now();
            poolExecutor.shutdown();
            System.out.println("Starting execution of program...");
        }
        Thread.yield();
        while (!poolExecutor.isTerminated()) {
            Thread.yield();
        }
        System.out.println("All trade completed!");
    }
}
