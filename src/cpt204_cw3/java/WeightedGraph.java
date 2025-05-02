package cpt204_cw3.java;

import java.util.*;

public class WeightedGraph<V> extends UnWeightedGraph<V> {
    /**
     * Construct an empty
     */
    public WeightedGraph() {
    }

    /**
     * Construct a WeightedGraph for vertices 0, 1, 2 and edge list
     */
    public WeightedGraph(List<V> vertices, List<WeightedEdge> edges) {
        createWeightedGraph(vertices, edges);
    }

    /**
     * Create adjacency lists from edge lists
     */
    private void createWeightedGraph(List<V> vertices, List<WeightedEdge> edges) {
        this.vertices = vertices;

        // Create a list for vertices
        for (int i = 0; i < vertices.size(); i++) {
            neighbors.add(new ArrayList<>());
        }

        // add edges
        for (WeightedEdge edge : edges) {
            addWeightedEdge(edge); // Add edge and reversed edge into the list
        }
    }

    /**
     * Return the weight on the edge (u, v)
     */
    public double getWeight(int u, int v) throws Exception {
        for (Edge edge : neighbors.get(u)) {
            if (edge.getV() == v) {
                return ((WeightedEdge) edge).getWeight();
            }
        }

        throw new Exception("Edge does not exit");
    }

    /**
     * Return the weight on the edge
     */
    public double getWeight(WeightedEdge edge) throws Exception {
        for (Edge e : neighbors.get(edge.getU())) {
            if (e.getV() == edge.getV()) {
                return ((WeightedEdge) e).getWeight();
            }
        }

        throw new Exception("Edge does not exit");
    }

    /**
     * Display edges with weights
     */
    public void printWeightedEdges() {
        for (int i = 0; i < getSize(); i++) {
            System.out.print(getVertex(i) + " (" + i + "): ");
            for (Edge edge : neighbors.get(i)) {
                System.out.print("(" + edge.getU() +
                        ", " + edge.getV() + ", " + ((WeightedEdge) edge).getWeight() + ") ");
            }
            System.out.println();
        }
    }

    /**
     * Add an edge (u, v, weight) to the graph.
     */
    public boolean addWeightedEdge(int u, int v, double weight) {
        return addWeightedEdge(new WeightedEdge(u, v, weight));
    }

    /**
     * Add an edge e to the graph, return true if successful
     */
    public boolean addWeightedEdge(WeightedEdge e) {
        if (e.getU() < 0 || e.getU() > getSize() - 1)
            throw new IllegalArgumentException("No such index: " + e.getU());

        if (e.getV() < 0 || e.getV() > getSize() - 1)
            throw new IllegalArgumentException("No such index: " + e.getV());

        if (e.getU() == e.getV()) return false; // No self-loop

        boolean added = false;

        // Add edge
        if (!neighbors.get(e.getU()).contains(e)) {
            neighbors.get(e.getU()).add(e);
            added = true;
        }

        // Add reversed edge
        WeightedEdge reverseEdge = new WeightedEdge(e.getV(), e.getU(), e.getWeight());
        if (!neighbors.get(reverseEdge.getU()).contains(reverseEdge)) {
            neighbors.get(reverseEdge.getU()).add(reverseEdge);
            added = true;
        }

        return added;
    }

    public boolean addWeightedEdges(List<WeightedEdge> edges) {
        boolean result = true;
        for (WeightedEdge edge : edges) {
            if (!addWeightedEdge(edge.getU(), edge.getV(), edge.getWeight())) {
                result = false;
            }
        }
        return result;
    }
}

