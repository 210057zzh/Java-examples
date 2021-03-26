package Assignment3;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 21005
 */
public class Server {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        CSVParser CSVParser = new CSVParser(scan);
        LinkedList<Trade> unable = new LinkedList<>();
        LinkedList<Trade> trades = CSVParser.ReadSchedule();
        ArrayList<Trader> traders = CSVParser.ReadTraders();
        ArrayList<ServerThread> threads = new ArrayList<>();
        try {
            int port = 3456;
            System.out.println("Binding to port " + port);
            ServerSocket ss = new ServerSocket(port);
            System.out.println("Bound to port " + port);
            System.out.println("Listening on port " + port);
            System.out.println("Waiting for traders");
            int traderidx = 0;
            while (threads.size() != traders.size()) {
                Socket s = ss.accept();
                System.out.println("Connection from: " + s.getInetAddress().getHostAddress());
                threads.add(new ServerThread(s, traders.get(traderidx)));
                if ((traders.size() - threads.size()) != 0) {
                    System.out.println("Waiting for " + (traders.size() - threads.size())
                            + " more traders");
                    for (ServerThread thread : threads) {
                        thread.sendMessage((traders.size() - threads.size())
                                + " more trader is needed before the service can begin. Waiting...");
                    }
                } else {
                    System.out.println("Starting service.");
                    for (ServerThread thread : threads) {
                        thread.sendMessage("All traders have arrived! Starting service.");
                    }
                }
                traderidx++;
            }
        } catch (IOException ioe) {
            System.out.println("ioe in Server constructor: " + ioe.getMessage());
            return;
        }
        ExecutorService poolExecutor = Executors.newFixedThreadPool(traders.size());
        synchronized (ServerThread.allAdded) {
            for (int i = 0; i < traders.size(); i++) {
                poolExecutor.execute(threads.get(i));
            }
            poolExecutor.shutdown();
            ServerThread.start = Instant.now();
        }

