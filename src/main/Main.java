package main;

import greedy.NearestNeighbor;
import greedy.TwoOptSearch;
import sa.SASearch;
import util.Map;
import util.Path;

public class Main {
    public static void main(String... args) {
        // read file
        // cf) 662's optimal tour = 2513
        Map.setMapFile("data/662.txt");
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

        // SASearch
        SASearch saSearch = new SASearch(90, 100000);
        Path path3 = saSearch.calculatePath(1);
        path3.printOrder();
        path3.printTotalCost();

    }
}
