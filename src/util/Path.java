package util;

import java.util.*;

public class Path {

    private double[][] distanceMap;

    public int[] order;
    public double totalCost;

    public Path(int[] order, double totalCost) {
        this.distanceMap = Map.getInstance().getDistanceMap();
        this.order = order;
        this.totalCost = totalCost;
    }

    /**
     * 2-opt
     * caution : start point or end point can't be selected.
     * @param lowerIndex first edge's the latter vertex index
     * @param higherIndex second edge'the former vertex index
     */
    public void twoOptSwap(int lowerIndex, int higherIndex) {
        if(lowerIndex < 1 || higherIndex > order.length - 2) {
            System.out.println("======TWO OPT SWAP ERROR======");
            System.exit(1);
        }

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
            System.out.println("======EDGE IS NOT ENOUGH======");
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
        for(int i = 0; i < order.length; i++) {
            copiedOrder[i] = order[i];
        }

        Path path = new Path(copiedOrder, totalCost);

        return path;
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

    public double refreshCost() {
        double newCost = 0.0;
        for(int i = 0; i < order.length - 1; i++) {
            int a = order[i];
            int b = order[i+1];
            newCost += distanceMap[a][b];
        }
        if (Math.abs(totalCost - newCost) > 0.01) {
            System.out.println("warning : cost auto refresh by float error");
            totalCost = newCost;
            return newCost;
        }

        return totalCost;
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
}
