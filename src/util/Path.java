package util;

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
