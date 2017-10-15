package ga;

import ga.initialize.Initializer;
import ga.optimize.Optimizer;
import greedy.NearestNeighbor;
import sa.TabuSearch;
import util.*;

import java.util.Arrays;
import java.util.HashMap;

public class GAMultiGroup3 extends GATSP {
    private PathComparatorAscCost ASC;
    public GAMultiGroup3() {
       ASC = new PathComparatorAscCost();
    }

    @Override
    public Path calculatePath() {
        System.out.println("=====Read Finish" + String.format("(%.2f)", timer.toc()) + "=====");
        Path bestPath;

        int startPoint = Map.getInstance().getCenterCityId();
        int populationSize = 50;
        int populationPerGroup = 25;
        int groups = populationSize/populationPerGroup;

        // === stage 1 : initialize
        Path population[] = new Path[populationSize];
        NearestNeighbor seeder = new NearestNeighbor();
        Path seed = seeder.calculatePath(startPoint);
        for (int i = 0; i < groups; i++)
            for (int j = 0; j < populationPerGroup; j++)
                population[i*populationPerGroup + j] = seed.deepCopy();
        bestPath = seed.deepCopy();

        System.out.println("=====World Begin" + String.format("(%.2f)", timer.toc()) + "=====");
        while (true) {
            // === stage 2 : optimize
            for (int i = 0; i < groups && generation != 0; i++)
                for (int j = 0; j < populationPerGroup; j++) {
                    TabuSearch optimizer = new TabuSearch(32, 0.05);
                    int tempIdx = i * populationPerGroup + j;
                    population[tempIdx] = optimizer.calculatePath(population[tempIdx], 0.1);
                    if (bestPath.totalCost > population[tempIdx].totalCost )
                        bestPath = population[tempIdx].deepCopy();
                }

            System.out.print("generation : " + (++generation) + ", timer : ");
            System.out.printf("%.2f, best : %.2f\n", timer.toc(), bestPath.totalCost);
            if(timer.toc() > 90.0) break;

            // === stage 3 : select
            Path[] temp = new Path[populationPerGroup];
            for (int i = 0; i < groups; i++) {
                for (int j = 0; j < populationPerGroup; j++) {
                    temp[j] = population[i * populationPerGroup + j];
                }
                Arrays.sort(temp, ASC);
                int parent_idx[] = selection.select(temp);
                int replace_idx = 5;
                Path parent[] = new Path[2];
                parent[0] = population[i * populationPerGroup + parent_idx[0]].deepCopy();
                parent[1] = population[i * populationPerGroup + parent_idx[1]].deepCopy();

                for (int j = replace_idx; (j + 2) < populationPerGroup; j+=2) {
                    Path child[] = crossover.crossover(parent[0], parent[1]);
                    population[i * populationPerGroup + j + 0] = child[0];
                    population[i * populationPerGroup + j + 1] = child[1];
                }
            }
        }

        // === statge 4 : last tabu
        Arrays.sort(population, ASC);
        if (bestPath.totalCost > population[0].totalCost )
            bestPath = population[0].deepCopy();

        TabuSearch opt = new TabuSearch(32, 0.1);
        bestPath = opt.calculatePath(bestPath, 120.0 - timer.toc() - 1.0);

        bestPath.printState();
        return bestPath;
    }
}
