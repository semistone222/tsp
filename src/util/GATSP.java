package util;

import ga.initialize.Initializer;
import ga.select.Selection;
import ga.crossover.Crossover;
import ga.mutate.Mutation;

public abstract class GATSP {
    protected int numOfCities;
    protected double distanceMap[][];

    protected Initializer initializer;
    protected Selection selection;
    protected Crossover crossover;
    protected Mutation mutation;

    protected int generation;
    protected Timer timer;

    public GATSP() {
        this.timer = new Timer();
        timer.tic();

        Map map = Map.getInstance();
        this.numOfCities = map.getNumOfCities();
        this.distanceMap = map.getDistanceMap();

        this.initializer = null;
        this.selection = null;
        this.crossover = null;
        this.mutation = null;

        this.generation = 0;
    }

    public void setProcess(Initializer initializer, Selection selection, Crossover crossover, Mutation mutation) {
        this.initializer = initializer;
        this.selection = selection;
        this.crossover = crossover;
        this.mutation = mutation;
    }

    public abstract Path calculatePath();
}
