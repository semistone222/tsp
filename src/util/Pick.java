package util;

public class Pick {
    public static int[] getTwoRandomNumber(int min, int max) {
        int first = (int) (Math.random() * max) + min;
        int second = (int) (Math.random() * max) + min;

        while(first == second) {
            second = (int) (Math.random() * max) + min;
        }

        int[] ret = new int[2];
        ret[0] = Math.min(first, second);
        ret[1] = Math.max(first, second);

        return ret;
    }
}
