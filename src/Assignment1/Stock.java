package Assignment1;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.*;

/**
 * @author davidz@usc.edu
 */
public class Stock {
    private ArrayList<Company> companies;
    private String file;
    protected Scanner scan = new Scanner(System.in);
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static class Data {
        private ArrayList<Company> data;
    }

    private static class Company {
        String name;
        String ticker;
        String startDate;
        String description;
        String exchangeCode;
    }


    public void ReadJson() {
        boolean valid = false;
        String name = null;
        while (!valid) {
            try {
                System.out.print("What is the name of the company file? ");
                name = scan.nextLine();
                BufferedReader json = new BufferedReader(new FileReader("src/Assignment1/" + name));
                JsonElement tree = JsonParser.parseReader(json);
                JsonArray a = (JsonArray) tree.getAsJsonObject().get("data");
                companies = gson.fromJson(a, new TypeToken<ArrayList<Company>>() { //https://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
                }.getType());
                for (Company company : companies) {
                    if (company.description == null || company.exchangeCode == null || company.name == null || company.startDate == null || company.ticker == null) {
                        throw new NoSuchFieldError();
                    }
                    String[] date = company.startDate.split("-");
                    if ((date.length != 3) || (date[0].length() != 4) || (date[1].length() != 2) || (date[2].length() != 2)) {
                        throw new InputMismatchException();
                    }
                    for (String number : date) {
                        Integer num = Integer.parseInt(number);
                    }
                    if (!(company.exchangeCode.toUpperCase(Locale.ROOT).equals("NYSE".toUpperCase(Locale.ROOT)) ||
                            company.exchangeCode.toUpperCase(Locale.ROOT).equals("NASDAQ".toUpperCase(Locale.ROOT)))) {
                        throw new IllegalArgumentException();
                    }

                }
                valid = true;
                file = name;
                System.out.println("File Read, ready to serve");
            } catch (java.io.FileNotFoundException e) {
                System.out.println(name + " Not Found, make sure the file is put under src/Assignment1/");
            } catch (com.google.gson.JsonSyntaxException e) {
                System.out.println(name + " is bad file, give me a new one please");
            } catch (NoSuchFieldError e) {
                System.out.println(name + " has missing field, give me a new one please");
            } catch (InputMismatchException e) {
                System.out.println("date format is incorrect, yyyy-mm-dd is used");
            } catch (java.lang.IllegalStateException e) {
                System.out.println("Not a Json Object");
            } catch (java.lang.NumberFormatException e) {
                System.out.println("Date must be number");
            } catch (IllegalArgumentException e) {
                System.out.println("There are only two exchanges, NASDAQ and NYSE, check file");
            }
        }
    }

    public void IndentOutWrap(String[] line) {
        int CharacterCounter = 0;
        System.out.print("\t");
        for (String word : line) {
            if (CharacterCounter + word.length() > 60) {
                System.out.println();
                System.out.print("\t");
                CharacterCounter = 0;
            }
            CharacterCounter += word.length();
            System.out.print(word + " ");
        }
        System.out.println();
    }

