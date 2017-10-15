package ga.crossover;

import util.Map;
import util.Path;
import util.Pick;

public class PartiallyMatchedCrossover implements Crossover {

    private final int numOfCities;

    public PartiallyMatchedCrossover() {
        this.numOfCities = Map.getInstance().getNumOfCities();
    }

    @Override
    public Path[] crossover(Path firstParent, Path secondParent) {
        Path[] child = new Path[2];

        Path firstChildren = firstParent.deepCopy();
        Path secondChildren = secondParent.deepCopy();

        int[] twoRandomCityIdx = Pick.getTwoRandomIndex(1, numOfCities - 1);
        int firstIdx = twoRandomCityIdx[0];
        int secondIdx = twoRandomCityIdx[1];

        for(int i = firstIdx; i <= secondIdx; i++) {
            int idx1 = getIndexOfCity(firstChildren.order, secondParent.order[i]);
            firstChildren.swapCity(i, idx1);
            int idx2 = getIndexOfCity(secondChildren.order, firstParent.order[i]);
            secondChildren.swapCity(i, idx2);
        }

        child[0] = firstChildren;
        child[1] = secondChildren;

        return child;
    }

    private int getIndexOfCity(int[] order, int city) {
        int ret = -1;

        for(int i = 0; i < order.length; i++) {
            if(order[i] == city) {
                ret = i;
                break;
            }
        }

        return ret;
    }
}
