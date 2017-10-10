package sa;

import greedy.NearestNeighbor;
import javafx.util.Pair;
import util.Path;
import util.Pick;
import util.TSP;
import util.Timer;

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
        this.timer = new Timer();
    }

    @Override
    public Path calculatePath(int startPoint) {
        this.timer.tic();
        NearestNeighbor nearestNeighbor = new NearestNeighbor();
        Path path = nearestNeighbor.calculatePath(startPoint);
        return calculatePath(path);
    }

    @Override
    public Path calculatePath(Path path) {
        Path retPath = path.deepCopy();

        while(!timer.isOver(Timer.FIRST_DEMO_LIMIT_SEC)) {

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
            if (timer.tick()) {
                System.out.println(
                        "time : " + timer.toc() + "s, "
                                + "cost : " + retPath.totalCost
                );
            }
        }

        return retPath;
    }

    public Path calculatePath(Path path, double sec) {
        timer.tic();
        Path retPath = path.deepCopy();

        while(timer.toc() < sec) {

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

        }


        return retPath;
    }
}
