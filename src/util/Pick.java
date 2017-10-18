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

    /** randNums
     *  [st, fi]를 가지고 있는 배열에서 n개를 중복없이 추출
     *  정렬하여 배열로 리턴
     */
    public static int[] randNums(int st, int fi, int n) {
        ArrayList<Integer> samples = new ArrayList<>();
        for(int i = st; i <= fi; i++) samples.add(i);

        int [] ret = new int[n];
        int index;

        for(int i = 0; i < n; i++) {
            index = (int)Math.floor(Math.random() * samples.size());
            index = index % samples.size();
            ret[i] = samples.get(index);
            samples.remove(index);
        }

        Arrays.sort(ret);
        return ret;
    }

    /** randCities
     *  [1, MAX]중 n개 만큼 중복되지 않은 city ID를 랜덤으로 뽑습니다
     *  sort하여 결과 값 IDs를 배열로 리턴합니다
     * @param n : 뽑을 숫자의 개수 (length)
     * @param max : 최대 도시 번호
     * @return
     */
    public static int[] randCities(int n, int max) {
        return randNums(1, max, n);
    }
    public static int[] randCities(int n) {
        return randNums(1, Map.getInstance().getNumOfCities(), n);
    }
    /** randIdx
     *  [0, MAX-1]중 n개 만큼 중복되지 않은 city ID를 랜덤으로 뽑습니다
     *  sort하여 결과 값 IDs를 배열로 리턴합니다
     * @param n : 뽑을 숫자의 개수 (배열 길이)
     * @param length : idx의 길이, 추출하고자 하는 배열의 길이 (MAX)
     * @return
     */
    public static int[] randIdx(int n, int length) {
        return randNums(0, length-1, n);
    }
    public static int[] randIdx(int n) {
        return randNums(0, Map.getInstance().getNumOfCities() - 1, n);
    }
}
