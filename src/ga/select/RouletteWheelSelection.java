package ga.select;

import util.Path;

import java.util.Random;

public class RouletteWheelSelection implements Selection {
    private double costMax, costMin, costDiff;
    private final double k;
    private double probTable[], sumOfFitness;

    public RouletteWheelSelection(double k) { this.k = k; }

    @Override
    public int[] select(Path[] population) { return _select(population, 2); }

    private int[] _select(Path[] population, int num) {
        // important! assume that population is sorted by cost asc
        costMax = population[population.length - 1].totalCost + 1;
        costMin = population[0].totalCost;
        costDiff = costMax - costMin;

        probTable = new double[population.length];
        for (int i = 0; i < population.length; i++) probTable[i] += fi(population[i].totalCost);
        sumOfFitness = probTable[population.length - 1];

        // TODO : it seems to be problem. fix this.
        // for debug, delete this)
        if(Math.abs(sumOfFitness - 0.0d) < 0.001d) {
            System.out.println("ALL POPULATION IS SAME");
        }

        int[] parentsIdx = new int[num];
        for (int i = 0; i < num; i++) parentsIdx[i] = spin();
        return parentsIdx;
    }

    private int spin() {
        int ret = -1;
        Random random = new Random();
        double point = sumOfFitness * random.nextDouble();

        for(int i = 0; i < probTable.length; i++) {
            if(point < probTable[i]) {
                ret = i;
                break;
            }
        }

        if(ret == -1) {
            System.err.println("======ROULETTE ERR======");
            System.exit(1);
        }

        return ret;
    }

    /**
     * get fitness of solution i
     * @param Cw cost of worst solution in solution set
     * @param Cb cost of best solution in solution set
     * @param Ci cost of solution i
     * @param k constant, should be larger than 1. generally, a value of 3 to 4 is used.
     * @return fitness of solution i
     */
    private double fi(double Ci) {
        return (costMax - Ci) + costDiff/(k - 1.0d);
    }
}
