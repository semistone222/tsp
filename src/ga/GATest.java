package ga;

import ga.crossover.EdgeRecombination;
import ga.crossover.PartiallyMatchedCrossover;
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
    private double oneGenerationTime;
    private double estimateGenerationTime;
    private Timer timer;

    public GATest() {
        timer = new Timer();
        timer.synchronize();

        Map map = Map.getInstance();
        this.numOfCities = map.getNumOfCities();
        this.distanceMap = map.getDistanceMap();
        System.out.printf("=====data copied(%04.2f)=====\n", timer.toc());

        generation = 0;
        populationSize = 50;
        ASC = new PathComparatorAscCost();

        oneGenerationTime = 0.0d;
        estimateGenerationTime = 0.0d;
    }

    private int populationSize;
    private PathComparatorAscCost ASC;

    public Path calculatePath() {
        Path best;

        // 시작점을 초기화
        int startPoint[] = Pick.randCities(populationSize);
        startPoint[0] = 0;//Map.getInstance().getCenterCityId();

        // 부모를 초기화
        Path population[] = new Path[populationSize];
        NearestNeighbor initA = new NearestNeighbor();
        SAInitializer initB  = new SAInitializer(30, 100);
        for (int i = 0; i < 25; i++)
            population[i] = initA.calculatePath(startPoint[0]);
        for (int i = 25; i < populationSize; i++)
            population[i] =
                    initB.initializePopulation(1, startPoint[0])[0];
        best = population[0].copyValue();
        System.out.printf("=====initialized(%04.2f)=====\n", timer.toc());

        // A 정렬 - 저장 - [엘리트선택 - 엘리트탐색] - 선택 - 교배
        // (B) 정렬 - 저장 - 선택 - 교배 - 탐색
        // C 정렬 - 저장 - 탐색 - [선택 - 탐색] - 교배
        while (true) {
            // 정렬
            Arrays.sort(population, ASC);

            // 저장
            if (best.totalCost > population[0].totalCost)
                best = population[0].copyValue();
            if (timer.tick())
                System.out.printf("cost [%.2f] @ g(%04d) t(%06.2f)\n",
                        best.totalCost, generation, timer.toc());

            // 시간예측
            double now = timer.toc();
            if (generation > 1) {
                oneGenerationTime = now - oneGenerationTime;
                estimateGenerationTime = (estimateGenerationTime + oneGenerationTime) / 2.0d;
            }
            else if (generation == 1) estimateGenerationTime = oneGenerationTime;
            else oneGenerationTime = now;

            // 시간조건
            if (timer.toc() > 119.0) break;
            if (timer.toc() > 119.0 - (estimateGenerationTime + 5.0)) break;

            // 이주, migration <- 10(20%) selection
            RouletteWheelSelection selectA = new RouletteWheelSelection(30.5d);
            selectA.setSpinTable(population); // 초기화 후 중복 없이 10개 뽑기
            Path migration[] = new Path[10];
            for (int i = 0; i < 10;) { // i will be even number
                int newTargetIdx[] = selectA.uniqueSelect(2);
                if (newTargetIdx.length == 0) {
                    System.err.println("GATest, newTargetIdx has 0 length");
                    System.exit(0);
                }
                for (int j : newTargetIdx)
                    migration[i++] = population[j];
            }

            //교배
            Path child[] = new Path[50];
            System.arraycopy(migration, 0, child, 0, 10);
            // 1. PMX 20개 생성
            PartiallyMatchedCrossover crossA = new PartiallyMatchedCrossover();
            for (int i = 10; i < 30;) {
                if ((i - 10) % 10 == 0) selectA.setSpinTable(population);
                int newParentIdx[] = selectA.uniqueSelect(2);
                Path newChild[] = crossA.crossover(population[newParentIdx[0]], population[newParentIdx[1]]);
                for (Path j : newChild)
                    child[i++] = j;
            }
            // 2. ERC 20개 생성
            EdgeRecombination crossB = new EdgeRecombination();
            for (int i = 30; i < 50;) {
                if ((i - 30) % 10 == 0) selectA.setSpinTable(population);
                int newParentIdx[] = selectA.uniqueSelect(2);
                Path newChild[] = crossB.crossover(population[newParentIdx[0]], population[newParentIdx[1]]);
                for (Path j : newChild)
                    child[i++] = j;
            }

            //탐색
            System.arraycopy(child, 0, population, 0, 50);
            for (int i = 0; i < 50; i++) {
                TabuOptimizer optA =
                        new TabuOptimizer(0.01, 0.005, 100);
                population[i] = optA.optimize(population[i]);
            }
            for (int i = 0; i < 50; i++) {
                for (int j = 0; j < 100; j++) {
                    int randIdx[] = Pick.randIdx(3);
                    population[i].caseThreeOpt(randIdx[0], randIdx[1], randIdx[2]);
                }
            }

            generation++;
        }
        return best;
    }
}
