package util;

public class Edge implements Comparable<Edge> {
    public int start;
    public int end;
    public double distance;

    public Edge(int start, int end, double distance) {
        this.start = start;
        this.end = end;
        this.distance = distance;
    }

    @Override
    public int compareTo(Edge o) {
        if (this.distance < o.distance) {
            return 1;
        } else if (this.distance > o.distance) {
            return -1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        Edge edge = (Edge) obj;
        return (this.start == edge.start) && (this.end == edge.end) && (this.distance == edge.distance);
    }
}
