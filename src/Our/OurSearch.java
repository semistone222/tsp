package Our;

import greedy.NearestNeighbor;
import sa.Cooling;
import util.*;

import java.awt.*;

public class OurSearch extends TSP {

    private TabuHelper TABU;
    private SAHelper SA;
    private Timer timer;

    public OurSearch() {
        TABU = new TabuHelper();
        SA = new SAHelper(40, 0, 0.8);
        timer = new Timer(Timer.FIRST_DEMO_LIMIT_SEC);
    }
    @Override
    public Path calculatePath(int startPoint) {
        timer.start(System.currentTimeMillis());
        NearestNeighbor nearestNeighbor = new NearestNeighbor();
        Path path = nearestNeighbor.calculatePath(Map.getStartPt());
//        Path path = RandomPath.getRandomPath(Map.getStartPt());
        return calculatePath(path);
    }

    @Override
    public Path calculatePath(Path path) {

        Path startOptimalPath = path.deepCopy();
        boolean bad = false;
        while(timer.toc() < 30.0) {
            SA.cooldown(7, timer.toc()/25.0);
            Path trial = TABU.Extract(startOptimalPath);

            if (trial.totalCost < startOptimalPath.totalCost) startOptimalPath = trial.deepCopy();
            else if (Math.random() < SA.probability(startOptimalPath.totalCost, trial.totalCost)) {
                startOptimalPath = trial.deepCopy();
//              TABU.clear();
            }
            info(startOptimalPath);
        }

        startOptimalPath.refreshCost();
        startOptimalPath.pathCheck();

        new Chart(startOptimalPath);
        return startOptimalPath;
    }

    private void info (Path p) {
        if (timer.tick()) {
            System.out.printf("%3.2f | cost :%5.2f | prob :%3.2f\n", timer.toc(), p.totalCost, SA.lastProb);
        }
    }
}
