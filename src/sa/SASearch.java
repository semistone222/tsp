package sa;

import greedy.NearestNeighbor;
import util.*;

public class SASearch extends TSP {

    private final double T0;
    private double T;
    private final int numOfIteration;
    private Timer timer;

    public SASearch(double T0, int numOfIteration) {
        if (T0 <= 0) {
            System.out.println("======INITIAL TEMPERATURE SHOULD BE BIGGER THAN 0======");
            System.exit(1);
        }

        this.T0 = T0;
        this.T = T0;
        this.numOfIteration = numOfIteration;
        this.timer = new Timer(Timer.FIRST_DEMO_LIMIT_SEC);
    }

    @Override
    public Path calculatePath(int startPoint) {
        this.timer.start(System.currentTimeMillis());
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

        while(!timer.isTimeGone()) {

            //�������� �ƹ��ų� �ٲ㺻�� <min���� rand ����!>
            RandPath = minPath.deepCopy();
            for(int i = 0 ; i < 3; i++) {
                randNums = Pick.randNums(3, numOfCities);
                RandPath.threeOptSwap(randNums[0], randNums[1], randNums[2]);
            }

            //�ٲ� ���� ������ ����ȭ �غ��� <trial�� rand�� ������ ����ȭ!>
            for (int i = 0; i < numOfIteration; i++) {
                trialPath = RandPath.deepCopy();
                randNums = Pick.randNums(3, numOfCities);
                trialPath.threeOptSwap(randNums[0], randNums[1], randNums[2]);

                if (RandPath.totalCost > trialPath.totalCost) RandPath = trialPath.deepCopy();
                if (savePath.totalCost > trialPath.totalCost) savePath = trialPath.deepCopy();
                iter++;

                if (timer.tick()) {
                    System.out.printf("iter(%6.2fM), timeDelta(%4.2f), cost(%5.2f) T(%3.2f)\n",
                            iter / 1000000.0, timer.toc(), RandPath.totalCost, T);
                }
                if (timer.isTimeGone()) break;
            }

            //������� ����ȭ (local optima�� �����ߴٰ� �����Ѵ�) �� �Ǿ��ٰ� ġ�� ������
            //�׷� global �����ؿ� rand�� ���Ѵ�
            if (minPath.totalCost > RandPath.totalCost) {
                minPath = RandPath.deepCopy(); // �� ������ ������ (������߰�)
            }
            else {
                //�ٵ� ��������?
                double criteria = Math.pow(Math.E, (minPath.totalCost - RandPath.totalCost)/T);
                if (Math.random() < criteria) {
                    minPath = RandPath.deepCopy(); // ���۰��̶� �޾Ƶ鿩����
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
