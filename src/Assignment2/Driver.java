package Assignment2;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public class Driver extends Thread {
    public static Scanner scan = new Scanner(System.in);
    private JsonParser jsonParser;
    private CSVParser csvParser;
    private ArrayList<Company> companies;
    private ArrayList<Trade> trades;
    private ConcurrentHashMap<String, Semaphore> locks = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        Driver driver = new Driver();
        driver.jsonParser = new JsonParser(scan);
        driver.companies = driver.jsonParser.ReadJson();
        driver.csvParser = new CSVParser(scan, driver.companies);
        driver.trades = driver.csvParser.ReadCSV();
        for (Company company : driver.companies) {
            driver.locks.put(company.ticker, new Semaphore(Integer.parseInt(company.stockBrokers)));
        }
        
        return;
    }

    @Override
    public void run() {

    }
}
