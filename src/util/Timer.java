package util;

public class Timer {

    public static final long FIRST_DEMO_LIMIT_SEC = 30 * 1000;
    public static final long SECOND_DEMO_LIMIT_SEC = 120 * 1000;

    private final long LIMIT;
    private long startTime;

    private double sec = 0.0;
    private double delta = 0.5;

    public Timer(long limit) {
        this.LIMIT = limit;
    }

    public void start(long startTime) {
        this.startTime = startTime;
    }

    public boolean isTimeGone() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - startTime) > LIMIT;
    }

    public long getExecutionSeconds() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - startTime) / 1000;
    }

    public void tic() { startTime = System.currentTimeMillis(); }
    public double toc() { return (System.currentTimeMillis() - startTime)/1000.0; }
    public boolean tick() {
        if (toc() > sec) {
            sec += delta;
            return true;
        }
        return false;
    }
}
