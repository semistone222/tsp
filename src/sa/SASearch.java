package sa;

import greedy.NearestNeighbor;
import util.Path;
import util.TSP;

public class SASearch extends TSP {
    // TODO : cooling function
    // http://what-when-how.com/artificial-intelligence/a-comparison-of-cooling-schedules-for-simulated-annealing-artificial-intelligence/

    private final double T0;
    private final double dT;
    private double T;

    public SASearch(double T, double dT) {
        if (T <= 0) {
            System.out.println("======TEMPERATURE SHOULD BE BIGGER THAN 0======");
            System.exit(1);
        }
        if (!(dT < 1 && dT > 0)) {
            System.err.println("======DELTA SHOULD BE BETWEEN 0 to 1======");
            System.exit(1);
        }

        this.T0 = T;
        this.dT = dT;
        this.T = T;
    }

    @Override
    public Path calculatePath(int startPoint) {
        NearestNeighbor nearestNeighbor = new NearestNeighbor();
        Path path = nearestNeighbor.calculatePath(startPoint);
        return calculatePath(path);
    }

    @Override
    public Path calculatePath(Path path) {
        return null;
    }
}
