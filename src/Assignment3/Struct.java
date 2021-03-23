package Assignment3;

class Trade {
    int time;
    String ticker;
    int numStocks;
    String date;
    double price;

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
    int balance;

    Trader(int id, int balance) {
        this.id = id;
        this.balance = balance;
    }
}
