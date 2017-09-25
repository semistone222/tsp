package main;

import greedy.NearestNeighbor;
import greedy.TwoOptSearch;
import util.Map;
import util.Path;

public class Main {
    public static void main(String... args) {
        // read file
        Map.setMapFile("data/395.txt");
        Map map = Map.getInstance();
        map.printCityHashMap();
        map.printDistanceMap();

        // greedy test
        NearestNeighbor nearestNeighbor = new NearestNeighbor();
        Path path = nearestNeighbor.calculatePath(1);
        path.printOrder();
        path.printTotalCost();

        // two-opt test
        path.twoOptSwap(3, 5);
        path.printOrder();
        path.printTotalCost();

        // 2-opt test
        TwoOptSearch twoOptSearch = new TwoOptSearch(1000000);
        Path path2 = twoOptSearch.calculatePath(1);
        path2.printOrder();
        path2.printTotalCost();

    }
}
