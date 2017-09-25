package util;

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
}
