package ga.initialize;

import greedy.NearestNeighbor;
import sa.Cooling;
import util.Path;
import util.Pick;
import util.TSP;

// for sa initializer
public class SA extends TSP {

    private final double T0;
    private double T;
    private final int numOfIteration;

    public SA(double T0, int numOfIteration) {
        if (T0 <= 0) {
            System.out.println("======INITIAL TEMPERATURE SHOULD BE BIGGER THAN 0======");
            System.exit(1);
        }

        this.T0 = T0;
        this.T = T0;
        this.numOfIteration = numOfIteration;
    }

    @Override
    public Path calculatePath(int startPoint) {
        NearestNeighbor nearestNeighbor = new NearestNeighbor();
        Path path = nearestNeighbor.calculatePath(startPoint);
        return calculatePath(path);
    }

    @Override
    public Path calculatePath(Path path) {
        int k = 0;
        Path minPath = path.deepCopy();
        while(T > 1) {
            for(int i = 0; i < numOfIteration; i++) {
                Path trialPath = minPath.deepCopy();
                int[] twoRandomNum = Pick.getTwoRandomIndex(1, numOfCities - 1);
                trialPath.twoOptSwap(twoRandomNum[0], twoRandomNum[1]);

                if(minPath.totalCost > trialPath.totalCost) {
                    minPath = trialPath.deepCopy();
                } else if (Math.random() < Math.pow(Math.E, (minPath.totalCost - trialPath.totalCost) / T)) {
                    minPath = trialPath.deepCopy();
                }
            }

            T = Cooling.exponentialMultiplicativeCooling(T0, 0.8, k);
            k++;
        }

        return minPath;
    }
}
