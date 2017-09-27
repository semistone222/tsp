package Our;

import greedy.NearestNeighbor;
import sa.Cooling;
import util.*;

public class OurSearch extends TSP {

    private TabuHelper TABU;
    private SAHelper SA;
    private Timer timer;

    public OurSearch() {
        TABU = new TabuHelper();
        SA = new SAHelper(20, 0, 0.8);
        timer = new Timer(Timer.FIRST_DEMO_LIMIT_SEC);
    }
    @Override
    public Path calculatePath(int startPoint) {
        timer.start(System.currentTimeMillis());
        NearestNeighbor nearestNeighbor = new NearestNeighbor();
        Path path = nearestNeighbor.calculatePath(startPoint);
        // Path path = RandomPath.getRandomPath(startPoint);
        return calculatePath(path);
    }

    @Override
    public Path calculatePath(Path path) {

        Path startOptimalPath = path.deepCopy();
        Path bestPath = path.deepCopy();
        double dataOptMargin = numOfCities / 900 * 5.0;

        double now = 0.0;
        while((now = timer.toc()) < 30.0) {
            SA.cooldown(7, Math.max(20.0 - dataOptMargin, now)/(25.0 - dataOptMargin));
            Path trial = TABU.Extract(startOptimalPath);
            // int[] twoRandomNum = Pick.getTwoRandomIndex(1, numOfCities - 1);
            // Path trial = startOptimalPath.deepCopy();
            // trial.twoOptSwap(twoRandomNum[0], twoRandomNum[1]);

            if (trial.totalCost < bestPath.totalCost) bestPath = trial.deepCopy();

            if (trial.totalCost < startOptimalPath.totalCost) startOptimalPath = trial.deepCopy();
            else if (Math.random() < SA.probability(startOptimalPath.totalCost, trial.totalCost)) {
                startOptimalPath = trial.deepCopy();
                // TABU.clear();
            }
            info(startOptimalPath);
        }

        bestPath.refreshCost();
        // debug, delete this.
        bestPath.printState();
        bestPath.write();

        new Chart(bestPath);
        return bestPath;
    }

    private void info (Path p) {
        if (timer.tick()) {
            System.out.printf("%3.2f | cost :%5.2f | prob :%3.2f\n", timer.toc(), p.totalCost, SA.lastProb);
        }
    }
}
