package ga;

import ga.crossover.Crossover;
import ga.initialize.Initializer;
import ga.mutate.Mutation;
import ga.optimize.Optimizer;
import ga.select.Selection;
import util.Path;
import util.PathComparatorAscCost;
import util.TSP;
import util.Timer;

import java.util.Arrays;

public class GeneticLocalSearch extends TSP {

    private int populationSize;
    private int generationSize;
    private Path[] population;

    private Initializer initializer;
    private Selection selection;
    private Crossover crossover;
    private Mutation mutation;
    private Optimizer optimizer;

    private Timer timer;

    public GeneticLocalSearch(int populationSize, int generationSize) {
        this.populationSize = populationSize;
        this.generationSize = generationSize;
        this.population = null;

        this.initializer = null;
        this.selection = null;
        this.crossover = null;
        this.mutation = null;
        this.optimizer = null;

        this.timer = new Timer();
    }

    public void setProcess(Initializer initializer, Selection selection, Crossover crossover, Mutation mutation, Optimizer optimizer) {
        this.initializer = initializer;
        this.selection = selection;
        this.crossover = crossover;
        this.mutation = mutation;
        this.optimizer = optimizer;
    }

    @Override
    public Path calculatePath(int startPoint) {
        timer.tic();
        population = initializer.initializePopulation(populationSize, startPoint);
        PathComparatorAscCost asc = new PathComparatorAscCost();

        int currentGeneration = 0;
        while (currentGeneration < generationSize && !timer.isOver(Timer.SECOND_DEMO_LIMIT_SEC)) {
            Arrays.sort(this.population, asc);

            // for debug
            if (timer.tick()) {
                System.out.println(
                        "generation : " + currentGeneration + ", "
                                + "time : " + timer.toc() + "s, "
                                + "cost : " + population[0].totalCost
                );
            }

            int childrenIdx = 0;
            int childrenSize = populationSize / 2;
            Path[] children = new Path[childrenSize];

            while (childrenIdx < childrenSize) {
                int[] parentsIdx = selection.select(population);
                Path[] child = crossover.crossover(population[parentsIdx[0]], population[parentsIdx[1]]);
                for (int i = 0; i < child.length; i++) {
                    child[i] = optimizer.optimize(child[i]);
                    mutation.mutate(child[i]);
                    child[i] = optimizer.optimize(child[i]);
                    children[childrenIdx++] = child[i];
                }
            }

            for(int p = populationSize - 1, c = 0; c < childrenSize; p--, c++) {
                population[p] = children[c];
            }
            currentGeneration++;
        }
        Arrays.sort(population, asc);
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
