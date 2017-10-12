package util;

import java.util.ArrayList;
import java.util.Arrays;

public class Pick {
    public static int[] getTwoRandomIndex(int lower, int higher) {
        int first = getRandomInt(lower, higher);
        int second = getRandomInt(lower, higher);

        while(first == second) {
            second = getRandomInt(lower, higher);
        }

        int[] ret = new int[2];
        ret[0] = Math.min(first, second);
        ret[1] = Math.max(first, second);

        return ret;
    }

    public static int getRandomInt(int lower, int higher) {
        int interval = higher - lower;
        return (int) (Math.random() * (interval + 1)) + lower;
    }


    public static int[] randNums(int n, int max) {
        ArrayList<Integer> samples = new ArrayList<>();
        for(int i = 1; i <= max; i++) samples.add(i); // 0 ~ max-1

        int [] ret = new int[n];
        int index;

        for(int i = 0; i < n; i++) {
            index = (int)Math.floor(Math.random()*(samples.size()));
            index = index % samples.size();
            ret[i] = samples.get(index);
            samples.remove(index);
        }

        Arrays.sort(ret);
        return ret;
    }
}
