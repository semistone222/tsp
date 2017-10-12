package ga;

import ga.crossover.Crossover;
import ga.initialize.Initializer;
import ga.mutate.Mutation;
import ga.select.Selection;
import greedy.NearestNeighbor;
import sa.TabuSearch;
import util.*;

import java.util.Arrays;

public class GAmu extends TSP{
    /* super */
    /* protected int numofCities;
     * protected double distanceMap[][]; */
    private int populationSize;
    private int generationSize;
    private int generation;
    private Path[] population;

    private Initializer initializer;
    private Selection selection;
    private Crossover crossover;
    private Mutation mutation;

    private Timer timer;

    public GAmu(int populationSize, int generationSize){
        this.populationSize = populationSize;
        this.generationSize = generationSize;
        this.generation = 0;
        this.population = null;

        this.initializer = null;
        this.selection = null;
        this.crossover = null;
        this.mutation = null;

        this.timer = new Timer();
    }

    public void setProcess(Initializer initializer, Selection selection, Crossover crossover, Mutation mutation) {
        this.initializer = initializer;
        this.selection = selection;
        this.crossover = crossover;
        this.mutation = mutation;
    }

    @Override
    public Path calculatePath(int notUsed) {
        /* stage 1
         * Nearest 5 Path */
        // TODO if start point is different, crossover occurs error
        int randNum[] = Pick.randNums(5, this.numOfCities);
        Path start5path[] = new Path[5];
        NearestNeighbor nearestNeighbor = new NearestNeighbor();
        for (int i = 0; i < 5; i++) {
            start5path[i] = nearestNeighbor.calculatePath(1);
        }

        /* stage 2
         * Crossover to make 4 Group (each have 5 populations) */
        PathComparatorAscCost asc = new PathComparatorAscCost();
        Arrays.sort(start5path, asc);
        Path group[][] = new Path[4][5];
        for (int groupNum = 0; groupNum < 4; groupNum++) {
            for (int seeds = 0; seeds < 5; seeds++) {
                group[groupNum][seeds] = crossover.crossover(start5path[0], start5path[groupNum+1])[0];
            }
        }

        System.out.printf("New World Begins. %.2f\n", timer.toc());
        TabuSearch tempOpt = new TabuSearch(16, 0.05);
        while(timer.toc() < 90.0) {
            /* stage 3
             * opt 10000 */
            for (int groupNum = 0; groupNum < 4; groupNum++) {
                for (int member = 0; member < 5; member++) {
                    group[groupNum][member] = tempOpt.calculatePath(group[groupNum][member], 0.5);
                }
            }

            /* stage 4 = after 10 sec
             * Select Elite and Develope */
            Path elite[][] = new Path[5][2];
            for (int groupNum = 0; groupNum < 4; groupNum++) {
                Arrays.sort(group[groupNum], asc);
                elite[groupNum][0] = tempOpt.calculatePath(group[groupNum][0], 1.0);
                elite[groupNum][1] = tempOpt.calculatePath(group[groupNum][1], 1.0);
            }

            System.out.print("generation : " + (++generation) + ", timer : ");
            System.out.printf("%.2f\n", timer.toc());
            System.out.printf("A : %.2f\n", elite[0][0].totalCost);
            System.out.printf("B : %.2f\n", elite[1][0].totalCost);
            System.out.printf("C : %.2f\n", elite[2][0].totalCost);
            System.out.printf("D : %.2f\n", elite[3][0].totalCost);

            /* stage 5 = after 18 sec
             * Crossover with elite */
            for (int groupNum = 0; groupNum < 3; groupNum++) {
                for (int child = 1; child < 5; child++) {
                    group[groupNum][child] = crossover.crossover(elite[groupNum][0], elite[groupNum][1])[0];
                    //mutation.mutate(group[groupNum][child]);
                }
            }
            group[3][0] = crossover.crossover(elite[3][0], elite[0][0])[0];
            group[3][1] = crossover.crossover(elite[3][0], elite[1][0])[0];
            group[3][2] = crossover.crossover(elite[3][0], elite[2][0])[0];
            group[3][3] = crossover.crossover(elite[3][0], elite[3][1])[0];
            group[3][4] = crossover.crossover(elite[3][0], elite[3][1])[0];
        }

        /* stage 6
         * Last OPT */
        Path lastHero[] = new Path[5];
        for (int i = 0; i < 5; i++) {
            lastHero[i] = group[3][i];
        }
        for (int i = 0; i < 5; i++) {
            lastHero[i] = tempOpt.calculatePath(lastHero[i], 1.0);
        }
        Arrays.sort(lastHero, asc);
        /* stage 7
         * last hero */
        lastHero[0] = tempOpt.calculatePath(lastHero[0], 120.0 - timer.toc() - 1.0);

        System.out.printf("Last Hero left after : %.2f\n", timer.toc());
        Path ret = lastHero[0].deepCopy();
        ret.printState();
        return ret;
    }

    @Override // 사용하지 않음
    public Path calculatePath(Path path) {
        return path;
    }
}