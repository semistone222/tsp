package sa;

import greedy.NearestNeighbor;
import util.*;

public class SASearchWithThreeOpt extends TSP {

    private final double T0;
    private double T;
    private final int numOfIteration;
    private Timer timer;

    public SASearchWithThreeOpt(double T0, int numOfIteration) {
        if (T0 <= 0) {
            System.out.println("======INITIAL TEMPERATURE SHOULD BE BIGGER THAN 0======");
            System.exit(1);
        }

        this.T0 = T0;
        this.T = T0;
        this.numOfIteration = numOfIteration;
        this.timer = new Timer();
    }

    @Override
    public Path calculatePath(int startPoint) {
        timer.tic();
        NearestNeighbor nearestNeighbor = new NearestNeighbor();
        Path path = nearestNeighbor.calculatePath(startPoint);
        return calculatePath(path);
    }

    @Override
    public Path calculatePath(Path path) {
        /* initialize */
        int iter = 0, iterOfT = 0;
        Path minPath, trialPath, RandPath, currentPath, bestPath, savePath;
        int [] randNums;

        /* start */
        minPath = path.deepCopy();
        trialPath = path.deepCopy();
        RandPath = path.deepCopy();
        bestPath = path.deepCopy();
        savePath = path.deepCopy();

        while(!timer.isOver(Timer.FIRST_DEMO_LIMIT_SEC)) {

            //최저에서 아무거나 바꿔본다 <min에서 rand 생성!>
            RandPath = minPath.deepCopy();
            for(int i = 0 ; i < 3; i++) {
                randNums = Pick.randIdx(3);
                RandPath.threeOptSwap(randNums[0], randNums[1], randNums[2]);
            }

            //바꾼 값을 열심히 최적화 해본다 <trial로 rand를 열심히 최적화!>
            for (int i = 0; i < numOfIteration; i++) {
                trialPath = RandPath.deepCopy();
                randNums = Pick.randCities(3);
                trialPath.threeOptSwap(randNums[0], randNums[1], randNums[2]);

                if (RandPath.totalCost > trialPath.totalCost) RandPath = trialPath.deepCopy();
                if (savePath.totalCost > trialPath.totalCost) savePath = trialPath.deepCopy();
                iter++;

                if (timer.tick()) {
                    System.out.printf("iter(%6.2fM), timeDelta(%4.2f), cost(%5.2f) T(%3.2f)\n",
                            iter / 1000000.0, timer.toc(), RandPath.totalCost, T);
                }
                if (timer.isOver(Timer.FIRST_DEMO_LIMIT_SEC)) break;
            }

            //어느정도 최적화 (local optima에 접근했다고 가정한다) 가 되었다고 치고 비교하자
            //그럼 global 최적해와 rand를 비교한다
            if (minPath.totalCost > RandPath.totalCost) {
                minPath = RandPath.deepCopy(); // 더 좋으면 가야지 (다음출발값)
            }
            else {
                //근데 안좋으면?
                double criteria = Math.pow(Math.E, (minPath.totalCost - RandPath.totalCost)/T);
                if (Math.random() < criteria) {
                    minPath = RandPath.deepCopy(); // 나쁜값이라도 받아들여볼까
                    System.out.println("accpet");
                }
                else {
                    minPath = bestPath.deepCopy();
                    System.out.println("denied");
                }
            }

            int temp = (int) Math.round(timer.toc()*2);
            T = Cooling.linearAdditiveCooling(1, T0, 60, temp);
        }

        return savePath;
    }
}