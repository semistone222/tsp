package ga;

import ga.crossover.Crossover;
import ga.crossover.PartiallyMatchedCrossover;
import ga.initialize.Initializer;
import ga.initialize.SAInitializer;
import ga.mutate.Mutation;
import ga.mutate.SwapMutation;
import ga.optimize.Optimizer;
import ga.optimize.TabuOptimizer;
import ga.optimize.TwoOptOptimizer;
import ga.select.Selection;
import ga.select.TournamentSelection;
import util.*;

import java.util.ArrayList;
import java.util.Arrays;

public class GeneticLocalSearch extends TSP {

    private int populationSize;
    private int generationSize;
    private Path[] population;

    private Initializer initializer;
    private Selection selection;
    private Crossover crossover;
    private SwapMutation mutation;
    private Optimizer optimizer;

    private Timer timer;
    private double oneGenerationTime;
    private double estimateGenerationTime;
    private double adaptiveMutant;
    private int notChangedCount;
    private double prevCost;

    public GeneticLocalSearch(int populationSize, int generationSize) {
        this.timer = new Timer();
        this.timer.synchronize();
        System.out.printf("=====data copied(%04.2f)=====\n", timer.toc());

        this.populationSize = populationSize;
        this.generationSize = generationSize;
        this.population = null;

        this.initializer = null;
        this.selection = null;
        this.crossover = null;
        this.mutation = null;
        this.optimizer = null;

        oneGenerationTime = 0.0d;
        estimateGenerationTime = 0.0d;
        notChangedCount = 0;
        prevCost = 0.0d;

        setProcess(
                new SAInitializer(30, 100),
                new TournamentSelection(2*2),
                new PartiallyMatchedCrossover(),
                new SwapMutation(0.01),
                //new TabuOptimizer(0.1, 0.005, 1)
                new TwoOptOptimizer(100)
        );
    }

    public void setProcess(Initializer initializer, Selection selection, Crossover crossover, Mutation mutation, Optimizer optimizer) {
        this.initializer = initializer;
        this.selection = selection;
        this.crossover = crossover;
        this.mutation = new SwapMutation(adaptiveMutant = .01);
        this.optimizer = optimizer;
    }

    @Override
    public Path calculatePath(int startPoint) {
        CostMemo memo = new CostMemo("GeneticLocalSearch");

        int setStartPoint[] = Pick.randCities(populationSize);
        setStartPoint[0] = Map.getInstance().getCenterCityId();

        population = new Path[populationSize];
        for (int i = 0; i < populationSize; i++) {
            population[i] = initializer.initializePopulation(1, setStartPoint[i])[0];
            population[i].changeStartPoint(1);
        }
        PathComparatorAscCost asc = new PathComparatorAscCost();
        System.out.printf("=====initialized(%04.2f)=====\n", timer.toc());

        int currentGeneration = 0;
        while (currentGeneration < generationSize && !timer.isOver(Timer.SECOND_DEMO_LIMIT_SEC)) {
            Arrays.sort(this.population, asc);

            // 돌연변이 값 조절
            if (population[0].totalCost == prevCost) {
                notChangedCount++;
                adaptiveMutant = notChangedCount * 0.01 + 0.01;
            } else {
                notChangedCount = 0;
                adaptiveMutant = 0.01;
            }
            mutation.threshold = adaptiveMutant;
            prevCost = population[0].totalCost;

            // 한 세대당 시간 계산
            double now = timer.toc();
            if (currentGeneration > 1) {
                oneGenerationTime = now - oneGenerationTime;
                estimateGenerationTime = (estimateGenerationTime + oneGenerationTime) / 2.0d;
                oneGenerationTime = now;
            }
            else if (currentGeneration == 1) estimateGenerationTime = oneGenerationTime;
            else oneGenerationTime = now;
            // 디버깅용, 저장
            double t, cos;
            if (timer.tick()) {
                System.out.printf("GL cost [%.2f] @ g(%04d) t(%06.2f) period(%.2f) m(%.2f)\n",
                        cos = population[0].totalCost, currentGeneration, t = timer.toc(), estimateGenerationTime, mutation.threshold);
                memo.memo(t, cos);
            }


            ArrayList<Path> children = new ArrayList<>();
            int childrenMaxSize;
            // jugementday Protocol
            if (adaptiveMutant < 0.99) childrenMaxSize = populationSize/2;
            else {
                childrenMaxSize = populationSize - 1;
                adaptiveMutant = 0.01;
                notChangedCount = 0;
                mutation.threshold = adaptiveMutant;
            }
            while (children.size() < childrenMaxSize) {
                int[] parentsIdx = selection.select(population);
                Path[] child = crossover.crossover(population[parentsIdx[0]], population[parentsIdx[1]]);
                for (int i = 0; i < child.length; i++) {
                    child[i] = optimizer.optimize(child[i]);
                    mutation.mutate(child[i]);
                    child[i] = optimizer.optimize(child[i]);
                    children.add(child[i]);
                }
            }

            for(int p = populationSize - 1, c = 0; c < children.size(); p--, c++) {
                population[p] = children.get(c);
            }

            currentGeneration++;
        }
        Arrays.sort(population, asc);

        memo.memo(timer.toc(), population[0].totalCost);
        memo.save();
        population[0].write();
        return population[0];
    }

    @Deprecated
    @Override
    public Path calculatePath(Path path) {
        System.err.println("======NOT IMPLEMENTED. USE calculatePath(int startPoint)======");
        System.exit(1);
        return null;
    }
}
