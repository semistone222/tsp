package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Map {

    private static Map instance = null;

    private int numOfCities;
    private double distanceMap[][];
    private int centerCityId;
    private HashMap<Integer, City> cityHashMap;

    public static void setMapFile(String fileName) {
        instance = new Map(fileName);
        Path.setMapData();
    }

    public static Map getInstance() {
        return instance;
    }

    public int getNumOfCities() {
        return numOfCities;
    }

    public double[][] getDistanceMap() {
        return distanceMap;
    }

    public int getCenterCityId(){
        return centerCityId;
    }

    public HashMap<Integer, City> getCityHashMap() {
        return cityHashMap;
    }

    private Map(String fileName) {
        cityHashMap = new HashMap<>();
        this.readMap(fileName);
    }

    private void readMap(String fileName) {
        File file = new File(fileName);
        int minX, maxX, minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;

        try {
            Scanner sc = new Scanner(file);
            while(sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] strings = line.split(" ");
                int id = Integer.valueOf(strings[0]);
                int x = Integer.valueOf(strings[1]);
                int y = Integer.valueOf(strings[2]);
                City city = new City(id, x, y);
                cityHashMap.put(id, city);

                if(y < minY){
                    minY = y;
                }

                if(y > maxY){
                    maxY = y;
                }
            }
            sc.close();

            numOfCities = cityHashMap.size();
            setDistanceMap();

            minX = cityHashMap.get(1).x;
            maxX = cityHashMap.get(cityHashMap.size()).x;
            setCenterCityId(minX, maxX, minY, maxY);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("======FileNotFoundException======");
            System.exit(1);
        }

    }

    private void setDistanceMap() {
        distanceMap = new double[numOfCities + 1][numOfCities + 1];

        for(int i = 1; i <= numOfCities; i++) {
            distanceMap[i][i] = 0;
            for(int j = i + 1; j <= numOfCities; j++) {
                City iCity = cityHashMap.get(i);
                City jCity = cityHashMap.get(j);
                distanceMap[i][j] = calculateDistance(iCity.x, iCity.y, jCity.x, jCity.y);
                distanceMap[j][i] = distanceMap[i][j];
            }
        }
    }

    private double calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.hypot((double) (x1 - x2), (double) (y1 - y2));
    }

    private void setCenterCityId(int minX, int maxX, int minY, int maxY) {
        int tempX = (minX + maxX) / 2;
        int tempY = (minY + maxY) / 2;
        centerCityId = getClosestCityIndex(tempX, tempY);
    }

    private int getClosestCityIndex(int fromX, int fromY){
        int minIndex = 0;
        double minDist = Double.MAX_VALUE;
        for(int i = 1; i <= this.numOfCities; i++){
            City city = cityHashMap.get(i);
            double dist = calculateDistance(fromX, fromY, city.x, city.y);
            if(minDist > dist){
                minIndex = i;
                minDist = dist;
            }
        }

        return minIndex;
    }

    public void printCityHashMap() {
        System.out.println("======CITY HASHMAP======");
        for(Integer i : cityHashMap.keySet()) {
            City city = cityHashMap.get(i);
            System.out.println(city.id + " " + city.x + " " + city.y);
        }
    }

    public void printDistanceMap() {
        System.out.println("======DISTANCE MAP======");
        for(int i = 1; i <= numOfCities; i++) {
            for (int j = 1; j <= numOfCities; j++) {
                System.out.print((int) distanceMap[i][j] + " ");
            }
            System.out.println();
        }
    }
}
