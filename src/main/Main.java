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
        new DataChart();
        String input;
        String defaultFileName;

        Scanner sc = new Scanner(System.in);
        System.out.print("Configuration... chosee default data sets [1|2|3] : ");
        input = sc.nextLine();
        int choice = Integer.parseInt(input);
        switch (choice){
            case 3:
                defaultFileName = "9698_27724.txt";
                break;
            case 2:
                defaultFileName = "4355_12723.txt";
                break;
            case 1:
            default:
                defaultFileName = "1911_6396.txt";
        }
        System.out.println(input + " select [ " + defaultFileName + " ]");

        System.out.println("Request input data_name in 'data' folder ex>662.txt");
        System.out.println("blank occurs default data (" + defaultFileName + ")");
        System.out.print("filename [type|enter] : ");
        String fileName = sc.nextLine();
        sc.close();
        if (fileName.equals("")) {
            System.out.println("[select default data]");
            fileName = defaultFileName;
        }

        Timer.begin();
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
        Path path8 = geneticLocalSearch.calculatePath(1);
        path8.printState();
        path8.printTotalCost();
        new DataChart();
        new Chart(path8);

        /* GATest */
        //GATest gaTest = new GATest();
        //Path gaTestPath = gaTest.calculatePath();
        //gaTestPath.printState();
        //gaTestPath.printTotalCost();
        //new DataChart();
        //new Chart(gaTestPath);
    }
}
