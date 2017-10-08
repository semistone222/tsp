package our;

import greedy.NearestNeighbor;
import javafx.util.Pair;
import sa.Cooling;
import util.Path;
import util.Pick;
import util.TSP;
import util.Timer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class DemoSearch extends TSP {

    private final double T0;
    private double T;
    private final int numOfCandidates;
    private Queue<Pair> tabuList;
    private int maxTabuSize;
    private Timer timer;

    public DemoSearch(double T0, double candidateRatio, double tabuSizeRatio) {
        if (T0 <= 0) {
            System.out.println("======INITIAL TEMPERATURE SHOULD BE BIGGER THAN 0======");
            System.exit(1);
        }

        this.T0 = T0;
        this.T = T0;
        this.numOfCandidates = (int) ((double) numOfCities * candidateRatio);
        System.out.println(this.numOfCandidates);
        this.tabuList = new LinkedList<>();
        this.maxTabuSize = (int) (numOfCities * tabuSizeRatio);
        this.timer = new Timer();
    }

    @Override
    public Path calculatePath(int startPoint) {
        timer.tic();
        NearestNeighbor nearestNeighbor = new NearestNeighbor();
        Path path = nearestNeighbor.calculatePath(startPoint);
        return calculatePath(path);
    }

    @Override
    public Path calculatePath(Path path) {
        int n = 0, k = 0;
        Path retPath = path.deepCopy();

        while(!timer.isOver(Timer.FIRST_DEMO_LIMIT_SEC)) {

            ArrayList<Path> neighbors = new ArrayList<>();
            for(int i = 0; i < numOfCandidates; i++) {
                Path neighbor = retPath.deepCopy();
                int[] twoRandomNum = Pick.getTwoRandomIndex(1, numOfCities - 1);
                neighbor.twoOptSwap(twoRandomNum[0], twoRandomNum[1]);
                neighbors.add(neighbor);
            }

            Path bestPath = neighbors.get(0);
            for(Path p : neighbors) {
                if(!tabuList.contains(p.recentlySwappedPair) && bestPath.totalCost > p.totalCost) {
                    bestPath = p.deepCopy();
                }
            }

            if(retPath.totalCost > bestPath.totalCost) {
                retPath = bestPath.deepCopy();
                T = Cooling.exponentialMultiplicativeCooling(T0, 0.99, k++);
            } else if (Math.random() < Math.pow(Math.E, (retPath.totalCost - bestPath.totalCost) / T)) {
                retPath = bestPath.deepCopy();
            }

            n++;

            tabuList.offer(bestPath.recentlySwappedPair);
            if(tabuList.size() > maxTabuSize) {
                tabuList.poll();
            }

            // delete this, just for debug
            if (timer.tick()) {
                System.out.println(
                        "time : " + timer.toc() + "s, "
                                + "n : " + n + ", "
                                + "k : " + k + ", "
                                + "T : " + T + ", "
                                + "cost : " + retPath.totalCost
                );
            }
        }

        return retPath;

    }
}