    public void StateMachine() {
        while (true) {
            try {
                System.out.println("    1) Display all public companies");
                System.out.println("    2) Search for a stock (by ticker)");
                System.out.println("    3) Search for all stocks on an exchange");
                System.out.println("    4) Add a new company/stocks");
                System.out.println("    5) Remove a company");
                System.out.println("    6) Sort companies");
                System.out.println("    7) Exit");
                System.out.print("What would you like to do? ");
                int option = scan.nextInt();
                scan.nextLine();
                System.out.println();
                if (option < 1 || option > 7) {
                    System.out.println("Not a valid Option, please look carefully again");
                } else if (option == 1) {
                    for (Company company : companies) {
                        System.out.println(company.name + ", symbol " + company.ticker + ", started on " + company.startDate + ", listed on " + company.exchangeCode + ",");
                        IndentOutWrap(company.description.split("\\s+"));
                        System.out.println();
                    }
                } else if (option == 2) {
                    boolean found = false;
                    Company goal = new Company();
                    while (!found) {
                        System.out.print("What is the name of the company you would like to search for?");
                        String target = scan.nextLine();
                        System.out.println();
                        for (Company company : companies) {
                            if (company.ticker.toUpperCase(Locale.ROOT).equals(target.toUpperCase(Locale.ROOT))) {
                                found = true;
                                goal = company;
                            }
                        }
                        if (found) {
                            System.out.println(goal.name + ", symbol " + goal.ticker + ", started on " + goal.startDate + ", listed on " + goal.exchangeCode + ".");
                            System.out.println();
                            break;
                        }
                        System.out.println(target + " could not be found.");
                    }
                } else if (option == 3) {
                    List<String> results = new ArrayList<>();
                    String exchange = "";
                    while (results.isEmpty()) {
                        System.out.print("What Stock Exchange would you like to search for? ");
                        exchange = scan.nextLine();
                        exchange = exchange.toUpperCase(Locale.ROOT);
                        for (Company company : companies) {
                            if (company.exchangeCode.equals(exchange)) {
                                results.add(company.name);
                            }
                        }
                        if (results.isEmpty()) {
                            System.out.println("No exchange named " + exchange + " found");
                        }
                    }
                    StringBuilder output = new StringBuilder();
                    for (int i = 0; i < results.size(); i++) {
                        if (i == results.size() - 1) {
                            output.append(results.get(i));
                        } else {
                            output.append(results.get(i)).append(" and ");
                        }
                    }
                    output.append(" found on the ").append(exchange).append(" exchange");
                    System.out.println(output);
                    System.out.println();
                } else if (option == 4) {
                    Company newcompany = new Company();
                    while (true) {
                        System.out.print("What is the name of the company you would like to add?");
                        String name = scan.nextLine();
                        Boolean unique = true;
                        for (Company company : companies) {
                            if (name.equals(company.name)) {
                                System.out.println("There is already an entry for " + name);
                                System.out.println();
                                unique = false;
                            }
                        }
                        if (!unique) {
                            continue;
                        }
                        System.out.print("What is the stock symbol of " + name + "?");
                        String ticker = scan.nextLine();
                        for (Company company : companies) {
                            if (company.ticker.toUpperCase(Locale.ROOT).equals(ticker.toUpperCase(Locale.ROOT))) {
                                System.out.println("Ticker already exists, please start over");
                                unique = false;
                                break;
                            }
                        }
                        if (!unique) {
                            continue;
                        }
                        System.out.print("What is the exchange where " + name + " is listed?");
                        String exchange = scan.nextLine();
                        if (!(exchange.toUpperCase(Locale.ROOT).equals("NYSE".toUpperCase(Locale.ROOT)) ||
                                exchange.toUpperCase(Locale.ROOT).equals("NASDAQ".toUpperCase(Locale.ROOT)))) {
                            System.out.println("There are only two exchanges, NASDAQ and NYSE, please try again.");
                            continue;
                        }
                        System.out.print("What is the start date of " + name + "?");
                        String date = scan.nextLine();
                        String[] word = date.split("-");
                        if ((word.length != 3) || (word[0].length() != 4) || (word[1].length() != 2) || (word[2].length() != 2)) {
                            System.out.println("date format incorrect");
                            continue;
                        }
                        try {
                            for (String number : word) {
                                Integer num = Integer.parseInt(number);
                            }
                        } catch (java.lang.NumberFormatException e) {
                            System.out.println("date format incorrect");
                            continue;
                        }
                        System.out.print("What is the description of " + name + "?");
                        String description = scan.nextLine();
                        newcompany.ticker = ticker.toUpperCase(Locale.ROOT);
                        newcompany.exchangeCode = exchange.toUpperCase(Locale.ROOT);
                        newcompany.name = name.toUpperCase(Locale.ROOT);
                        newcompany.startDate = date.toUpperCase(Locale.ROOT);
                        newcompany.description = description.toUpperCase(Locale.ROOT);
                        companies.add(newcompany);
                        System.out.println();
                        System.out.println("There is now a new entry for:");
                        System.out.println(newcompany.name + ", symbol " + newcompany.ticker + ", started on " + newcompany.startDate + ", listed on " + newcompany.exchangeCode + ",");
                        IndentOutWrap(newcompany.description.split("\\s+")); //stackoverflow
                        System.out.println();
                        break;
                    }
                } else if (option == 5) {
                    int i = 0;
                    for (Company company : companies) {
                        System.out.println("\t" + Integer.toString(i + 1) + ") " + company.name);
                        i++;
                    }
                    int target = 0;
                    while (true) {
                        try {
                            System.out.print("Which company would you like to remove?");
                            target = scan.nextInt() - 1;
                            if (target < 0 || target > companies.size() - 1) {
                                System.out.println("give a number in the options");
                                continue;
                            }
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("need a number, retry");
                            scan.nextLine();
                        }
                    }
                    String targetname = companies.get(target).name;
                    companies.remove(target);
                    System.out.println();
                    System.out.println(targetname + " is now removed.");
                } else if (option == 6) {
                    System.out.println("\t1) A to Z");
                    System.out.println("\t2) Z to A");
                    int selection = 0;
                    while (true) {
                        try {
                            System.out.println("\nHow would you like to sort by?");
                            selection = scan.nextInt();
                            scan.nextLine();
                            if (selection != 1 && selection != 2) {
                                System.out.println("choose a number in 1 and 2");
                                continue;
                            }
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("enter a number please");
                            scan.nextLine();
                        }
                    }
                    if (selection == 2) {
                        companies.sort(new Comparator<Company>() {
                            @Override
                            public int compare(Company o1, Company o2) {
                                return o2.name.compareToIgnoreCase(o1.name);
                            }
                        }); //stack overflow
                        System.out.println("Your companies are now sorted from in alphabetical order (Z-A).");
                    } else if (selection == 1) {
                        companies.sort(new Comparator<Company>() {
                            @Override
                            public int compare(Company o1, Company o2) {
                                return o1.name.compareToIgnoreCase(o2.name);
                            }
                        });
                        System.out.println("Your companies are now sorted from in alphabetical order (A-Z).");
                    }
                } else if (option == 7) {
                    break;
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Enter a Number Please");
                scan.nextLine();
            }
        }
    }


    public void Exit() {
        int selection = 0;
        while (true) {
            try {
                System.out.println("\t1) yes");
                System.out.println("\t2) no");
                System.out.println("Would You like to save your changes?");
                selection = scan.nextInt();
                scan.nextLine();
                if (selection == 1) {
                    System.out.println("Saving progress");
                    PrintWriter writer = new PrintWriter("src/Assignment1/" + file);
                    Data data = new Data();
                    data.data = companies;
                    gson.toJson(data, writer);
                    writer.flush();
                    writer.close();
                    System.out.println("Your edits have been saved to " + file);
                } else if (selection != 2) {
                    System.out.println("give me a valid number");
                    continue;
                }
                break;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Enter a valid number");
                scan.nextLine();
            }
        }
        System.out.println("Thank you for using my program!");
        scan.close();
    }

    public static void main(String[] args) {
        Stock stock = new Stock();
        stock.ReadJson();
        stock.StateMachine();
        stock.Exit();
    }
}
