package ga.initialize;

import util.Path;

public class RandomInitializer implements Initializer {

    @Override
    public Path[] initializePopulation(int populationSize, int startCity) {
        Path[] population = new Path[populationSize];
        for(int i = 0; i < populationSize; i++) {
            population[i] = Path.getRandomPath(startCity);
        }

        return population;
    }
}
