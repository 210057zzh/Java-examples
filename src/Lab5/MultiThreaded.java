package Lab5;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreaded extends Thread {
    private static ArrayList<Integer> arrayList;
    private static Integer target;
    private static Integer result = -1;
    private Integer start;
    private Integer end;
    private static Instant first;

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            if (arrayList.get(i).equals(target)) {
                System.out.println("Spent " + Duration.between(first, Instant.now()).toMillis() + "  milliseconds");
                System.out.println(target + " is at " + i);
                result = i;
                return;
            }
        }
    }

    public MultiThreaded(Integer start, Integer end) {
        this.start = start;
        this.end = end;
    }

    public static Integer driver(ArrayList<Integer> arrayList, Integer target) {
        result = -1;
        MultiThreaded.arrayList = arrayList;
        MultiThreaded.target = target;
        int i1 = 100;
        Integer fourth = arrayList.size() / i1;
        ExecutorService executorService = Executors.newFixedThreadPool(i1);
        MultiThreaded.first = Instant.now();
        for (int i = 0; i < i1; i++) {
            MultiThreaded thread = new MultiThreaded(i * fourth, (i + 1) * fourth);
            executorService.execute(thread);
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            if (MultiThreaded.result != -1) {
                break;
            }
            Thread.yield();
        }
        if (MultiThreaded.result == -1) {
            System.out.println("Spent " + Duration.between(first, Instant.now()).toMillis() + "  milliseconds");
            System.out.println(target + " is at " + result);
        }
        return result;
    }
}
