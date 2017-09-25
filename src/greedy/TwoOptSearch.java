package greedy;

import util.Path;
import util.Pick;
import util.TSP;

public class TwoOptSearch extends TSP {

    private int limitTrial;

    public TwoOptSearch(int limitTrial) {
        this.limitTrial = limitTrial;
    }

    @Override
    public Path calculatePath(int startPoint) {
        NearestNeighbor nearestNeighbor = new NearestNeighbor();
        Path path = nearestNeighbor.calculatePath(startPoint);
        return calculatePath(path);
    }

    @Override
    public Path calculatePath(Path path) {
         // default 2-opt
         // O(1)
         return pickTwoRandomEdge(path);

         // lower performance and even higher complexity(time) than pickTwoRandomEdge.
         // even if we lower complexity, performance will be poor.
         // O(nlogn)
         // return pickTwoLongEdge(path);

         // it seems to be slightly faster than default 2-opt.
         // but, it is hard to say that this algorithm is better than default 2-opt.
         // maybe, picking longer edge is not important.
         // O(n)
         // return pickWeightedRandomEdge(path);
    }

    private Path pickTwoRandomEdge(Path path) {
        Path minPath = path.deepCopy();

        int trial = 0;
        while(trial < limitTrial) {
            Path trialPath = minPath.deepCopy();

            int[] twoRandomNum = Pick.getTwoRandomIndex(1, numOfCities - 1);
            trialPath.twoOptSwap(twoRandomNum[0], twoRandomNum[1]);

            if(minPath.totalCost > trialPath.totalCost) {
                minPath = trialPath.deepCopy();
            }

            trial++;
        }

        return minPath;
    }

    private Path pickTwoLongEdge(Path path) {
        Path minPath = path.deepCopy();

        boolean[][] tried = new boolean[numOfCities + 1][numOfCities + 1];

        int trial = 0;
        while(trial < limitTrial) {
            Path trialPath = minPath.deepCopy();
            int[] index = trialPath.getTwoLongEdgeIndex(tried);
            trialPath.twoOptSwap(index[0], index[1]);

            if(minPath.totalCost > trialPath.totalCost) {
                minPath = trialPath.deepCopy();
                tried = new boolean[numOfCities + 1][numOfCities + 1];
            } else {
                tried[index[0]][index[1]] = true;
            }
            trial++;
        }

        return minPath;
    }

    private Path pickWeightedRandomEdge(Path path) {
        Path minPath = path.deepCopy();

        int trial = 0;
        while(trial < limitTrial) {
            Path trialPath = minPath.deepCopy();
            int[] index = trialPath.getTwoDistanceWeightedRandomIndex();
            trialPath.twoOptSwap(index[0], index[1]);

            if(minPath.totalCost > trialPath.totalCost) {
                minPath = trialPath.deepCopy();
            }

            trial++;
        }

        return minPath;

    }
}
