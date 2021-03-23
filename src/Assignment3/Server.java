package Assignment3;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Server {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        ScheduleParser scheduleParser = new ScheduleParser(scan);
        ConcurrentLinkedQueue<Assignment3.Trade> Queue = scheduleParser.ReadSchedule();
        return;
    }
}

class ScheduleParser {
    public Scanner scan;
    public String file;
    public ConcurrentLinkedQueue<Assignment3.Trade> Queue = new ConcurrentLinkedQueue<>();
    String first = "https://api.tiingo.com/tiingo/daily/";

    public ScheduleParser(Scanner scan) {
        this.scan = scan;
    }

    public ConcurrentLinkedQueue<Assignment3.Trade> ReadSchedule() {
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
                    Assignment3.Trade temp = (Assignment3.Trade) verifyTrade(strLine);
                    if (temp.numStocks != 0) {
                        Queue.add(temp);
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
        System.out.println("CSV Read, ready to serve");
        return Queue;
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
            Integer num = Integer.parseInt(number);
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
                throw new NoSuchFieldError("time \"" + fields[3] + "\" is invalid, no info for ticker \"" + fields[1] + "\"");
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
            throw new NoSuchFieldError("time \"" + fields[3] + "\" is invalid");
        }

        return new Trade(Integer.parseInt(fields[0]), fields[1], Integer.parseInt(fields[2]), fields[3], price);
    }
}
