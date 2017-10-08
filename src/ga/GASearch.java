package ga;

import ga.crossover.Crossover;
import ga.initialize.Initializer;
import ga.mutate.Mutation;
import ga.select.Selection;
import util.Path;
import util.PathComparatorAscCost;
import util.TSP;

import java.util.Arrays;

/**
 * TODO : further research required
 * http://www.aistudy.com/biology/genetic/operator_moon.htm
 * https://ko.wikipedia.org/wiki/%EC%9C%A0%EC%A0%84_%EC%95%8C%EA%B3%A0%EB%A6%AC%EC%A6%98
 * https://en.wikipedia.org/wiki/Genetic_algorithm
 * https://kr.mathworks.com/discovery/genetic-algorithm.html
 * memetic algorithm (genetic local search)
 * http://kowon.dongseo.ac.kr/~dkkang/AI2011Fall/W0910.pdf page 8
 */
public class GASearch extends TSP {

    private int populationSize;
    private int generationSize;
    private Path[] population;

    private Initializer initializer;
    private Selection selection;
    private Crossover crossover;
    private Mutation mutation;

    public GASearch(int populationSize, int generationSize) {
        this.populationSize = populationSize;
        this.generationSize = generationSize;
        this.population = null;

        this.initializer = null;
        this.selection = null;
        this.crossover = null;
        this.mutation = null;
    }

    public void setProcess(Initializer initializer, Selection selection, Crossover crossover, Mutation mutation) {
        this.initializer = initializer;
        this.selection = selection;
        this.crossover = crossover;
        this.mutation = mutation;
    }

    @Override
    public Path calculatePath(int startPoint) {
        // generate first generation
        this.population = initializer.initializePopulation(populationSize, startPoint);

        // using cost ascending comparator
        PathComparatorAscCost asc = new PathComparatorAscCost();

        // TODO : it is hard to say that this is GA... because only two child is processed in each generation. am i right?
        // repeat for generationSize
        for(int i = 0; i < generationSize; i++) {
            // sort by cost ascending
            // this.population[0] is ith generationScore
            Arrays.sort(this.population, asc);

            // select parent in population
            int[] parentsIdx = this.selection.select(population);

            // crossover to make child
            Path[] child = this.crossover.crossover(population[parentsIdx[0]], population[parentsIdx[1]]);

            // mutate child with low probability
            this.mutation.mutate(child[0]);
            this.mutation.mutate(child[1]);

            // replace two worst solution with new child
            this.population[populationSize - 2] = child[0];
            this.population[populationSize - 1] = child[1];
        }

        Arrays.sort(this.population, asc);

        // return the best solution
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
