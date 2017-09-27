package Our;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import util.*;

public class RandomPath {
    public static Path getRandomPath(int startCity){
        int numOfCities = Map.getInstance().getNumOfCities();
        int [] order = new int[numOfCities + 1];

        List<Integer> list = new ArrayList<>();
        for(int i = 1; i <= numOfCities; i++){
            if(i == startCity){
                continue;
            }
            list.add(i);
        }

        Collections.shuffle(list);

        order[0] = startCity;
        order[numOfCities] = startCity;

        for(int i = 0; i < list.size(); i++){
            order[i+1] = list.get(i);
        }

        Path randomPath = new Path(order, 0.0);
        randomPath.refreshCost();

        // debug, delete this.
        randomPath.printState();

        return randomPath;
    }
}
