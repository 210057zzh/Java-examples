package Lab5;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SingleThread {
    private ArrayList<Integer> array;
    private int target;


    Integer SearchandPrint(ArrayList<Integer> array, Integer target) {
        this.array = array;
        this.target = target;
        Instant start = Instant.now();
        int result = Search();
        System.out.println("Spent " + Duration.between(start, Instant.now()).toMillis() + "  milliseconds");
        System.out.println(target + " is at " + result);
        return result;
    }

    Integer Search() {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).equals(target)) {
                return i;
            }
        }
        return -1;
    }
}
