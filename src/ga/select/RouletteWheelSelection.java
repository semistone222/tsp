package ga.select;

import javafx.util.Pair;
import util.Path;

import java.util.ArrayList;
import java.util.Random;

public class RouletteWheelSelection implements Selection {
    // 변수들
    private double costMax, costMin, costDiff;
    private final double k;
    private double spinTablePDF[], sumOfFitness;
    private ArrayList<Pair<Integer, Double>> uniqueTable; // 중복을 배제한 부모 선택을 위함

    // 생성자
    public RouletteWheelSelection(double k) { this.k = k; }

    // 상위 함수
    public int[] select(Path[] poulation) {
        return _select(poulation, 2);
    }

    /** fi
     * cost의 좋은 정도가 fi로 리턴됨. k값에 비례하여 보정
     * get fitness of solution i
     * @param Ci cost of solution i
     * @return fitness of solution i
     */
    private double fi(double Ci) {
        return (costMax - Ci) + costDiff/(k - 1.0d);
    }

    /** spin
     * random 함수를 돌려서 probTable에서 하나 뽑는 함수
     */
    private int spin() {
        int ret = -1;
        Random random = new Random();
        double point = sumOfFitness * random.nextDouble();

        double wheel = 0.0d;
        for (int i = 0; i < spinTablePDF.length; i++) {
            wheel += spinTablePDF[i];
            if (point <= wheel) {
                ret = i;
                break;
            }
        }

        if(ret == -1) {
            System.err.println("======ROULETTE ERR======");
            System.exit(1);
        }

        return ret;
    }

    /** _select
     * probTable을 초기화하고 fi들을 모두 저장함
     * num 만큼 뺑뺑이를 돌려서 그 배열을 리턴한다
     */
    private int[] _select(Path[] population, int num) {
        // important! assume that population is sorted by cost asc
        costMax = population[population.length - 1].totalCost + 1;
        costMin = population[0].totalCost;
        costDiff = costMax - costMin;
        if (costDiff < 0) {
            System.err.println("=====RouteWheel : setSpinTable : not asc sorted=====");
            System.exit(0);
        }

        spinTablePDF = new double[population.length];
        sumOfFitness = 0.0d;
        for (int i = 0; i < population.length; i++) {
            spinTablePDF[i] = fi(population[i].totalCost);
            sumOfFitness += spinTablePDF[i];
        }

        // for debug, delete this
        if(Math.abs(sumOfFitness - 0.0d) < 0.001d) {
            System.out.println("=====ALL POPULATION IS SAME=====");
        }

        // spin
        int[] parentsIdx = new int[num];
        for (int i = 0; i < num; i++) parentsIdx[i] = spin();
        return parentsIdx;
    }

    //===== 여기서 부터 추가된 함수 입니다

    /** selectIdx
     * 딱 한개의 idx를 cost 뺑뺑이로 리턴함
     */
    public int selectIdx() {
        if (spinTablePDF.length != 0) {
            System.err.println("=====RouletteWheel : not set probTable=====");
            System.exit(0);
        }
        return spin();
    }

    /** uniqueSelect()
     * 딱 한개의 idx를 새로 초기화 전까지 모든 중복을 고려하여 리턴함
     */
    public int uniqueSelect() {
        return uniqueSpin();
    }

    /** uniqueSelect(int n)
     * n개의 idx를 중복 없이 뽑아서 cost 뺑뺑이로 리턴함
     */
    public int[] uniqueSelect(int n) {
        int ret[] = new int[n];
        for (int i = 0; i < n; i++) {
            if (uniqueTable.isEmpty()) {
                System.err.println("=====RouletteWheel : not set uniqueTable=====");
                System.exit(0);
            }
            ret[i] = uniqueSpin();
        }
        return ret;
    }

    /** uniqueSpin()
     *  한개 뽑고 리스트에서 삭제
     */
    private int uniqueSpin() {
        if (uniqueTable.isEmpty()) {
            System.err.println("=====RouletteWheel : unique : empty queue=====");
            System.exit(0);
        }

        int ret = -1;
        Random random = new Random();
        double point = sumOfFitness * random.nextDouble();

        double wheel = 0.0d;
        for (Pair<Integer, Double> tempFi : uniqueTable) {
            wheel += tempFi.getValue();
            if (point <= wheel) {
                ret = tempFi.getKey(); // idx를 얻어냄
                sumOfFitness -= tempFi.getValue(); // 전체 spin fi의 총합이 감소
                uniqueTable.remove(tempFi); // 돌림판 일부 제거
                break;
            }
        }

        if(ret == -1) {
            System.err.println("======ROULETTE ERR======");
            System.exit(1);
        }

        return ret;
    }

    /** setSpinTable(Path[] population)
     *  리스트 생성해서 초기화 하는 함수!
     *  unique 선택전 호출 필요
     */
    public void setSpinTable(Path[] population) {
        // 반드시 오름차순으로 정렬 되어있을것
        _select(population, 0);
        uniqueTable = new ArrayList<>();
        for (int i = 0; i < population.length; i++)
            uniqueTable.add(new Pair<>(i, spinTablePDF[i]));
    }











}
