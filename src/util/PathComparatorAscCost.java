package util;

import java.util.Comparator;

public class PathComparatorAscCost implements Comparator<Path> {

    @Override
    public int compare(Path o1, Path o2) {
        if(o1.totalCost < o2.totalCost) {
            return -1;
        } else if (o1.totalCost > o2.totalCost) {
            return 1;
        }

        return 0;
    }
}
