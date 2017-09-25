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
        if(lowerIndex < 1 || higherIndex > order.length - 1) {
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
}
