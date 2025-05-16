package cpt204_cw3.taskAB;

import java.util.*;

public abstract class UnWeightedGraph<V> implements Graph<V> {
    protected List<V> vertices = new ArrayList<>(); // Store vertices
    protected List<List<Edge>> neighbors = new ArrayList<>(); // Adjacency lists

    /**
     * Construct an empty graph
     */
    public UnWeightedGraph() {
    }

    /**
     * Construct a graph from vertices and edges stored in List
     */
    public UnWeightedGraph(List<V> vertices, List<Edge> edges) {
        for (V vertex : vertices) addVertex(vertex);

        createAdjacencyLists(edges);
    }

    /**
     * Create adjacency lists for each vertex
     */
    private void createAdjacencyLists(List<Edge> edges) {
        for (Edge edge : edges) addEdge(edge.getU(), edge.getV());
    }

    @Override
    public int getSize() {
        return vertices.size();
    }

    @Override
    public List<V> getVertices() {
        return vertices;
    }

    @Override
    public V getVertex(int index) {
        return vertices.get(index);
    }

    @Override
    public int getIndex(V v) {
        return vertices.indexOf(v);
    }

    @Override
    public List<Integer> getNeighbors(int index) {
        List<Integer> result = new ArrayList<>();
        for (Edge e : neighbors.get(index))
            result.add(e.getV());

        return result;
    }

    @Override
    public int getDegree(int v) {
        return neighbors.get(v).size();
    }

    @Override
    public void printEdges() {
        for (int u = 0; u < neighbors.size(); u++) {
            System.out.print(getVertex(u) + " (" + u + "): ");
            for (Edge e : neighbors.get(u)) {
                System.out.print("(" + getVertex(e.getU()) + ", " +
                        getVertex(e.getV()) + ") ");
            }
            System.out.println();
        }
    }

    @Override
    public void clear() {
        vertices.clear();
        neighbors.clear();
    }

    @Override
    public boolean addVertex(V vertex) {
        if (!vertices.contains(vertex)) {
            vertices.add(vertex);
            neighbors.add(new ArrayList<Edge>());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addVertexes(ArrayList<V> vertexes) {
        boolean result = true;
        for (V vertex : vertexes) {
            if (!addVertex(vertex)) {
                result = false;
            }
        }
        return result;
    }

    @Override
    public boolean addEdge(Edge e) {
        if (e.getU() < 0 || e.getU() > getSize() - 1)
            throw new IllegalArgumentException("No such index: " + e.getU());

        if (e.getV() < 0 || e.getV() > getSize() - 1)
            throw new IllegalArgumentException("No such index: " + e.getV());

        if (e.getU() == e.getV()) {
            return false; // No self-loop
        }

        boolean added = false;

        // Add edge
        if (!neighbors.get(e.getU()).contains(e)) {
            neighbors.get(e.getU()).add(e);
            added = true;
        }

        // Add reversed edge
        Edge reverseEdge = createReverseEdge(e);
        if (!neighbors.get(reverseEdge.getU()).contains(reverseEdge)) {
            neighbors.get(reverseEdge.getU()).add(reverseEdge);
            added = true;
        }

        return added;
    }

    protected abstract Edge createReverseEdge(Edge originalEdge);

    @Override
    public boolean addEdge(int u, int v) {
        return addEdge(new Edge(u, v));
    }

    /**
     * Add edges to the graph, return true if successful
     */
    @Override
    public boolean addEdges(List<Edge> edges) {
        boolean result = true;
        for (Edge edge : edges) {
            if (!addEdge(edge)) {
                result = false;
            }
        }
        return result;
    }

    @Override
    public boolean remove(V v) {
        int index = vertices.indexOf(v);
        if (index == -1) return false; // Vertex not found

        vertices.remove(index); // Remove vertex
        neighbors.remove(index); // Remove adjacency list

        // Remove edges from other adjacency lists
        for (List<Edge> edges : neighbors) {
            edges.removeIf(e -> e.getU() == index || e.getV() == index);
        }
        return true;
    }

    @Override
    public boolean remove(int u, int v) {
        if (u < 0 || u > getSize() - 1)
            throw new IllegalArgumentException("No such index: " + u);

        if (v < 0 || v > getSize() - 1)
            throw new IllegalArgumentException("No such index: " + v);

        List<Edge> edges = neighbors.get(u);
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).getV() == v) {
                edges.remove(i);
                return true;
            }
        }
        return false;
    }
}

