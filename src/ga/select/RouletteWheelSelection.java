package ga.select;

import util.Path;

import java.util.Random;

public class RouletteWheelSelection implements Selection {

    private final double k;

    public RouletteWheelSelection(double k) {
        this.k = k;
    }

    @Override
    public Path[] select(Path[] population) {
        // assume that population is sorted by cost asc
        double sumOfFitness = 0;
        for(Path path : population) {
            sumOfFitness += fi(
                    population[population.length - 1].totalCost,
                    population[0].totalCost,
                    path.totalCost,
                    k
            );
        }

        Path[] parents = new Path[2];
        parents[0] = spin(population, sumOfFitness);
        parents[1] = spin(population, sumOfFitness);

        return parents;
    }

    private Path spin(Path[] population, double sumOfFitness) {
        Path ret = population[0];
        Random random = new Random();
        double point = sumOfFitness * random.nextDouble();
        double sum = 0;

        for(Path path : population) {
            sum += fi(
                    population[population.length - 1].totalCost,
                    population[0].totalCost,
                    path.totalCost,
                    k
            );

            if(point < sum) {
                ret = path;
                break;
            }
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
    private static double fi(double Cw, double Cb, double Ci, double k) {
        return (Cw - Ci) + (Cw - Cb) / (k - 1.0d);
    }
}
