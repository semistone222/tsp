package main;

import Our.OurSearch;
import greedy.NearestNeighbor;
import greedy.ThreeOptSearch;
import greedy.TwoOptSearch;
import sa.SASearch;
import sa.TabuSearch;
import util.Map;
import util.Path;
import util.Chart;

public class Main {
    public static void main(String... args) {
        // read file
        // cf) 662's optimal tour = 2513
        Map.setMapFile("data/131.txt");
        Map map = Map.getInstance();
        // map.printCityHashMap();
        // map.printDistanceMap();

        // greedy test
        // NearestNeighbor nearestNeighbor = new NearestNeighbor();
        // Path path = nearestNeighbor.calculatePath(1);
        // path.printOrder();
        // path.printTotalCost();

        // 2-opt search test
        // TwoOptSearch twoOptSearch = new TwoOptSearch(1000000000);
        // Path path2 = twoOptSearch.calculatePath(map.getCenterCityId());
        // path2.printOrder();
        // path2.printTotalCost();

        // 3-opt search test
        // ThreeOptSearch threeOptSearch = new ThreeOptSearch(1000000000);
        // Path path3 = threeOptSearch.calculatePath(map.getCenterCityId());
        // path3.printOrder();
        // path3.printTotalCost();
        //new Chart(path3);

        // SASearch test
        // SASearch saSearch = new SASearch(90, 100000);
        // Path path4 = saSearch.calculatePath(1);
        // path4.printOrder();
        // path4.printTotalCost();

        // TabuSearch test
        // TabuSearch tabuSearch = new TabuSearch(16 , 0.05);
        // Path path5 = tabuSearch.calculatePath(map.getCenterCityId());
        // path5.printOrder();
        // path5.printTotalCost();

        // SA + Tabu test
        OurSearch ourSearch = new OurSearch();
        Path path6 = ourSearch.calculatePath(map.getCenterCityId());
        path6.printTotalCost();
    }
}
