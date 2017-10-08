package ga;

import ga.crossover.Crossover;
import ga.initialize.Initializer;
import ga.mutate.Mutation;
import ga.select.Selection;
import util.Path;
import util.TSP;

public class GASearch extends TSP {

    protected int populationSize;
    protected int generationSize;
    protected Path[] populationList;

    protected Initializer initializer;
    protected Crossover crossover;
    protected Mutation mutation;
    protected Selection selection;

    public GASearch(int populationSize, int generationSize) {
        this.populationSize = populationSize;
        this.generationSize = generationSize;
        this.populationList = null;

        this.initializer = null;
        this.crossover = null;
        this.mutation = null;
        this.selection = null;
    }

    public void setProcess(Initializer initializer, Crossover crossover, Mutation mutation, Selection selection) {
        this.initializer = initializer;
        this.crossover = crossover;
        this.mutation = mutation;
        this.selection = selection;
    }

    @Override
    public Path calculatePath(int startPoint) {
        return null;
    }

    @Override
    public Path calculatePath(Path path) {
        return null;
    }
}
