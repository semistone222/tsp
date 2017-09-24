package greedy;

import util.Path;
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
        int trial = 0;
        while(trial < limitTrial) {
            // TODO : Deep Copy
        }
        return null;
    }
}
