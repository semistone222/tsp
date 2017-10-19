package util;

import javafx.util.Pair;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Path {

    private static double[][] distanceMap;
    public static void setMapData() {
        distanceMap = Map.getInstance().getDistanceMap();
    }

    public int[] order;
    public double totalCost;
    public Pair<Integer, Integer> recentlySwappedPair;

    public Path(int[] order, double totalCost) {
        this.order = order;
        this.totalCost = totalCost;
        this.recentlySwappedPair = new Pair<>(null, null);
    }

    public Path(int[] order, double totalCost, Pair<Integer, Integer> pair) {
        this.order = order;
        this.totalCost = totalCost;
        this.recentlySwappedPair = new Pair<>(pair.getKey(), pair.getValue());
    }

    /**
     * 2-opt
     * caution : start point or end point can't be selected.
     * @param lowerIndex first edge's the latter vertex index
     * @param higherIndex second edge'the former vertex index
     */
    public void twoOptSwap(int lowerIndex, int higherIndex) {
        if(lowerIndex < 1 || higherIndex > order.length - 2) {
            System.err.println("======TWO OPT SWAP ERROR======");
            System.exit(1);
        }

        recentlySwappedPair = new Pair<>(lowerIndex, higherIndex);

        totalCost -= distanceMap[order[lowerIndex - 1]][order[lowerIndex]];
        totalCost -= distanceMap[order[higherIndex]][order[higherIndex + 1]];
        totalCost += distanceMap[order[lowerIndex - 1]][order[higherIndex]];
        totalCost += distanceMap[order[lowerIndex]][order[higherIndex + 1]];

        int[] newOrder = new int[order.length];
        int index = 0;
        for(int i = 0; i <= lowerIndex - 1; i++) {
            newOrder[index++] = order[i];
        }
        for(int i = higherIndex; i >= lowerIndex; i--) {
            newOrder[index++] = order[i];
        }
        for(int i = higherIndex + 1; i < order.length; i++) {
            newOrder[index++] = order[i];
        }

        order = newOrder;
    }

    /**
     * swap city
     * caution : start point or end point can't be selected.
     * @param formerIdx former city's index
     * @param latterIdx latter city's index
     */
    public void swapCity(int formerIdx, int latterIdx) {
        if(formerIdx < 1 || latterIdx > order.length - 2) {
            System.err.println("======SWAP CITY ERROR======");
            System.exit(1);
        }

        recentlySwappedPair = new Pair<>(formerIdx, latterIdx);

        totalCost -= distanceMap[order[formerIdx - 1]][order[formerIdx]];
        totalCost -= distanceMap[order[formerIdx]][order[formerIdx + 1]];
        totalCost -= distanceMap[order[latterIdx - 1]][order[latterIdx]];
        totalCost -= distanceMap[order[latterIdx]][order[latterIdx + 1]];

        int temp = order[formerIdx];
        order[formerIdx] = order[latterIdx];
        order[latterIdx] = temp;

        totalCost += distanceMap[order[formerIdx - 1]][order[formerIdx]];
        totalCost += distanceMap[order[formerIdx]][order[formerIdx + 1]];
        totalCost += distanceMap[order[latterIdx - 1]][order[latterIdx]];
        totalCost += distanceMap[order[latterIdx]][order[latterIdx + 1]];
    }

    public int[] getTwoLongEdgeIndex(boolean[][] tried) {
        LinkedList<Edge> edgeList = new LinkedList<>();
        for(int i = 0; i < order.length - 1; i++) {
            Edge e = new Edge(order[i], order[i + 1], distanceMap[order[i]][order[i + 1]]);
            edgeList.add(e);
        }

        Collections.sort(edgeList, new Comparator<Edge>() {
            @Override
            public int compare(Edge o1, Edge o2) {
                if(o1.distance > o2.distance) {
                    return -1;
                } else if(o1.distance < o2.distance) {
                    return 1;
                }
                return 0;
            }
        });

        Edge firstLongest = edgeList.poll();
        Edge secondLongest = edgeList.poll();

        if(firstLongest == null || secondLongest == null ) {
            System.err.println("======EDGE IS NOT ENOUGH======");
            System.exit(1);
        }

        while(tried[firstLongest.end][secondLongest.start]) {
            firstLongest = edgeList.poll();
            secondLongest = edgeList.poll();
        }

        int[] ret = new int[2];
        ret[0] = Math.min(firstLongest.end, secondLongest.start);
        ret[1] = Math.max(firstLongest.end, secondLongest.start);

        return ret;
    }

    public Edge getDistanceWeightedRandomEdge() {
        double total = 0;
        LinkedList<Edge> edgeList = new LinkedList<>();
        for(int i = 0; i < order.length - 1; i++) {
            Edge e = new Edge(order[i], order[i + 1], distanceMap[order[i]][order[i + 1]]);
            edgeList.add(e);
            total += e.distance;
        }

        int randomIndex = -1;
        double random = Math.random() * total;
        for(int i = 0; i < edgeList.size(); i++) {
            random -= edgeList.get(i).distance;
            if(random < 0.0d) {
                randomIndex = i;
                break;
            }
        }

        return edgeList.get(randomIndex);
    }

    public int[] getTwoDistanceWeightedRandomIndex() {
        Edge randomEdge1 = getDistanceWeightedRandomEdge();
        Edge randomEdge2 = getDistanceWeightedRandomEdge();

        while(randomEdge1.end == 0 || randomEdge1.end == order.length - 1 || randomEdge1.equals(randomEdge2)) {
            randomEdge1 = getDistanceWeightedRandomEdge();
        }

        while(randomEdge2.start == 0 || randomEdge2.start == order.length - 1 || randomEdge1.equals(randomEdge2)) {
            randomEdge2 = getDistanceWeightedRandomEdge();
        }

        int[] ret = new int[2];
        ret[0] = Math.min(randomEdge1.end, randomEdge2.start);
        ret[1] = Math.max(randomEdge1.end, randomEdge2.start);

        return ret;
    }

    public Path deepCopy() {
        int[] copiedOrder = new int[order.length];
        System.arraycopy(order, 0, copiedOrder, 0, order.length);
        return new Path(copiedOrder, totalCost, recentlySwappedPair);
    }

    public void printOrder() {
        System.out.println("======PATH ORDER======");
        for(int i = 0; i < order.length; i++) {
            System.out.print(order[i] + " ");
        }
        System.out.println();
    }

    public void printTotalCost() {
        System.out.println("======TOTAL COST======");
        System.out.println(totalCost);
    }

    public void refreshCost() {
        double newCost = 0.0;
        for(int i = 0; i < order.length - 1; i++) {
            int a = order[i];
            int b = order[i+1];
            newCost += distanceMap[a][b];
        }

        totalCost = newCost;
    }

    public void threeOptSwap(int indexA, int indexB, int indexC) {
        if (indexC == Map.getInstance().getNumOfCities() + 1) {
            System.err.println("threeOptSwap : OutOfIndexError");
            System.exit(1);
        }

        Path [] seven = new Path[7];
        /* type
         * DEFAULT AB-1-CD-2-EF-3-GA
         * 0 : ABCD[FE]GA
         * 1 : AB[DC][FE]GA
         * 2 : AB[DC]EFGA
         *
         * 3 : AB[FE][DC]GA
         * 4 : AB[FE][CD]GA
         * 5 : AB[EF][CD]GA
         * 6 : AB[EF][DC]GA
         */
        seven[0] = this.deepCopy();                         // ABCDEFGA
        seven[0].twoOptSwap(indexB + 1, indexC); // ABCDFEGA
        seven[1] = seven[0].deepCopy();                     // ABCDFEGA
        seven[1].twoOptSwap(indexA + 1, indexB); // ABDCFEGA
        seven[2] = this.deepCopy();                         // ABCDEFGA
        seven[2].twoOptSwap(indexA + 1, indexB); // ABDCEFGA

        seven[3] = this.deepCopy();                         // ABCDEFGA
        seven[3].twoOptSwap(indexA + 1, indexC); // ABFEDCGA
        seven[4] = seven[3].deepCopy();                     // ABFEDCGA
        seven[4].twoOptSwap(indexB + 1, indexC); // ABFECDGA
        seven[5] = seven[4].deepCopy();                     // ABFECDGA
        seven[5].twoOptSwap(indexA + 1, indexB); // ABEFCDGA
        seven[6] = seven[3].deepCopy();                     // ABFEDCGA
        seven[6].twoOptSwap(indexA + 1, indexB); // ABEFDCGA

        double minCost = seven[0].totalCost;
        int minIdx = 0;
        for(int i = 1; i < 7; i++) {
            if (minCost > seven[i].totalCost) {
                minCost = seven[i].totalCost;
                minIdx = i;
            }
        }

        System.arraycopy(seven[minIdx].order, 0, order, 0, order.length);
        totalCost = minCost;
    }

    /**
     * caseThreeOpt
     * 신속한 3opt를 하면서 2opt 중복은 빼고,
     * 모두 경로를 바꿔보는게 아니라
     * Cost만 먼저 계산해보고 나중에 가장 낮은 cost로 경로변경을 시행해서
     * 속도를 높이고자 하였습니다
     */
    public void caseThreeOpt(int firstIdx, int secondIdx, int thirdIdx) {
        if (secondIdx < firstIdx || thirdIdx < secondIdx) {
            System.err.println("climbThreeOptCost, idx not sort");
            System.exit(0);
        }
        if (firstIdx < 0 || thirdIdx >= order.length - 1) {
            System.err.println("climbThreeOptCost, out of array index");
            System.exit(0);
        }
        /* type
        * DEFAULT AB-1-CD-2-EF-3-GA
        * X : ABCD[FE]GA
        * 0 : AB[DC][FE]GA
        * X : AB[DC]EFGA
        *
        * X : AB[FE][DC]GA
        * 1 : AB[FE][CD]GA
        * 2 : AB[EF][CD]GA
        * 3 : AB[EF][DC]GA
        */
        // 시티맵의 범위는 [1, MAX]
        // 인덱스 구성은 0(start) : MAX-1(last) : MAX(start) 로 되어있음
        // 즉, 인덱스 범위는 엣지로 구성되므로 [0, MAX-1] 임
        int B = order[firstIdx];
        int C = order[firstIdx + 1];
        int D = order[secondIdx];
        int E = order[secondIdx + 1];
        int F = order[thirdIdx];
        int G = order[thirdIdx + 1];

        double candidate[] = new double[5];
        candidate[4] = this.totalCost
                - distanceMap[B][C]
                - distanceMap[D][E]
                - distanceMap[F][G];
        // 0 : AB[DC][FE]GA
        candidate[0] = candidate[4]
                + distanceMap[B][D]
                + distanceMap[C][F]
                + distanceMap[E][G];
        // 1 : AB[FE][CD]GA
        candidate[1] = candidate[4]
                + distanceMap[B][F]
                + distanceMap[E][C]
                + distanceMap[D][G];
        // 2 : AB[EF][CD]GA
        candidate[2] = candidate[4]
                + distanceMap[B][E]
                + distanceMap[F][C]
                + distanceMap[D][G];
        // 3 : AB[EF][DC]GA
        candidate[3] = candidate[4]
                + distanceMap[B][E]
                + distanceMap[F][D]
                + distanceMap[C][G];

        int ret = 4;
        for(int i = 0; i < 4; i++)
            if(candidate[ret] > candidate[i])
                ret = i;

        if (ret == 4) return;

        int bdx = firstIdx;
        int cdx = firstIdx + 1;
        int ddx = secondIdx;
        int edx = secondIdx + 1;
        int fdx = thirdIdx;
        int gdx = thirdIdx + 1;
        int newOrder[] = new int[order.length];

        // DEFAULT AB-1-CD-2-EF-3-GA
        for (int i = 0; i <= bdx; i++) newOrder[i] = order[i];
        for (int i = gdx; i < order.length; i++) newOrder[i] = order[i];
        switch (ret) {
            case 0:
                // 0 : AB[DC][FE]GA
                for (int i = 0; i <= ddx - cdx; i++) newOrder[cdx + i] = order[ddx - i];
                for (int i = 0; i <= fdx - edx; i++) newOrder[edx + i] = order[fdx - i];
                totalCost = candidate[ret];
                break;
            case 1:
                // 1 : AB[FE][CD]GA
                for (int i = 0; i <= ddx - cdx; i++) newOrder[cdx + i] = order[fdx - i];
                for (int i = 0; i <= fdx - edx; i++) newOrder[edx + i] = order[cdx + i];
                totalCost = candidate[ret];
                break;
            case 2:
                // 2 : AB[EF][CD]GA
                for (int i = 0; i <= ddx - cdx; i++) newOrder[cdx + i] = order[edx + i];
                for (int i = 0; i <= fdx - edx; i++) newOrder[edx + i] = order[cdx + i];
                totalCost = candidate[ret];
                break;
            case 3:
                // 3 : AB[EF][DC]GA
                for (int i = 0; i <= ddx - cdx; i++) newOrder[cdx + i] = order[edx + i];
                for (int i = 0; i <= fdx - edx; i++) newOrder[edx + i] = order[ddx - i];
                totalCost = candidate[ret];
                break;
            default:
        }

    }

    public PathState checkState() {
        boolean [] visited = new boolean[Map.getInstance().getNumOfCities() + 1];
        if (order[0] != order[order.length - 1]) {
            return PathState.NOT_THE_SAME_START_AS_END;
        }

        visited[order[0]] = true;

        for(int i = 1 ; i < order.length - 1; i++) {
            if (visited[order[i]]) {
                return PathState.VISITED_DUPLICATELY;
            } else {
                visited[order[i]] = true;
            }
        }

        for(int i = 1; i < visited.length; i++) {
            if (!visited[i]) {
                return PathState.NOT_ALL_VISITED;
            }
        }

        return PathState.GOOD;
    }

    public void printState() {
        PathState pathState = checkState();
        switch (pathState) {
            case GOOD:
                System.out.println("======PATH IS GOOD======");
                break;
            case NOT_THE_SAME_START_AS_END:
                System.err.println("======PATH WRONG : START != END======");
                break;
            case VISITED_DUPLICATELY:
                System.err.println("======PATH WRONG : VISITED DUPLICATELY======");
                break;
            case NOT_ALL_VISITED:
                System.err.println("======PATH WRONG : NOT_ALL_VISITED======");
                break;
        }
    }

    public enum PathState {
        GOOD, NOT_THE_SAME_START_AS_END, VISITED_DUPLICATELY, NOT_ALL_VISITED
    }

    public void write() {
        try {
            FileWriter fw = new FileWriter("result.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.print(totalCost + ", path: ");
            for (int i = 0; i < order.length; i++) {
                out.print(order[i] + " ");
            }
            out.print("\n");
            out.close();
            bw.close();
            fw.close();
        } catch (IOException e) {
            System.exit(1);
        }
    }

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

        return randomPath;
    }

    // 으 - 악
    public void LinKernighan() {
        //G* 는 0
        //t1을 잡는다
        //t1과 인접한점 x1을 만든다
   }

   public void changeStartPoint(int startID) {
        if (order[0] == startID) return;

        int i;
        for (i = 1; i < (order.length - 1); i++)
            if (order[i] == startID) break;

        int newOrder[] = new int[order.length];
        newOrder[0] = newOrder[order.length - 1] = startID;
        System.arraycopy(order, i, newOrder, 0, order.length - 1 - i);
        System.arraycopy(order, 0, newOrder, order.length - 1 - i, i + 1);
        System.arraycopy(newOrder, 0, order, 0, order.length);
   }
}
