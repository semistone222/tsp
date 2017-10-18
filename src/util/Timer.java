package util;

public class Timer {

    public static final int FIRST_DEMO_LIMIT_SEC = 30;
    public static final int SECOND_DEMO_LIMIT_SEC = 120;

    private long startTime = System.currentTimeMillis();
    private double sec = 0.0;
    private double delta = 0.5;

    public boolean isOver(int limitSec) {
        return toc() > limitSec;
    }

    public void tic() { startTime = System.currentTimeMillis(); }

    public double toc() {
        return (System.currentTimeMillis() - startTime) / 1000.0;
    }

    public boolean tick() {
        if (toc() > sec) {
            sec += delta;
            return true;
        }
        return false;
    }

    private static long worldBegin;
    public static void begin() { worldBegin = System.currentTimeMillis(); }
    public void synchronize() { startTime = worldBegin; }
}
