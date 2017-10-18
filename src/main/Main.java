package main;

import ga.*;
import ga.initialize.*;
import ga.select.*;
import ga.select.Selection;
import ga.optimize.*;
import ga.crossover.*;
import ga.mutate.*;
import ga.GeneticLocalSearch;
import ga.crossover.EdgeRecombination;
import ga.crossover.EdgeRecombinationCrossover;
import ga.crossover.PartiallyMatchedCrossover;
import ga.initialize.RandomInitializer;
import ga.initialize.SAInitializer;
import ga.mutate.SwapMutation;
import ga.optimize.TabuOptimizer;
import ga.optimize.TwoOptOptimizer;
import ga.select.TournamentSelection;
import sa.SASearch;
import sa.TabuSearch;
import util.*;

import java.util.Scanner;

public class Main {
    public static void main(String... args) {
        Timer.begin();
        String defaultFileName = "1911_6396.txt";

        // read file
        Scanner sc = new Scanner(System.in);
        System.out.println("Request input data_name in 'data' folder ex>662.txt");
        System.out.println("blank occurs default data (" + defaultFileName + ")");
        System.out.print("filename [type|enter] : ");
        String fileName = sc.nextLine();
        sc.close();
        if (fileName.equals("")) {
            System.out.println("[select default data]");
            fileName = defaultFileName;
        }
        Map.setMapFile("data/" + fileName);
        Map map = Map.getInstance();

         /* TabuSearch(tabu Only) test */
         // TabuSearch tabuSearch = new TabuSearch(16 , 0.05);
         // Path path5 = tabuSearch.calculatePath(map.getCenterCityId());
         // path5.printOrder();
         // path5.printTotalCost();

        // ourSearch(SA + Tabu) test
        // OurSearch ourSearch = new OurSearch();
        // Path path6 = ourSearch.calculatePath(map.getCenterCityId());
        // path6.printTotalCost();

        /* GASearch test */
        // GASearch gaSearch = new GASearch(100, 100000);
        // gaSearch.setProcess(
        //         new SAInitializer(30, 100),
        //         new TournamentSelection(2 * 2 * 2 * 2),
        //         new PartiallyMatchedCrossover(),
        //         new SwapMutation(0.01)
        // );
        // Path path7 = gaSearch.calculatePath(1);
        // path7.printTotalCost();
        // new Chart(path7);

        /* GeneticLocalSearch test */
        GeneticLocalSearch geneticLocalSearch = new GeneticLocalSearch(100, 10000);
        geneticLocalSearch.setProcess(
             new SAInitializer(30, 100),
             new RouletteWheelSelection(2.0d),
             new EdgeRecombination(),
             new SwapMutation(0.1),
             new TwoOptOptimizer(10)
        );

        Path path8 = geneticLocalSearch.calculatePath(1);
        path8.printTotalCost();
    }
}
