package ga.crossover;

import util.Path;

import java.util.*;

public class EdgeRecombinationCrossover implements Crossover {

    @Override
    public Path[] crossover(Path firstParent, Path secondParent) {
        int[] childrenOrder = combine(firstParent.order, secondParent.order);
        Path children = new Path(childrenOrder, 0);
        children.refreshCost();
        Path[] child = {children};
        return child;
    }

    public int[] combine(int[] order1, int[] order2) {
        if(order1.length != order2.length) {
            System.err.println("======CANNOT COMBINE======");
            System.exit(1);
        }

        HashMap<Integer, HashSet<Integer>> adjacencyMap = new HashMap<>();
        int orderSize = order1.length;

        for(int i = 0; i < orderSize - 1; i++) {
            int city = order1[i];
            HashSet<Integer> adjacencySet = adjacencyMap.get(city);

            if(adjacencySet == null) {
                adjacencySet = new HashSet<>();
                adjacencyMap.put(city, adjacencySet);
            }

            if(i == 0) {
                adjacencySet.add(order1[orderSize - 2]);
            } else {
                adjacencySet.add(order1[i - 1]);
            }
            adjacencySet.add(order1[i + 1]);
        }

        for(int i = 0; i < orderSize - 1; i++) {
            int city = order2[i];
            HashSet<Integer> adjacencySet = adjacencyMap.get(city);

            if(adjacencySet == null) {
                adjacencySet = new HashSet<>();
                adjacencyMap.put(city, adjacencySet);
            }

            if(i == 0) {
                adjacencySet.add(order2[orderSize - 2]);
            } else {
                adjacencySet.add(order2[i - 1]);
            }
            adjacencySet.add(order2[i + 1]);
        }

        int[] childrenOrder = new int[orderSize];
        int startCity = order1[0];
        int currentCity = startCity;
        int childrenOrderIdx = 0;

        while(childrenOrderIdx < orderSize - 1) {

            childrenOrder[childrenOrderIdx++] = currentCity;
            for(Integer i : adjacencyMap.keySet()) {
                HashSet<Integer> adjacencySet = adjacencyMap.get(i);
                adjacencySet.remove(currentCity);
            }

            HashSet<Integer> currentAdjacencySet = adjacencyMap.get(currentCity);

            int nextCity = -1;
            if(currentAdjacencySet != null && currentAdjacencySet.size() != 0) {
                int min = Integer.MAX_VALUE;
                for(Integer next : currentAdjacencySet) {
                    HashSet<Integer> nextAdjacencySet = adjacencyMap.get(next);
                    if(min > nextAdjacencySet.size()) {
                        min = nextAdjacencySet.size();
                        nextCity = next;
                    }
                }
            } else {
                for(Integer i : adjacencyMap.keySet()) {
                    nextCity = i;
                    break;
                }
            }

            adjacencyMap.remove(currentCity);
            currentCity = nextCity;
        }

        childrenOrder[orderSize - 1] = startCity;

        return childrenOrder;
    }
}
