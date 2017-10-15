package ga.optimize;

import javafx.util.Pair;
import util.Map;
import util.Path;
import util.Pick;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class TabuOptimizer implements Optimizer {

    private final int numOfCities;
    private final int numOfCandidates;
    private final int maxTabuSize;
    private final int numOfIterations;
    private Queue<Pair> tabuList;

    public TabuOptimizer(double candidateRatio, double tabuSizeRatio, int numOfIterations) {
        this.numOfCities = Map.getInstance().getNumOfCities();
        this.numOfCandidates = (int) (numOfCities * candidateRatio);
        this.maxTabuSize = (int) (numOfCities * tabuSizeRatio);
        this.numOfIterations = numOfIterations;
        this.tabuList = new LinkedList<>();
    }

    @Override
    public Path optimize(Path path) {
        Path retPath = path.deepCopy();

        int currentIterations = 0;
        while(currentIterations < numOfIterations) {

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
            }

            tabuList.offer(bestPath.recentlySwappedPair);
            if(tabuList.size() > maxTabuSize) {
                tabuList.poll();
            }

            currentIterations++;
        }

        return retPath;
    }
}
