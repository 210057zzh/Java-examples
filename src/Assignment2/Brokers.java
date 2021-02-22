package Assignment2;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public class Brokers {
    public static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        JsonParser jsonParser = new JsonParser(scan);
        ArrayList<Company> companies = jsonParser.ReadJson();
        CSVParser csvParser = new CSVParser(scan, companies);
        HashMap<String, List<Trade>> lists = csvParser.ReadCSV();
        System.out.println("Starting execution of program...");
        return;
    }
}
