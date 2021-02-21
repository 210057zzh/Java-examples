package Test;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class SumParallel {
    public static void main(String[] args) {
        long minNumber = 0;
        long maxNumber = 1_000_000_000;
        long numElements = 1_000_000;
        int numThreads = (int) ((maxNumber - minNumber) / numElements);
        long before = System.currentTimeMillis();

        ForkJoinPool pool = new ForkJoinPool();
        SumP sum[] = new SumP[numThreads];
        long start = minNumber;
        long end = numElements;
        for (int i = 0; i < numThreads; i++) {
            sum[i] = new SumP(start, end);
            start = end + 1;
            end = start + numElements - 1;
            pool.execute(sum[i]); // no return value, so we will join later
        }
        long num = 0;
        for (int i = 0; i < numThreads; i++) {
            num += sum[i].join();
        }
        long after = System.currentTimeMillis();
        System.out.println("time with parallelism = " + (after - before));
        System.out.print("SUM(" + minNumber + ".." + maxNumber + ") = ");
        System.out.println(num);
    }
}

class SumP extends RecursiveTask<Long> {
    private long minNum;
    private long maxNum;
    public static final long serialVersionUID = 1;

    public SumP(long minNum, long maxNum) {
        this.minNum = minNum;
        this.maxNum = maxNum;
    }

    protected Long compute() {
        long sum = 0;
        for (long i = minNum; i <= maxNum; i++) {
            sum += i;
        }
        return sum;
    }
}