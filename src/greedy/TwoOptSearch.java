package greedy;

import util.*;

public class TwoOptSearch extends TSP {

    private int limitTrial;
    private Timer timer;

    public TwoOptSearch(int limitTrial) {
        this.limitTrial = limitTrial;
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
        while(trial < limitTrial && !timer.isOver(Timer.FIRST_DEMO_LIMIT_SEC)) {
            Path trialPath = minPath.deepCopy();

            int[] twoRandomNum = Pick.getTwoRandomIndex(1, numOfCities - 1);
            trialPath.twoOptSwap(twoRandomNum[0], twoRandomNum[1]);

            if(minPath.totalCost > trialPath.totalCost) {
                minPath = trialPath.deepCopy();
            }

            trial++;

            // delete this, just for debug
            if (timer.tick()) {
                System.out.println(
                        "iter : " + trial / 1000000.0 + "M, "
                                + "time : " + timer.toc() + "s, "
                                + "cost : " + minPath.totalCost
                );
            }
        }

        return minPath;
    }

    // something wrong with this method
    @Deprecated
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

    // not good
    @Deprecated
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
