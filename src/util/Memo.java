package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Memo {
    private String info;
    private ArrayList<Integer> memo;
    public Memo(String st){ memo = new ArrayList<>(); info = st; }
    public void doMemo (int val) { memo.add(val); }
    private void _saveMemoWithName (String filename) {
        try {
            FileWriter fw = new FileWriter(filename, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.print(info);
            for (int i = 0; i < memo.size(); i++) {
                out.print(",");
                out.print(memo.get(i));
            }
            out.print("\n");
            out.close();
            bw.close();
            fw.close();
        } catch (IOException e) {
            System.exit(1);
        }
    }
    public void saveMemo () {
        _saveMemoWithName("costs.txt");
    }
    public void savePath (Path ipath) {
        memo.clear();
        for (int i = 0; i < ipath.order.length; i++) memo.add(ipath.order[i]);
        _saveMemoWithName("path.txt");
    }
}
