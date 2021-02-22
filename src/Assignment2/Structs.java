package Assignment2;

class Company {
    String name;
    String ticker;
    String startDate;
    String description;
    String exchangeCode;
    String stockBrokers;
}

class Trade {
    int time;
    String ticker;
    int numStocks;

    Trade(int time, String exchangeCode, int numStocks) {
        this.time = time;
        this.ticker = exchangeCode;
        this.numStocks = numStocks;
    }
}
