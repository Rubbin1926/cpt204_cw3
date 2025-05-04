package cpt204_cw3.taskAB;

public class Edge {
    private int u, v;

    public Edge(int u, int v) {
        this.u = u;
        this.v = v;
    }

    public void setU(int u) {
        this.u = u;
    }

    public void setV(int v) {
        this.v = v;
    }

    public int getU() {
        return u;
    }

    public int getV() {
        return v;
    }

    public boolean equals(Object o) {
        if (o instanceof Edge e) {
            return u == e.u && v == e.v;
        }
        return false;
    }
}
