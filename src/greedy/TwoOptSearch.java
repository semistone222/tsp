package greedy;

import util.Path;
import util.TSP;

public class TwoOptSearch extends TSP {

    private int limitTrial;

    public TwoOptSearch(int limitTrial) {
        this.limitTrial = limitTrial;
    }

    @Override
    public Path calculatePath(int startPoint) {
        NearestNeighbor nearestNeighbor = new NearestNeighbor();
        Path path = nearestNeighbor.calculatePath(startPoint);
        return calculatePath(path);
    }

    @Override
    public Path calculatePath(Path path) {
        int trial = 0;
        while(trial < limitTrial) {
            // TODO : Deep Copy
            // https://geunhokim.wordpress.com/2013/06/15/deep-copy-shallow-copy/
            // TODO : 1) Random Pick
            // TODO : 2) 2 Long Edge Pick With Tabu Memory
            // http://asuraiv.blogspot.kr/2015/11/java-priorityqueue.html
            // TODO : 3) Weighted Random Pick
            // https://www.google.co.kr/search?q=java+weighted+random&oq=java+weighted+random&aqs=chrome..69i57j69i60l2j0l3.4539j0j7&sourceid=chrome&ie=UTF-8

        }
        return null;
    }
}
