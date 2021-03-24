package Assignment3;

import java.util.HashSet;

class Trade {
    int time;
    String ticker;
    int numStocks;
    String date;
    double price;
    HashSet<Integer> tried = new HashSet<>();

    Trade(int time, String exchangeCode, int numStocks, String date, double price) {
        this.time = time;
        this.ticker = exchangeCode;
        this.numStocks = numStocks;
        this.date = date;
        this.price = price;
    }
}

class Trader {
    int id;
    double balance;

    Trader(int id, double balance) {
        this.id = id;
        this.balance = balance;
    }
}
