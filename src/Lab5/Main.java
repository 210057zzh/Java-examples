package Lab5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author 21005
 */
public class Main {
    public static void main(String[] args) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < 100_000_000; i++) {
            arrayList.add(i);
        }
        Collections.shuffle(arrayList);
        Integer rnd = ThreadLocalRandom.current().nextInt(0, 1_000_000);
        Integer twornd = -1 * ThreadLocalRandom.current().nextInt(1, 100_000_000);
        SingleThread singleThread = new SingleThread();
        System.out.println("Single");
        singleThread.SearchandPrint(arrayList, rnd);
        singleThread.SearchandPrint(arrayList, twornd);
        System.out.println("Multi");
        MultiThreaded.driver(arrayList, rnd);
        MultiThreaded.driver(arrayList, twornd);
        System.out.println("Parallel");
        Parallel.driver(arrayList, rnd);
        Parallel.driver(arrayList, twornd);
    }
}
