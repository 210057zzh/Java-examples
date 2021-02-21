package Lab4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SleepingBarber<bool> extends Thread {

    static private int maxSeats;
    static private int totalCustomers;
    static private List<Customer> customersWaiting;
    private Lock barberLock;
    private Condition sleepingCondition;
    static private boolean moreCustomers;
    private String name;
    public boolean sleeping = false;

    public SleepingBarber(String name) {
        maxSeats = 3;
        totalCustomers = 10;
        moreCustomers = true;
        customersWaiting = Collections.synchronizedList(new ArrayList<Customer>());
        barberLock = new ReentrantLock();
        sleepingCondition = barberLock.newCondition();
        this.name = name;
        this.start();
    }

    public static synchronized boolean addCustomerToWaiting(Customer customer) {
        if (customersWaiting.size() == maxSeats) {
            return false;
        }
        Util.printMessage("Customer " + customer.getCustomerName() + " is waiting");
        customersWaiting.add(customer);
        String customersString = "";
        for (int i = 0; i < customersWaiting.size(); i++) {
            customersString += customersWaiting.get(i).getCustomerName();
            if (i < customersWaiting.size() - 1) {
                customersString += ",";
            }
        }
        Util.printMessage("Customers currently waiting: " + customersString);
        return true;
    }

    public void wakeUpBarber() {
        barberLock.lock();
        try {
            sleepingCondition.signal();
        } finally {
            barberLock.unlock();
        }
    }

    @Override
    public void run() {
        while (moreCustomers) {
            while (!customersWaiting.isEmpty()) {
                Customer customer = null;
                synchronized (SleepingBarber.class) {
                    customer = customersWaiting.remove(0);
                }
                customer.startingHaircut();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    System.out.println("ie cutting customer's hair" + ie.getMessage());
                }
                customer.finishingHaircut();
                Util.printMessage(this.name + ": Checking for more customers...");
            }
            barberLock.lock();
            try {
                Util.printMessage(this.name + ": No customers, so time to sleep... ");
                this.sleeping = true;
                sleepingCondition.await();
                Util.printMessage(this.name + ": Someone woke me up!");
                this.sleeping = false;
            } catch (InterruptedException ie) {
                System.out.println("ie while sleeping: " + ie.getMessage());
            } finally {
                barberLock.unlock();
            }
        }
        Util.printMessage(this.name + ": All done for today!  Time to go home!");
    }

    public static void main(String[] args) {
        SleepingBarber sb1 = new SleepingBarber("barber 1");
        SleepingBarber sb2 = new SleepingBarber("barber 2");
        ExecutorService executors = Executors.newCachedThreadPool();
        for (int i = 0; i < sb2.totalCustomers; i++) {
            Customer customer = new Customer(i, sb1, sb2);
            executors.execute(customer);
            try {
                Random rand = new Random();
                int timeBetweenCustomers = rand.nextInt(2000);
                Thread.sleep(timeBetweenCustomers);
            } catch (InterruptedException ie) {
                System.out.println("ie in customers entering: " + ie.getMessage());
            }
        }
        executors.shutdown();
        while (!executors.isTerminated()) {
            Thread.yield();
        }
        Util.printMessage("No more customers coming today...");
        moreCustomers = false;
        sb1.wakeUpBarber();
        sb2.wakeUpBarber();
    }
}
