package main;

import ga.GASearch;
import ga.GeneticLocalSearch;
import ga.crossover.PartiallyMatchedCrossover;
import ga.initialize.RandomInitializer;
import ga.initialize.SAInitializer;
import ga.mutate.SwapMutation;
import ga.optimize.TabuOptimizer;
import ga.optimize.TwoOptOptimizer;
import ga.select.TournamentSelection;
import util.Chart;
import util.Map;
import util.Path;

import java.util.Scanner;

public class Main {
    public static void main(String... args) {
        // read file
        // cf) 662's optimal tour = 2513
        Scanner sc = new Scanner(System.in);
        System.out.print("input data name in 'data' folder : ");
        String fileName = sc.next();
        sc.close();
        Map.setMapFile("data/" + fileName);
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
        // new Chart(path3);

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
        // OurSearch ourSearch = new OurSearch();
        // Path path6 = ourSearch.calculatePath(map.getCenterCityId());
        // path6.printTotalCost();

        // GA test
        // GASearch gaSearch = new GASearch(100, 100000);
        // gaSearch.setProcess(
        //         new SAInitializer(30, 10000),
        //         new TournamentSelection(2 * 2 * 2 * 2),
        //         new PartiallyMatchedCrossover(),
        //         new SwapMutation(0.01)
        // );

        // Path path7 = gaSearch.calculatePath(1);
        // path7.printTotalCost();

        GeneticLocalSearch geneticLocalSearch = new GeneticLocalSearch(100, 10000);
        geneticLocalSearch.setProcess(
                new SAInitializer(30, 100),
                new TournamentSelection(2 * 2 * 2 * 2),
                new PartiallyMatchedCrossover(),
                new SwapMutation(0.01),
                new TabuOptimizer(0.1, 0.005, 1)
        );

        Path path8 = geneticLocalSearch.calculatePath(1);
        path8.printTotalCost();
    }
}
