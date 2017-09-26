package sa;

import greedy.NearestNeighbor;
import util.Path;
import util.Pick;
import util.TSP;
import util.Timer;

public class SASearch extends TSP {

    private final double T0;
    private double T;
    private int numOfIteration;
    private Timer timer;

    public SASearch(double T0, int numOfIteration) {
        if (T0 <= 0) {
            System.out.println("======INITIAL TEMPERATURE SHOULD BE BIGGER THAN 0======");
            System.exit(1);
        }

        this.T0 = T0;
        this.T = T0;
        this.numOfIteration = numOfIteration;
        this.timer = new Timer(Timer.FIRST_DEMO_LIMIT_SEC);
    }

    @Override
    public Path calculatePath(int startPoint) {
        this.timer.start(System.currentTimeMillis());
        NearestNeighbor nearestNeighbor = new NearestNeighbor();
        Path path = nearestNeighbor.calculatePath(startPoint);
        return calculatePath(path);
    }

    @Override
    public Path calculatePath(Path path) {
        int k = 0;
        Path minPath = path.deepCopy();
        while(!timer.isTimeGone() && T > 0.001) {
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
            // TODO : decide cooling function and parameters
            T = Cooling.quadraticMultiplicativeCooling(T0, 0.9, k);
            k++;
            // delete this, just for debug
            timer.printExecutionTime();
            System.out.println("T,k,cost = (" + T + "," + k + "," + minPath.totalCost + ")");
        }

        return minPath;
    }
}
