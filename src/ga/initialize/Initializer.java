package ga.initialize;

import util.Path;

public interface Initializer {

    Path[] initializePopulation(int populationSize, int startCity);

}
