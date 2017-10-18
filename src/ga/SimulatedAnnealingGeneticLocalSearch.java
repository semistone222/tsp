package ga;

import ga.crossover.Crossover;
import ga.initialize.Initializer;
import ga.mutate.Mutation;
import ga.optimize.Optimizer;
import ga.select.Selection;
import sa.Cooling;
import util.Path;
import util.PathComparatorAscCost;
import util.TSP;
import util.Timer;

import java.util.ArrayList;
import java.util.Arrays;

public class SimulatedAnnealingGeneticLocalSearch extends TSP {

    private int populationSize;
    private int generationSize;
    private Path[] population;

    private Initializer initializer;
    private Selection selection;
    private Crossover crossover;
    private Mutation mutation;
    private Optimizer optimizer;

    private final double T0;
    private double T;

    private Timer timer;

    public SimulatedAnnealingGeneticLocalSearch(int populationSize, int generationSize, double T0) {
        this.populationSize = populationSize;
        this.generationSize = generationSize;
        this.population = null;

        this.initializer = null;
        this.selection = null;
        this.crossover = null;
        this.mutation = null;
        this.optimizer = null;

        if (T0 <= 0) {
            System.out.println("======INITIAL TEMPERATURE SHOULD BE BIGGER THAN 0======");
            System.exit(1);
        }

        this.T0 = T0;
        this.T = T0;

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
        int k = 0;
        while (currentGeneration < generationSize && !timer.isOver(Timer.SECOND_DEMO_LIMIT_SEC)) {
            Arrays.sort(this.population, asc);

            // for debug
            if (timer.tick()) {
                System.out.println(
                        "generation : " + currentGeneration + ", "
                                + "time : " + timer.toc() + "s, "
                                + "T : " + T + ", "
                                + "cost : " + population[0].totalCost
                );
            }

            ArrayList<Path> children = new ArrayList<>();
            int childrenMaxSize = populationSize / 2;
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

            int p = populationSize - 1;
            for(int c = 0; c < children.size(); c++) {
                Path child = children.get(c);
                if(population[0].totalCost > child.totalCost) {
                    population[p--] = child;
                } else if (Math.random() < Math.pow(Math.E, (population[0].totalCost - child.totalCost) / T)) {
                    population[p--] = child;
                }
            }

            T = Cooling.exponentialMultiplicativeCooling(T0, 0.99, k);
            k++;
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
