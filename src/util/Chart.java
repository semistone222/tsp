package util;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Chart extends JFrame {

    private int sizeOfNode = 10;
    private int screenSizeX;
    private int screenSizeY = 800;
    private int padX = 10;
    private int padY = 10;

    public Chart(Path path) {
        setTitle("TSP path");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        int maxValueX = Integer.MIN_VALUE;
        int maxValueY = Integer.MIN_VALUE;
        HashMap<Integer, City> cityHashMap = Map.getInstance().getCityHashMap();

        for(Integer i : cityHashMap.keySet()) {
            City city = cityHashMap.get(i);
            if(city.x > maxValueX) {
                maxValueX = city.x;
            }

            if(city.y > maxValueY) {
                maxValueY = city.y;
            }
        }

        float ratio = (float) maxValueX / (float) maxValueY;
        screenSizeX = (int) ((float) screenSizeY * ratio);
        float scaleXFactor = (float) screenSizeX / (float) maxValueX;
        float scaleYFactor = (float) screenSizeY / (float) maxValueY;

        // TODO : Add Scrollbar
        TSPPanel tspPanel = new TSPPanel(path.order, cityHashMap, scaleXFactor, scaleYFactor);
        setContentPane(tspPanel);
        setSize(screenSizeX + padX*2, screenSizeY + padY*2 + 40);
        setVisible(true);
    }

    private class TSPPanel extends JPanel {

        private int[] order;
        private HashMap<Integer, City> cityHashMap;
        private float scaleXFactor;
        private float scaleYFactor;

        private TSPPanel(int[] order, HashMap<Integer, City> cityHashMap, float scaleXFactor, float scaleYFactor) {
            this.order = order;
            this.cityHashMap = cityHashMap;
            this.scaleXFactor = scaleXFactor;
            this.scaleYFactor = scaleYFactor;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(Color.BLACK);
            for(int i = 0; i < order.length - 1; i++) {
                City start = cityHashMap.get(order[i]);
                City end = cityHashMap.get(order[i + 1]);
                g.drawLine(padX + (int) scaleXFactor * start.x,
                        screenSizeY - padY - (int) scaleYFactor * start.y,
                        padX + (int) scaleXFactor * end.x,
                        screenSizeY - padY - (int) scaleYFactor * end.y);
            }

            g.setColor(Color.GRAY);
            for(Integer i : cityHashMap.keySet()) {
                City city = cityHashMap.get(i);
                g.fillOval(padX +(int) scaleXFactor * city.x - sizeOfNode / 2,
                        screenSizeY - padY - (int) scaleYFactor * city.y - sizeOfNode / 2,
                        sizeOfNode,
                        sizeOfNode);
            }

            City first = cityHashMap.get(order[0]);
            g.setColor(Color.RED);
            g.fillOval(padX + (int) scaleXFactor * first.x - sizeOfNode / 2,
                    screenSizeY - padY - (int) scaleYFactor * first.y - sizeOfNode / 2,
                    sizeOfNode,
                    sizeOfNode);
        }
    }
}