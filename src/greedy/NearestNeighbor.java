package greedy;

import util.Path;
import util.TSP;

import java.util.Arrays;

public class NearestNeighbor extends TSP {

    @Override
    public Path calculatePath(int startPoint) {
        int[] order = new int[numOfCities + 1];
        order[0] = startPoint;
        order[numOfCities] = startPoint;
        return calculatePath(new Path(order, 0));
    }

    @Override
    public Path calculatePath(Path path) {
        boolean[] visited = new boolean[numOfCities + 1];
        Arrays.fill(visited, false);
        visited[path.order[0]] = true;

        for (int i = 0; i < numOfCities - 1; i++) {
            int cityId = path.order[i];

            int indexOfNearestCity = -1;
            double minDistance = Double.MAX_VALUE;
            for (int j = 1; j <= numOfCities; j++) {
                if(!visited[j] && minDistance > distanceMap[cityId][j]) {
                    indexOfNearestCity = j;
                    minDistance = distanceMap[cityId][j];
                }
            }

            path.order[i + 1] = indexOfNearestCity;
            path.totalCost += minDistance;
            visited[indexOfNearestCity] = true;
        }

        double lastDistance = distanceMap[path.order[numOfCities - 1]][path.order[numOfCities]];
        path.totalCost += lastDistance;

        return path;
    }
}
