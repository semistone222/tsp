package our;

import javafx.util.Pair;
import util.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class TabuHelper {
    private int candidates;
    private double tabuSizeRatio;
    private Queue<Pair> tabuList;
    private int maxTabuSize;
    private int numOfCities;

    public TabuHelper() {
        numOfCities = Map.getInstance().getNumOfCities();
        tabuSizeRatio = 0.3;
        candidates = (int) (tabuSizeRatio * numOfCities);
        maxTabuSize = 80;
        tabuList = new LinkedList<>();
    }

    public Path Extract(Path item) {
        Path copy = item.deepCopy();

        ArrayList<Path> neighbors = new ArrayList<>();
        for(int i = 0; i < candidates; i++) {
            Path neighbor = copy.deepCopy();
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

        tabuList.offer(bestPath.recentlySwappedPair);
        if(tabuList.size() > maxTabuSize) {
            tabuList.poll();
        }

        return bestPath;
    }
    public void clear(){
        tabuList.clear();
    }
}
