package Assignment2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CSVParser {
    public Scanner scan;
    public String file;
    public HashMap<String, ConcurrentLinkedQueue<Trade>> hashMap = new HashMap<>();
    public ArrayList<Company> companies;

    public CSVParser(Scanner scan, ArrayList<Company> companies) {
        this.scan = scan;
        this.companies = companies;
    }

    public HashMap<String, ConcurrentLinkedQueue<Trade>> ReadCSV() {
        for (Company company : companies) {
            hashMap.put(company.ticker, new ConcurrentLinkedQueue<Trade>());
        }
        boolean valid = false;
        file = null;
        while (!valid) {
            try {
                System.out.print("What is the name of the CSV file? ");
                file = scan.nextLine();
                BufferedReader csv = new BufferedReader(new FileReader("src/Assignment2/" + file));
                String strLine;
                //Read File Line By Line
                while ((strLine = csv.readLine()) != null) {
                    // Print the content on the console
                    Trade temp = (Trade) verifyTrade(strLine);
                    if (temp.numStocks != 0) {
                        hashMap.get(temp.ticker).add(temp);
                    }
                }
                valid = true;
            } catch (java.io.FileNotFoundException e) {
                System.out.println(file + " Not Found, make sure the file is put under src/Assignment2/");
            } catch (NoSuchFieldError e) {
                System.out.println(e.getMessage());
            } catch (java.lang.NumberFormatException e) {
                System.out.println("Time and num stocks must be good number");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("CSV Read, ready to serve");
        return hashMap;
    }

    private Object verifyTrade(String line) throws NoSuchFieldError, NumberFormatException {
        String[] fields = line.split(",");
        if (fields.length != 3) {
            throw new NoSuchFieldError("one of the trade has bad format");
        }
        if (Integer.parseInt(fields[0]) < 0) {
            throw new NumberFormatException();
        }
        boolean found = false;
        for (Company company : companies) {
            if (company.ticker.equals(fields[1])) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new NoSuchFieldError("No matching ticker for " + fields[1]);
        }
        return new Trade(Integer.parseInt(fields[0]), fields[1], Integer.parseInt(fields[2]));
    }
}


