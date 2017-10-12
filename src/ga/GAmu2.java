package ga;

import greedy.NearestNeighbor;
import sa.TabuSearch;
import util.*;

public class GAmu2 extends GATSP {
    @Override
    public Path calculatePath() {
        timer.tic();
        Memo groupMemo[][] = new Memo[4][5];
        Memo timeCheker = new Memo("time");
        String groupName = "ABCD";
        double limit = 300.0;

        /* stage1
         * seed start path with nearest neighbor*/
        Path population[][] = new Path[4][5];
        NearestNeighbor seeder1 = new NearestNeighbor();
        Path seed = seeder1.calculatePath(1);
        for (int groupNum = 0; groupNum < 4; groupNum++) {
            for (int mem = 0; mem < 5; mem++) {
                population[groupNum][mem] = seed.deepCopy();
                groupMemo[groupNum][mem] = new Memo("group" + groupName.charAt(groupNum) + "_" + mem);
                groupMemo[groupNum][mem].doMemo((int)Math.round(population[groupNum][mem].totalCost));
            }
        }
        Path ret = seed.deepCopy();
        System.out.printf("New World Begins. %.2f\n", timer.toc());

        while (true) {
            System.out.print("opt : " + (generation) + ", timer : ");
            System.out.printf("%.2f\n", timer.toc());
            /* stage 2
             * optimizer */
            for (int groupNum = 0; groupNum < 4; groupNum++) {
                for (int mem = 0; mem < 5; mem++) {
                    TabuSearch optimer = new TabuSearch(16, 0.05);
                    population[groupNum][mem] = optimer.calculatePath(population[groupNum][mem], 0.5);
                    if (ret.totalCost > population[groupNum][mem].totalCost) ret = population[groupNum][mem].deepCopy();
                    groupMemo[groupNum][mem].doMemo((int)Math.round(population[groupNum][mem].totalCost));
                }
            }
            timeCheker.doMemo((int)timer.toc());

            System.out.print("eilte : " + (generation) + ", timer : ");
            System.out.printf("%.2f\n", timer.toc());
            /* stage 3
             * elite optimizer */
            int eliteIdx[][] = new int[4][2];
            for (int groupNum = 0; groupNum < 4; groupNum++) {
                double eliteMin = 999999.0;
                eliteIdx[groupNum][0] = 0;
                for (int mem = 0; mem < 5; mem++) {
                    if (eliteMin > population[groupNum][mem].totalCost) {
                        eliteMin = population[groupNum][mem].totalCost;
                        eliteIdx[groupNum][1] = eliteIdx[groupNum][0];
                        eliteIdx[groupNum][0] = mem;
                    }
                }
                int temp = eliteIdx[groupNum][0];
                TabuSearch optimer = new TabuSearch(16, 0.05);
                population[groupNum][temp] = optimer.calculatePath(population[groupNum][temp], 0.5);

                temp = eliteIdx[groupNum][1];
                optimer = new TabuSearch(16, 0.05);
                population[groupNum][temp] = optimer.calculatePath(population[groupNum][temp], 0.5);

                for (int mem = 0; mem < 5; mem++) {
                    groupMemo[groupNum][mem].doMemo((int)Math.round(population[groupNum][mem].totalCost));
                    if (ret.totalCost > population[groupNum][mem].totalCost) ret = population[groupNum][mem].deepCopy();
                }
            }
            timeCheker.doMemo((int)timer.toc());

            if (timer.toc() > limit - 33.0) break;
            System.out.print("generation : " + (++generation) + ", timer : ");
            System.out.printf("%.2f\n", timer.toc());
            System.out.printf("A : %.2f\n", population[0][eliteIdx[0][0]].totalCost);
            System.out.printf("B : %.2f\n", population[1][eliteIdx[1][0]].totalCost);
            System.out.printf("C : %.2f\n", population[2][eliteIdx[2][0]].totalCost);
            System.out.printf("D : %.2f\n", population[3][eliteIdx[3][0]].totalCost);

            System.out.print("crossover : " + (generation) + ", timer : ");
            System.out.printf("%.2f\n", timer.toc());
            /* stage 4
             * crossover */
            for (int groupNum = 0; groupNum < 3; groupNum++) {
                Path parent[] = new Path[2];
                parent[0] = population[groupNum][eliteIdx[groupNum][0]].deepCopy();
                parent[1] = population[groupNum][eliteIdx[groupNum][1]].deepCopy();

                Path[] child = crossover.crossover(parent[0], parent[1]);
                population[groupNum][0] = child[0];
                population[groupNum][1] = child[1];

                child = crossover.crossover(parent[0], parent[1]);
                population[groupNum][2] = child[0].deepCopy();
                mutation.mutate(child[0]);
                mutation.mutate(child[1]);

                population[groupNum][3] = child[0];
                population[groupNum][4] = child[1];

                population[groupNum][eliteIdx[groupNum][0]] = parent[0];
            }

            Path parent[] = new Path[4];
            parent[0] = population[0][eliteIdx[0][0]].deepCopy();
            parent[1] = population[1][eliteIdx[1][0]].deepCopy();
            parent[2] = population[2][eliteIdx[2][0]].deepCopy();
            parent[3] = population[3][eliteIdx[3][0]].deepCopy();

            Path child[];
            child = crossover.crossover(parent[0], parent[1]);
            population[3][0] = child[0];
            child = crossover.crossover(parent[1], parent[2]);
            population[3][1] = child[0];
            child = crossover.crossover(parent[2], parent[3]);
            population[3][2] = child[0];
            child = crossover.crossover(parent[0], parent[3]);
            population[3][3] = child[0];
            child = crossover.crossover(parent[1], parent[3]);
            population[3][4] = child[0];

            for (int groupNum = 0; groupNum < 4; groupNum++) {
                for (int mem = 0; mem < 5; mem++) {
                    groupMemo[groupNum][mem].doMemo((int) Math.round(population[groupNum][mem].totalCost));
                    if (ret.totalCost > population[groupNum][mem].totalCost) ret = population[groupNum][mem].deepCopy();
                }
            }
            timeCheker.doMemo((int)timer.toc());
        }
        System.out.printf("Last Hero borned : %.2f\n", timer.toc());
        /* stage 5
         * last optimization */
        TabuSearch lastOptimizer = new TabuSearch(16, 0.05);
        ret = lastOptimizer.calculatePath(ret, limit - timer.toc() - 1.0);
        groupMemo[0][0].doMemo((int) Math.round(ret.totalCost));
        timeCheker.doMemo((int)timer.toc());

        timeCheker.saveMemo();
        for (int groupNum = 0; groupNum < 4; groupNum++) {
            for (int mem = 0; mem < 5; mem++) {
                groupMemo[groupNum][mem].saveMemo();
            }
        }

        System.out.printf("Last Hero left after : %.2f\n", timer.toc());
        ret.printState();
        ret.printTotalCost();

        return ret;
    }
}
