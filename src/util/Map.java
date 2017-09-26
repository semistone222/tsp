package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Map {

    private static Map instance = null;

    private int numOfCities;
    private double distanceMap[][];
    private static int startPointID;
    private HashMap<Integer, City> cityHashMap;

    public static void setMapFile(String fileName) {
        instance = new Map(fileName);
    }

    public static Map getInstance() {
        return instance;
    }

    // 중심점 찾기
    public static int getStartPt(){return startPointID;}

    public int getNumOfCities() {
        return numOfCities;
    }

    public double[][] getDistanceMap() {
        return distanceMap;
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
        int min_x, max_x, min_y=999999, max_y=0;

        try {
            Scanner sc = new Scanner(file);
            while(sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] strings = line.split(" ");
                int id = Integer.valueOf(strings[0]);
                int x = Integer.valueOf(strings[1]);
                int y = Integer.valueOf(strings[2]);
                if(y<min_y){
                    min_y = y;
                }
                if(y>max_y){
                    max_y = y;
                }
                City city = new City(id, x, y);
                cityHashMap.put(id, city);
            }
            sc.close();

            numOfCities = cityHashMap.size();
            setDistanceMap();

            // min,max x좌표 설정
            min_x = cityHashMap.get(1).x;
            max_x = cityHashMap.get(cityHashMap.size()).x;

            // 무게중심 구하기
            int temp_x, temp_y;
            temp_x = (min_x + max_x)/2;
            temp_y = (min_y + max_y)/2;
            startPointID = getStartID(temp_x, temp_y);

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

    private int getStartID(int temp_x, int temp_y){
        int index = 0;
        double temp;
        double dist_min = calculateDistance(temp_x, temp_y, cityHashMap.get(1).x, cityHashMap.get(1).y);
        for(int i=0; i<this.numOfCities; i++){
            temp = calculateDistance(temp_x, temp_y, cityHashMap.get(i+1).x, cityHashMap.get(i+1).y);
            if(dist_min>temp){
                index = i+1;
                dist_min=temp;
            }
        }
        return index;
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
