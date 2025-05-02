package cpt204_cw3.java;

import java.util.Objects;

public class WeightedEdge extends Edge implements Comparable<WeightedEdge> {
    private double weight;

    public WeightedEdge(int u, int v, double weight) {
        super(u, v);
        this.weight = weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public int compareTo(WeightedEdge o) {
        // Return 1 if this edge is greater than the other edge
        // Return 0 if this edge is equal to the other edge
        // Return -1 if this edge is less than the other edge
        return Double.compare(this.weight, o.weight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; // check father class
        WeightedEdge that = (WeightedEdge) o;
        return Double.compare(that.weight, weight) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), weight);
    }
}
