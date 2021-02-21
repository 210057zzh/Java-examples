package Assignment2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class JsonParser {
    public ArrayList<Company> companies;
    public String file;
    public Scanner scan;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static class Data {
        private ArrayList<Company> data;
    }

    public JsonParser(Scanner scan) {
        this.scan = scan;
    }

    public ArrayList<Company> ReadJson() {
        boolean valid = false;
        file = null;
        while (!valid) {
            try {
                System.out.print("What is the name of the company file? ");
                file = scan.nextLine();
                BufferedReader json = new BufferedReader(new FileReader("src/Assignment2/" + file));
                JsonElement tree = com.google.gson.JsonParser.parseReader(json);
                JsonArray a = (JsonArray) tree.getAsJsonObject().get("data");
                companies = gson.fromJson(a, new TypeToken<ArrayList<Company>>() { //https://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
                }.getType());
                for (Company company : companies) {
                    if (company.description == null || company.exchangeCode == null || company.name == null || company.startDate == null || company.ticker == null || company.stockBrokers == null) {
                        throw new NoSuchFieldError();
                    }
                    String[] date = company.startDate.split("-");
                    if ((date.length != 3) || (date[0].length() != 4) || (date[1].length() != 2) || (date[2].length() != 2)) {
                        throw new InputMismatchException();
                    }
                    for (String number : date) {
                        Integer num = Integer.parseInt(number);
                    }

                    if (Integer.parseInt(company.stockBrokers) < 1) {
                        throw new java.lang.NumberFormatException();
                    }
                    if (!(company.exchangeCode.toUpperCase(Locale.ROOT).equals("NYSE".toUpperCase(Locale.ROOT)) ||
                            company.exchangeCode.toUpperCase(Locale.ROOT).equals("NASDAQ".toUpperCase(Locale.ROOT)))) {
                        throw new IllegalArgumentException();
                    }
                }
                valid = true;
                file = file;
                System.out.println("Json Read");
            } catch (java.io.FileNotFoundException e) {
                System.out.println(file + " Not Found, make sure the file is put under src/Assignment2/");
            } catch (com.google.gson.JsonSyntaxException e) {
                System.out.println(file + " is bad file, give me a new one please");
            } catch (NoSuchFieldError e) {
                System.out.println(file + " has missing field, give me a new one please");
            } catch (InputMismatchException e) {
                System.out.println("date format is incorrect, yyyy-mm-dd is used");
            } catch (java.lang.IllegalStateException e) {
                System.out.println("Not a Json Object");
            } catch (java.lang.NumberFormatException e) {
                System.out.println("Date must be number or Stock trader number is off");
            } catch (IllegalArgumentException e) {
                System.out.println("There are only two exchanges, NASDAQ and NYSE, check file");
            }
        }
        return companies;
    }
}
