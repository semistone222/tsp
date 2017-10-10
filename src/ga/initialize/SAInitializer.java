package ga.initialize;

import util.Path;

import java.util.Random;

public class SAInitializer implements Initializer {

    private final double T0;
    private final int numOfIteration;

    public SAInitializer(double T0, int numOfIteration) {
        if (T0 <= 0) {
            System.out.println("======INITIAL TEMPERATURE SHOULD BE BIGGER THAN 0======");
            System.exit(1);
        }

        this.T0 = T0;
        this.numOfIteration = numOfIteration;
    }

    @Override
    public Path[] initializePopulation(int populationSize, int startCity) {
        Random random = new Random();
        Path[] population = new Path[populationSize];
        for(int i = 0; i < populationSize; i++) {
            int randomTrial = (int) (this.numOfIteration * random.nextDouble());
            SA sa = new SA(T0, randomTrial);
            population[i] = sa.calculatePath(Path.getRandomPath(startCity));
        }

        return population;
    }
}
