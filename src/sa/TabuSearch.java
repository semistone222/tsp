package sa;

import greedy.NearestNeighbor;
import util.Path;
import util.Pick;
import util.TSP;
import util.Timer;

import java.util.*;

public class TabuSearch extends TSP {

    private final int numOfCandidates;
    // TODO : tabuClearSize, tabu
    private final int tabuClearSize;
    private int[][] tabu;
    private Timer timer;

    public TabuSearch(int numOfCandidates, int tabuClearSize) {
        this.numOfCandidates = numOfCandidates;
        this.tabuClearSize = tabuClearSize;
        this.tabu = new int[numOfCities + 1][numOfCities + 1];
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
        Path minPath = path.deepCopy();

        while(!timer.isTimeGone()) {

            LinkedList<Path> candidates = new LinkedList<>();
            for(int i = 0; i < numOfCandidates; i++) {
                Path candidate = minPath.deepCopy();
                int[] twoRandomNum = Pick.getTwoRandomIndex(1, numOfCities - 1);
                candidate.twoOptSwap(twoRandomNum[0], twoRandomNum[1]);
                candidates.add(candidate);
            }

            Collections.sort(candidates, new Comparator<Path>() {
                @Override
                public int compare(Path o1, Path o2) {
                    if(o1.totalCost > o2.totalCost) {
                        return 1;
                    } else if (o1.totalCost < o2.totalCost) {
                        return -1;
                    }
                    return 0;
                }
            });

            Path bestPath = candidates.pollFirst();

            if(!isTabu(bestPath)) {
                minPath = bestPath.deepCopy();
            } else if (isAspirationCriteriaFulfilled(minPath, bestPath)) {
                minPath = bestPath.deepCopy();
            } else {
                while(!isTabu(bestPath)) {
                    bestPath = candidates.pollFirst();
                }
                minPath = bestPath.deepCopy();
            }

            updateTabu();
        }

        return minPath;
    }

    // TODO : updateTabu
    private void updateTabu() {

    }

    // TODO : isTabu
    private boolean isTabu(Path path) {
        return true;
    }

    private boolean isAspirationCriteriaFulfilled(Path minPath, Path newPath) {
        return minPath.totalCost > newPath.totalCost;
    }
}
