package ga.mutate;

import util.Map;
import util.Path;
import util.Pick;

import java.util.Random;

public class SwapMutation implements Mutation {

    public double threshold;
    private final int numOfCities;

    /**
     *
     * @param threshold should be between 0.001 and 0.01
     */
    public SwapMutation(double threshold) {
        this.threshold = threshold;
        this.numOfCities = Map.getInstance().getNumOfCities();
    }

    @Override
    public void mutate(Path path) {
        Random random = new Random();
        if(threshold > random.nextDouble()) {
            int[] twoRandomNum = Pick.getTwoRandomIndex(1, numOfCities - 1);
            path.twoOptSwap(twoRandomNum[0], twoRandomNum[1]);
        }
    }
}
