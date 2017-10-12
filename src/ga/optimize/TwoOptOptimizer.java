package ga.optimize;

import util.Map;
import util.Path;
import util.Pick;

public class TwoOptOptimizer implements Optimizer {

    private final int numOfIterations;
    private final int numOfCities;

    public TwoOptOptimizer(int numOfIterations) {
        this.numOfIterations = numOfIterations;
        this.numOfCities = Map.getInstance().getNumOfCities();
    }

    @Override
    public Path optimize(Path path) {
        Path bestPath = path.deepCopy();
        int currentIteration = 0;
        while(currentIteration < numOfIterations) {
            Path trialPath = bestPath.deepCopy();
            int[] twoRandomNum = Pick.getTwoRandomIndex(1, numOfCities - 1);
            trialPath.twoOptSwap(twoRandomNum[0], twoRandomNum[1]);
            if(bestPath.totalCost > trialPath.totalCost) {
                bestPath = trialPath.deepCopy();
            }
            currentIteration++;
        }
        return bestPath;
    }
}
