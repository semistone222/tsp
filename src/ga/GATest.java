package ga;

import greedy.NearestNeighbor;
import ga.initialize.*;
import ga.optimize.*;
import ga.select.*;

import util.*;

import java.util.Arrays;

public class GATest {
    private int numOfCities;
    private double distanceMap[][];
    private int generation;
    private Timer timer;

    public GATest() {
        timer = new Timer();
        timer.synchronize();

        Map map = Map.getInstance();
        this.numOfCities = map.getNumOfCities();
        this.distanceMap = map.getDistanceMap();
        System.out.printf("=====data copied(%03.2f)=====\n", timer.toc());

        generation = 0;
        populationSize = 50;
        ASC = new PathComparatorAscCost();
    }

    private int populationSize;
    private PathComparatorAscCost ASC;

    public Path calculatePath() {
        Path best;

        // 시작점을 초기화
        int startPoint[] = Pick.randCities(populationSize);
        startPoint[0] = Map.getInstance().getCenterCityId();

        // 부모를 초기화
        Path population[] = new Path[populationSize];
        NearestNeighbor initA = new NearestNeighbor();
        SAInitializer initB  = new SAInitializer(30, 100);
        for (int i = 0; i < 25; i++)
            population[i] = initA.calculatePath(startPoint[i]);
        for (int i = 25; i < populationSize; i++)
            population[i] =
                    initB.initializePopulation(1, startPoint[i])[0];
        best = population[0].copyValue();
        System.out.printf("=====initialized(%03.2f)=====\n", timer.toc());

        // (A) 정렬 - 저장 - 선택 - 탐색 - 선택 - 교배
        // B 정렬 - 저장 - 탐색 - 선택 - 교배
        // C 정렬 - 저장 - 탐색 - 선택 - 탐색 - 교배
        while (timer.toc() < 119.0) {
            //정렬
            Arrays.sort(population, ASC);

            //저장
            if (best.totalCost > population[0].totalCost)
                best = population[0].copyValue();
            if (timer.tick())
                System.out.printf("cost [%.2f] @ g(%03d) t(%03.2f)\n",
                        best.totalCost, generation, timer.toc());

            //선택
            RouletteWheelSelection selectA = new RouletteWheelSelection(3.5d);
            Path migration[] = new Path[10];
            for (int i = 0; i < 10;) { // i will be even number
                int newParentIdx[] = selectA.select(population);
                if (newParentIdx.length == 0) {
                    System.err.println("GATest, newSelect has 0 length");
                    System.exit(0);
                }
                for (int j : newParentIdx)
                    migration[i] = population[newParentIdx[j]];
                i += newParentIdx.length;
            }

            //탐색
            for (int i = 0; i < 5; i++) {
                TabuOptimizer optA =
                        new TabuOptimizer(0.01, 0.005, 100);
                migration[i] = optA.optimize(population[i]);
            }
            for (int i = 5; i < 10; i++) {
                for (int j = 0; j < 100; j++) {
                    int randIdx[] = Pick.randIdx(3);
                    migration[i].caseThreeOpt(randIdx[0], randIdx[1], randIdx[2]);
                }
            }

            // 교배
            for (int i = 0; i < 10; i++) {

            }
        }
        return null;
    }
}
