package util;

import com.sun.org.apache.regexp.internal.RE;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DataChart extends JFrame{

    private ArrayList<Refer> testData;
    private class Refer {
        public Refer(){
            column = new ArrayList<>();
            maxCost = 0.0d;
            minCost = 50000.0d;
        }
        public ArrayList<Pair<Double, Double>> column;
        public double maxCost;
        public double minCost;
        public void dataParse(String timeValues[], String costValues[]) {
            for (int i = 1; i < timeValues.length; i++) {
                double t = Double.parseDouble(timeValues[i]);
                double c = Double.parseDouble(costValues[i]);
                column.add(new Pair<>(t,c));
                minCost = Math.min(c, minCost);
                maxCost = Math.max(c, maxCost);
            }
        }
    }
    private void txtInit() {
        try {
            BufferedReader in = new BufferedReader(new FileReader("costs.txt"));
            String s;
            String timeValues[];
            String costValues[];

            while ((s = in.readLine()) != null) {
                timeValues = s.split(",");
                s = in.readLine();
                if (s == null || s.length() < 2) break;
                costValues = s.split(",");

                Refer temp = new Refer();
                temp.dataParse(timeValues, costValues);
                testData.add(temp);
            }

            in.close();
        }  catch (IOException e) {
            System.err.println(e); // 에러가 있다면 메시지 출력
            System.exit(0);
        }
    }

    private double rangeMap(double tarMin, double tarMax, double srcMin, double srcMax, double val) {
        double ret = 0.0;
        double minDiff = (val - srcMin) / (srcMax - srcMin);
        ret = minDiff * (tarMax - tarMin) + tarMin;
        return ret;
    }

    private int windowWidth;
    private int windowHeight;

    private void ready() {
        setTitle("Cost Compare Tool");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Panel panel = new Panel();
        setContentPane(panel);
        windowWidth = 1200; windowHeight = 900;
        setSize(windowWidth + 20, windowHeight + 50); // x + 20, y + 50
        setVisible(true);
    }

    public DataChart() {
        testData = new ArrayList<>();
        txtInit();
        ready();
    }

    public DataChart(CostMemo obj) {
        testData = new ArrayList<>();
        Refer temp = new Refer();
        temp.column = obj.costs;
        for (Pair<Double,Double> i : obj.costs) {
            temp.column.add(new Pair<>(i.getKey(), i.getValue()));
            temp.maxCost = Math.max(i.getValue(), temp.maxCost);
            temp.maxCost = Math.min(i.getKey(), temp.minCost);
        }
        testData.add(temp);

        ready();
    }

    private int whiteBoxWidth, whiteBoxHeight;
    private double plotCostMin, plotCostMax;
    private class Panel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // 방향 밑 끝의 꼭지점 확인을 위한 작업입니다
            g.setColor(Color.BLACK);
            g.fillOval(0-5, 0-5, 10, 10);
            g.setColor(Color.RED);
            g.fillOval(windowWidth - 5, 0-5, 10, 10);
            g.setColor(Color.GREEN);
            g.fillOval(0-5, windowHeight - 5, 10, 10);
            g.setColor(Color.BLUE);
            g.fillOval(windowWidth - 5, windowHeight - 5, 10, 10);

            // 가운데 하얀 사각형을 그립니다
            g.setColor(Color.white);
            whiteBoxWidth = 1000; whiteBoxHeight = 700;
            g.fillRect(100,100, whiteBoxWidth, whiteBoxHeight);

            // 범위 획득
            plotCostMin = 90000.0d;
            for (Refer r : testData) {
                plotCostMin = Math.min(plotCostMin, r.minCost);
                plotCostMax = Math.max(plotCostMax, r.maxCost);
            }
            plotCostMax = Math.min(plotCostMin + 2000, plotCostMax);

            // 범위 표시
            // y축 먼저
            g.setColor(Color.BLACK);
            for (int y = 0; y <= whiteBoxHeight; y+= 100) {
                g.drawLine(100, 100 + y, 100 + whiteBoxWidth, 100+ y);
                double real = rangeMap(plotCostMin, plotCostMax, whiteBoxHeight, 0, y);
                String temp = String.format("%.2f", real);
                g.drawString(temp, 100 - 50, 100 + y + 5);
            }
            // x축
            g.setColor(Color.GRAY);
            for (int x = 0; x <= whiteBoxWidth; x+= 100) {
                g.drawLine(100 + x, 100 + 0, 100 + x, 100+ whiteBoxHeight);
                double real = rangeMap(0, 120, 0, whiteBoxWidth, x);
                String temp = String.format("%.1f", real);
                g.drawString(temp, 100 + x - 10, 100 + whiteBoxHeight + 15);
            }

            // 실선을 그리되, 마지막은 초록색, 나머지는 검은색으로 그립니다
            int prevX = 0, prevY = 0;
            int w = 2, h = 2;
            for (Refer r : testData) {
                if (r == testData.get(testData.size() - 1)) g.setColor(Color.GREEN);
                else g.setColor(Color.BLACK);
                for (Pair<Double, Double> p : r.column) {
                    int x = (int)rangeMap(100, 100 + whiteBoxWidth, 0, 120, p.getKey());
                    int y = (int)rangeMap(100, 100 + whiteBoxHeight, plotCostMax, plotCostMin, p.getValue());
                    g.fillOval(x - w/2, y - h/2, w, h);
                    if (p != r.column.get(0)) g.drawLine(x, y, prevX, prevY);
                    prevX = x; prevY = y;
                }
            }

        }
    }

}
