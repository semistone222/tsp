package util;

public abstract class TSP {

    protected int numOfCities;
    protected double distanceMap[][];

    public TSP() {
        Map map = Map.getInstance();
        this.numOfCities = map.getNumOfCities();
        this.distanceMap = map.getDistanceMap();
    }

    public abstract Path calculatePath(int startPoint);
    public abstract Path calculatePath(Path path);
}
