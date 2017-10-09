package ga.select;

import util.Path;

import java.util.Random;

public class RouletteWheelSelection implements Selection {

    private final double k;

    public RouletteWheelSelection(double k) {
        this.k = k;
    }

    @Override
    public int[] select(Path[] population) {
        // important! assume that population is sorted by cost asc
        double sumOfFitness = 0;
        for(Path path : population) {
            sumOfFitness += fi(
                    population[population.length - 1].totalCost,
                    population[0].totalCost,
                    path.totalCost,
                    k
            );
        }

        // TODO : it seems to be problem. fix this.
        // for debug, delete this
        if(sumOfFitness == 0) {
            System.out.println("ALL POPULATION IS SAME");
        }

        int[] parentsIdx = new int[2];
        parentsIdx[0] = spin(population, sumOfFitness);
        parentsIdx[1] = spin(population, sumOfFitness);

        return parentsIdx;
    }

    private int spin(Path[] population, double sumOfFitness) {
        int ret = -1;
        Random random = new Random();
        double point = sumOfFitness * random.nextDouble();
        double sum = 0;

        for(int i = 0; i < population.length; i++) {
            Path path = population[i];

            sum += fi(
                    population[population.length - 1].totalCost,
                    population[0].totalCost,
                    path.totalCost,
                    k
            );

            if(point < sum) {
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
    private static double fi(double Cw, double Cb, double Ci, double k) {
        return (Cw - Ci) + (Cw - Cb) / (k - 1.0d);
    }
}
