package Our;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import util.*;

public class RandomPath {
    public static Path	 getRandomPath(int startCity){
        int numOfCity = Map.getInstance().getNumOfCities();
        int [] path = new int[numOfCity + 1];

        List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < numOfCity; i++){
            if(i == startCity){
                continue;
            }
            list.add(i);
        }

        Collections.shuffle(list);

        path[0] = startCity;
        path[numOfCity] = startCity;

        for(int i = 0; i < list.size(); i++){
            path[i+1] = list.get(i);
        }
        Path p = new Path(path, 0.0);
        p.refreshCost();
        p.pathCheck();
        return p;
    }
}
