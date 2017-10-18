package ga;

import ga.crossover.*;
import ga.initialize.*;
import ga.mutate.*;
import ga.optimize.*;
import ga.select.*;

import greedy.NearestNeighbor;
import sa.TabuSearch;
import util.*;

import java.util.Arrays;

// GATSP 를 상속받는 GAMultiGroup3 입니다
@Deprecated
public class GAMultiGroup3 extends GATSP {
    // 변수들
    private PathComparatorAscCost ASC;
    private int populationPerGroup;
    private int groups;
    private int populationSize;

    // 생성자, 그룹수와 그룹당 인구수를 받습니다
    public GAMultiGroup3(int _groups, int _populationsPerGroup) {
        groups = _groups;
        populationPerGroup = _populationsPerGroup;
        populationSize = groups * populationPerGroup;
        ASC = new PathComparatorAscCost();

        // Main 까지 왔다갔다 하기 귀찮아서 여기서 파라메터를 조정합니다
        setProcess(
                new SAInitializer(30, 10000),
                new TabuOptimizer(0.01, 0.005, 1000),
                new RouletteWheelSelection(4.5d),
                new EdgeRecombination(),
                new SwapMutation(0.1)
        );
    }

    @Override
    public Path calculatePath() {
        // 읽는데 시간은 얼마 안걸리지만 넣어 보았습니다
        System.out.println("=====Read Finish" + String.format("(%.2f)", timer.toc()) + "=====");
        Path bestPath;

        // 시작점과 변수를 초기화 합니다
        int startPoint[] = Pick.randCities(populationSize);
        startPoint[0] = Map.getInstance().getCenterCityId();
        int populationSize = populationPerGroup * groups;

        // === stage 1 : First Parents
        Path population[] = new Path[populationSize];
        NearestNeighbor seeder = new NearestNeighbor();
        for (int i = 0; i < groups; i++)
            for (int j = 0; j < populationPerGroup; j++) {
                int idx = i * populationPerGroup + j;
                population[idx] = seeder.calculatePath(startPoint[idx]);
            }
        // 초기화니까(귀찮) 그냥 첫번째를 복사해서 집어 넣는다
        bestPath = population[0].deepCopy();

        // 첫번째 자식이 만들어졌다고 선언한다!
        System.out.println("=====World Begin" + String.format("(%.2f)", timer.toc()) + "=====");
        while (true) {
            // === stage 2 : optimize
            for (int i = 0; i < groups; i++)
                for (int j = 0; j < populationPerGroup; j++) {
                    int idx = i * populationPerGroup + j;
                    population[idx] = optimizer.optimize(population[idx]);
                    if (bestPath.totalCost > population[idx].totalCost )
                        bestPath = population[idx].deepCopy();
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

                for (int j = replace_idx; (j + 1) < populationPerGroup; j+=1) {
                    Path child[] = crossover.crossover(parent[0], parent[1]);
                    population[i * populationPerGroup + j + 0] = child[0];
                }
            }
        }

        // === statge 4 : last tabu
        Arrays.sort(population, ASC);
        if (bestPath.totalCost > population[0].totalCost )
            bestPath = population[0].deepCopy();

        TabuSearch opt = new TabuSearch(0.01, 0.1);
        bestPath = opt.calculatePath(bestPath, 120.0 - timer.toc() - 1.0);

        bestPath.printState();
        return bestPath;
    }
}
