package util;

import javafx.util.Pair;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class CostMemo {
    private String info;
    public ArrayList<Pair<Double,Double> > costs;
    public CostMemo(String st) {
        info = st;
        costs = new ArrayList<>();
    }
    public void memo (double time, double cost) { costs.add(new Pair<>(time, cost)); }
    public void save () {
        save("costs.txt");
    }
    public void save(String fileName) {
        try {
            FileWriter fw = new FileWriter(fileName, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.print("time");
            for (Pair<Double, Double> data : costs) {
                out.print(",");
                out.printf("%.3f", data.getKey());
            }
            out.print("\n");
            out.print(info);
            for (Pair<Double, Double> data : costs) {
                out.print(",");
                out.printf("%.3f", data.getValue());
            }
            out.print("\n");
            out.close();
            bw.close();
            fw.close();
        } catch (IOException e) {
            System.exit(1);
        }
    }
}
