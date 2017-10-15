package ga.crossover;

import util.Map;
import util.Path;
import util.Pick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import static java.lang.System.exit;

public class EdgeRecombination implements Crossover{

    private final int numOfCities;
    private HashMap<Integer, HashSet<Integer>> edgeTable = new HashMap<>();
    private HashMap<Integer,HashSet<Integer>> edgeOriginal = new HashMap<>();
    private ArrayList<Integer> saveUsed = new ArrayList<Integer>();

    public EdgeRecombination() {
        this.numOfCities=Map.getInstance().getNumOfCities();
    }

    @Override
    public Path[] crossover(Path firstParent, Path secondParent){
        Path[] child = new Path[1];
        int[] childPath;

        // City 별로 link 나누기
        edgeTable.clear();
        edgeTable = makeEdgeTable(firstParent, secondParent);
        saveUsed.clear();


        // 시작점과 link된 것들중 가장 적은 link를 보유하는 것을 다음 path로
        childPath = getChildPath(firstParent);


        // 자식값 반환
        Path childTemp = new Path(childPath, 0);
        childTemp.refreshCost();
        child[0]=childTemp;

        return child;
    }

    private int getStartCityParent(Path firstparent){
        return firstparent.order[0];
    }

    private HashMap<Integer, HashSet<Integer>> makeEdgeTable(Path firstParent, Path secondParent){

        for(int i=1; i<numOfCities+1; i++){
            HashSet<Integer> set = new HashSet<>();
            edgeTable.put(i, set);
        }
        addLinks(edgeTable, firstParent);
        addLinks(edgeTable, secondParent);
        return edgeTable;
    }

    private void addLinks(HashMap<Integer, HashSet<Integer>> tempTable, Path parent) {
        int length = parent.order.length-1;
        int[] path = parent.order;
        int edge, link;
        for(int i=0; i<length; i++){
            edge = path[i];
            link = path[i+1];
            tempTable.get(edge).add(link);
            tempTable.get(link).add(edge);


        }
    }

    private void printEdgeTable(){
        for(int i=1; i<edgeTable.size()+1; i++){
            System.out.print("Edge : "+ (i)+" ");
            System.out.println("Links : "+edgeTable.get(i).toString());
        }
    }

    private int getStartCityLeast(){
        int startPoint = 1;         // 원래는 1
        int leastSize = edgeTable.get(1).size();
        for(int i=1; i<edgeTable.size(); i++){
            if(leastSize > edgeTable.get(1).size()){
                startPoint = i;
                leastSize = edgeTable.get(1).size();
            }
        }
        return startPoint;
    }

    // 시작점을 가장 작은 link수로 할 경우 Path firstParent parameter 삭제
    private int[] getChildPath(Path firstParent){
        int[] childPath = new int[numOfCities+1];
        // 시작점 정하기
        // 시작점이 첫 부모의 첫번째 Path
        //childPath[0] = getStartCityParent(firstParent);

        //  시작점이 가장 link수가 작은것일 때
        childPath[0] = getStartCityLeast();
        saveUsed.add(childPath[0]);

        // 첫 path가 link된곳을 지움
        HashSet<Integer> first = edgeTable.get(childPath[0]);


        for(int i=1; i<numOfCities; i++){
            int leastCity=-1;
            int compareCity;
            int leastSize=-1;
            deleteCityInLinks(childPath[i-1]);
            Iterator<Integer> itr = edgeTable.get(childPath[i-1]).iterator();
            while(itr.hasNext()){
                if(leastCity == -1){
                    leastCity = itr.next();
                    leastSize = edgeTable.get(leastCity).size();

                }
                else{
                    compareCity = itr.next();
                    if(leastSize>edgeTable.get(compareCity).size()){
                        leastCity = compareCity;
                        leastSize = edgeTable.get(compareCity).size();
                    }
                }
            }
            if(leastCity == -1){
                // link가 없을경우 새로운 edge
                leastCity = getLeastLinked();
            }

            childPath[i] = leastCity;
            saveUsed.add(leastCity);
            Path p = new Path(childPath, 0);
            edgeTable.get(childPath[i-1]).clear();

        }
        childPath[numOfCities] = childPath[0];
        return childPath;
    }

    private void deleteCityInLinks(int city){
        if(!edgeTable.get(city).isEmpty()) {
            Iterator<Integer> itr = edgeTable.get(city).iterator();
            int cityHas;
            while (itr.hasNext()) {
                cityHas = itr.next();
                edgeTable.get(cityHas).remove(city);
            }
        }
    }

    private int getLeastLinked(){
        int leastLinked =-1;
        int leastSize = 99999;
        for(int i=0; i<edgeTable.size(); i++){
            if(!saveUsed.contains(i+1)){
                if(leastSize>edgeTable.get(i+1).size()){
                    leastLinked = i+1;
                    leastSize = edgeTable.get(i+1).size();
                }
            }
        }
        return leastLinked;
    }
}
