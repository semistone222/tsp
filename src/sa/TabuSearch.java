package sa;

import Our.RandomPath;
import greedy.NearestNeighbor;
import javafx.util.Pair;
import util.Path;
import util.Pick;
import util.TSP;
import util.Timer;
import util.Memo;

import java.util.*;

public class TabuSearch extends TSP {

    private final int numOfCandidates;
    private Queue<Pair> tabuList;
    private int maxTabuSize;
    private Timer timer;

    public TabuSearch(double candidateRatio, double tabuSizeRatio) {
        this.numOfCandidates = (int) (numOfCities * candidateRatio);
        this.tabuList = new LinkedList<>();
        this.maxTabuSize = (int) (numOfCities * tabuSizeRatio);
        this.timer = new Timer(Timer.FIRST_DEMO_LIMIT_SEC);
    }

    @Override
    public Path calculatePath(int startPoint) {
        this.timer.start(System.currentTimeMillis());
        // NearestNeighbor nearestNeighbor = new NearestNeighbor();
        // Path path = nearestNeighbor.calculatePath(startPoint);
        Path path = RandomPath.getRandomPath(startPoint);
        return calculatePath(path);
    }

    @Override
    public Path calculatePath(Path path) {
        Path retPath = path.deepCopy();
        Memo memo = new Memo("Tabu");

        while(!timer.isTimeGone()) {

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

            // delete this, just for debug
            if (false) System.out.println(
                    "time : " + timer.getExecutionSeconds() + "s, "
                            + "cost : " + retPath.totalCost
            );

            if (timer.tick()){
                System.out.printf("iter(%6.2fM), timeDelta(%4.2f), cost(%5.2f)\n",
                        0.0, timer.toc(), retPath.totalCost);
                memo.doMemo((int)Math.round(retPath.totalCost));
            }
        }

        memo.saveMemo();
        return retPath;
    }
}
