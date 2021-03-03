package Lab5;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class Parallel {
    public static void driver(ArrayList<Integer> arrayList, Integer target) {
        int i1 = 100;
        Find[] result = new Find[i1];
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Integer fourth = arrayList.size() / i1;
        Find.arrayList = arrayList;
        Find.target = target;
        Find.first = Instant.now();
        for (int i = 0; i < i1; i++) {
            result[i] = new Find(i * fourth, (i + 1) * fourth);
            forkJoinPool.execute(result[i]);
        }
        Integer sum = 0;
        for (int i = 0; i < i1; i++) {
            sum += result[i].join();
        }
        forkJoinPool.shutdown();
        if (sum == -i1) {
            System.out.println("Spent " + Duration.between(Find.first, Instant.now()).toMillis() + "  milliseconds");
            System.out.println(target + " is at " + -1);
        }
    }
}

class Find extends RecursiveTask<Integer> {
    private Integer start;
    private Integer end;
    public static Instant first;
    public static final long serialVersionUID = 1;
    public static ArrayList<Integer> arrayList;
    public static Integer target;

    public Find(Integer minNum, Integer maxNum) {
        this.start = minNum;
        this.end = maxNum;
    }

    @Override
    protected Integer compute() {
        for (Integer i = start; i < end; i++) {
            if (arrayList.get(i).equals(target)) {
                System.out.println("Spent " + Duration.between(first, Instant.now()).toMillis() + "  milliseconds");
                System.out.println(target + " is at " + i);
                return i;
            }
        }
        return -1;
    }
}
