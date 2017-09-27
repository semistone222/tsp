package greedy;

import Our.RandomPath;
import util.*;

public class ThreeOptSearch extends TSP {
    private  int limitTrial;
    private Timer timer;

    public ThreeOptSearch(){ limitTrial = 1000000000; }
    public ThreeOptSearch(int _limit) { limitTrial = _limit; }

    @Override public Path calculatePath(int startPoint) {
        timer = new Timer(Timer.FIRST_DEMO_LIMIT_SEC);
        timer.tic();

        NearestNeighbor nearestNeighbor = new NearestNeighbor();
        Path path = nearestNeighbor.calculatePath(startPoint);
        // Path path = RandomPath.getRandomPath(startPoint);
        return calculatePath(path);
    }

    @Override public Path calculatePath(Path path) {
        return threeOptSearch(path);
    }

    private Path threeOptSearch (Path path) {
        Memo memo = new Memo("treeOpt");

        Path minPath = path.deepCopy();
        Path trialPath;

        int trial = 0;
        while(trial < limitTrial && !timer.isTimeGone()) {
            trialPath = minPath.deepCopy();

            int [] randNums = Pick.randNums(3, numOfCities );
            trialPath.threeOptSwap(randNums[0], randNums[1], randNums[2]);

            if(minPath.totalCost > trialPath.totalCost) {
                minPath = trialPath.deepCopy();
            }

            trial++;
            if (timer.tick()) {
                System.out.printf("iter(%6.2fM), timeDelta(%4.2f), cost(%5.2f)\n",
                        trial / 1000000.0, timer.toc(), minPath.totalCost);
                memo.doMemo((int)Math.round(minPath.totalCost));
            }
        }

        memo.saveMemo();
        return minPath;
    }
}