        Trade trade;
        while (!trades.isEmpty()) {
            twice:
            for (int i = 0; i < traders.size(); i++) {
                if (threads.get(i).assigning.tryLock()) {
                    Trader trader = traders.get(i);
                    while ((trade = trades.pollFirst()) != null) {
                        Duration duration = Duration.between(ServerThread.start, Instant.now());
                        if (duration.getSeconds() < trade.time) {
                            threads.get(i).assigning.unlock();
                            try {
                                Thread.sleep(trade.time * 1000L - duration.toMillis());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            trades.addFirst(trade);
                            break twice;
                        }
                        if (trade.numStocks > 0) {
                            if (trader.balance - trade.numStocks * trade.price < 0) {
                                if (!trade.tried.contains(i)) {
                                    trade.tried.add(i);
                                    if (trade.tried.size() == traders.size()) {
                                        unable.addLast(trade);
                                        break;
                                    }
                                }
                                trades.addFirst(trade);
                                break;
                            }
                            trader.balance -= trade.numStocks * trade.price;
                        }
                        threads.get(i).trades.addLast(trade);
                    }
                    threads.get(i).assigning.unlock();
                }
            }
        }
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ServerThread.finished = true;
        while (!poolExecutor.isTerminated()) {
            Thread.yield();
        }
        System.out.println("uncompleted trade:" + unable.size());
        for (Trade bad : unable) {
            System.out.printf("(%d,%s,%.2f,%d,%s)\n", bad.time, bad.ticker, bad.price, bad.numStocks, bad.date);
        }
        Duration duration = Duration.between(ServerThread.start, Instant.now());
        for (ServerThread thread : threads) {
            try {
                thread.sendMessage("uncompleted trade:" + unable.size());
                for (Trade bad : unable) {
                    thread.sendMessage(String.format("(%d,%s,%.2f,%d,%s)", bad.time, bad.ticker, bad.price, bad.numStocks, bad.date));
                }
                thread.sendMessage(String.format("[%02d:%02d:%02d:%03d] Processing complete!!",
                        duration.toHours(),
                        duration.toMinutesPart(),
                        duration.toSecondsPart(),
                        duration.toMillisPart()));
                thread.sendMessage(String.format("Total Profit Earned: %.2f", thread.gain));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.printf("[%02d:%02d:%02d:%03d] Processing complete!!",
                duration.toHours(),
                duration.toMinutesPart(),
                duration.toSecondsPart(),
                duration.toMillisPart());
        for (ServerThread thread : threads) {
            if (thread.socket != null) {
                try {
                    thread.socket.close();
                } catch (IOException e) {
                }
            }
        }

    }
}

class CSVParser {
    public Scanner scan;
    public String file;
    public LinkedList<Trade> trades = new LinkedList<>();
    public ArrayList<Trader> traders = new ArrayList<>();
    String first = "https://api.tiingo.com/tiingo/daily/";

    public CSVParser(Scanner scan) {
        this.scan = scan;
    }

    public LinkedList<Trade> ReadSchedule() {
        boolean valid = false;
        file = null;
        while (!valid) {
            try {
                System.out.print("What is the name of the schedule file? ");
                file = scan.nextLine();
                BufferedReader csv = new BufferedReader(new FileReader("TestFile/Assignment3/" + file));
                String strLine;
                //Read File Line By Line
                while ((strLine = csv.readLine()) != null) {
                    // Print the content on the console
                    Trade temp = (Trade) verifyTrade(strLine);
                    if (temp.numStocks != 0) {
                        trades.add(temp);
                    }
                }
                valid = true;
                csv.close();
            } catch (java.io.FileNotFoundException e) {
                System.out.println(file + " Not Found, make sure the file is put under TestFile/Assignment3/");
            } catch (java.lang.NumberFormatException e) {
                System.out.println("Time and num stocks must be good number");
            } catch (NoSuchFieldError e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (com.google.gson.JsonSyntaxException e) {
                System.out.println("Json not valid");
            }
        }
        System.out.println("Schedule Read, ready to serve");
        return trades;
    }

    public ArrayList<Trader> ReadTraders() {
        boolean valid = false;
        file = null;
        while (!valid) {
            try {
                System.out.print("What is the name of the traders file? ");
                file = scan.nextLine();
                BufferedReader csv = new BufferedReader(new FileReader("TestFile/Assignment3/" + file));
                String strLine;
                //Read File Line By Line
                while ((strLine = csv.readLine()) != null) {
                    // Print the content on the console
                    traders.add((Trader) verifyTrader(strLine));
                }
                if (traders.size() == 0) {
                    System.out.println("No trader found, bad file");
                    continue;
                }
                valid = true;
                csv.close();
            } catch (java.io.FileNotFoundException e) {
                System.out.println(file + " Not Found, make sure the file is put under TestFile/Assignment3/");
            } catch (java.lang.NumberFormatException e) {
                System.out.println("Trader id and balance must be good number");
            } catch (NoSuchFieldError e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println("IOException, please examine your system");
            }
        }
        System.out.println("Trader Read, ready to serve");
        return traders;
    }


    private Object verifyTrade(String line) throws NoSuchFieldError, NumberFormatException, IOException {
        String[] fields = line.split(",");
        if (fields.length != 4) {
            throw new NoSuchFieldError("one of the trade has bad format");
        }
        if (Integer.parseInt(fields[0]) < 0) {
            throw new NumberFormatException();
        }
        String[] date = fields[3].split("-");
        if ((date.length != 3) || (date[0].length() != 4) || (date[1].length() != 2) || (date[2].length() != 2)) {
            throw new InputMismatchException();
        }
        for (String number : date) {
            Integer.parseInt(number);
        }
        String tiingo = first + fields[1] + "/prices?startDate=" + fields[3] + "&endDate=" + fields[3] +
                "&token=ae3d790e817e50d08b86f7d4b467cc8d67554ec3";
        double price = 0;
        try {
            URL url = new URL(tiingo);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            Scanner scanner = new Scanner(url.openStream());

            //Write all the JSON data into a string using a scanner
            String inline = scanner.nextLine();
            if (inline.length() == 2) {
                throw new NoSuchFieldError("time \"" + fields[3] +
                        "\" is invalid, no info for ticker \"" + fields[1] + "\"");
            }
            //Close the scanner
            scanner.close();
            //Using the JSON simple library parse the string into a json object
            JsonObject obj = JsonParser.parseString(inline).getAsJsonArray().get(0).getAsJsonObject();
            //Get the required object from the above created object
            price = obj.get("close").getAsDouble();
        } catch (java.io.FileNotFoundException e) {
            throw new NoSuchFieldError("ticker \"" + fields[1] + "\" is invalid");
        } catch (IOException e) {
            throw new NoSuchFieldError("time \"" + fields[3] + "\" is invalid or no internet");
        }
        return new Trade(Integer.parseInt(fields[0]), fields[1], Integer.parseInt(fields[2]), fields[3], price);
    }

    public Object verifyTrader(String line) {
        String[] fields = line.split(",");
        if (fields.length != 2) {
            throw new NoSuchFieldError("one of the trader has bad format");
        }
        if (Integer.parseInt(fields[0]) < 0 && Double.parseDouble(fields[1]) <= 0) {
            throw new NumberFormatException();
        }
        return new Trader(Integer.parseInt(fields[0]), Double.parseDouble(fields[1]));
    }
}
