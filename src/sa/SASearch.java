package sa;

import greedy.NearestNeighbor;
import util.Path;
import util.TSP;

// TODO : cooling function
public class SASearch extends TSP {

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
        // begin
        // t <- 0, init T
        // select a current point Vc at random
        // evaluate Vc
        // repeat
        //      repeat
        //          select the neighbor Vn From the neighborhood of Vc
        //          if Vn is better than Vc then Vc <- Vn
        //          else if random[0, 1) < e^(Diff/T) then Vc <- Vn
        //      until(termination-condition)
        //      T <- g(T, t)
        //      t <- t + 1
        // until(termination-condition)
        // end
        return null;
    }
}
