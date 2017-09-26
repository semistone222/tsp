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
    public void saveMemo () {
        try {
            FileWriter fw = new FileWriter("results.txt", true);
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
}
