package ga.crossover;

import util.Path;

public interface Crossover {

    Path[] crossover(Path firstParent, Path secondParent);

}
