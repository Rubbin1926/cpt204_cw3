package cpt204_cw3.taskAB;

import java.util.ArrayList;
import java.util.List;

public interface Graph<V> {
    /**
     * Return the number of vertices in the graph
     */
    public int getSize();

    /**
     * Return the vertices in the graph
     */
    public java.util.List<V> getVertices();

    /**
     * Return the object for the specified vertex index
     */
    public V getVertex(int index);

    /**
     * Return the index for the specified vertex object
     */
    public int getIndex(V v);

    /**
     * Return the neighbors of the specified vertex
     */
    public java.util.List<Integer> getNeighbors(int index);

    /**
     * Return the degree for a specified vertex
     */
    public int getDegree(int v);

    /**
     * Print the edges
     */
    public void printEdges();

    /**
     * Clear the graph
     */
    public void clear();

    /**
     * Add a vertex to the graph, return true if successful
     */
    public boolean addVertex(V vertex);

    /**
     * Add vertexes to the graph, return true if successful
     */
    public boolean addVertexes(ArrayList<V> vertexes);

    /** Add an edge to the graph */
    public boolean addEdge(Edge e);

    /**
     * Add an edge to the graph, return true if successful
     */
    public boolean addEdge(int u, int v);

    /**
     * Add edges to the graph, return true if successful
     */
    public boolean addEdges(List<Edge> edges);

    /**
     * Remove a vertex v from the graph, return true if successful
     */
    public boolean remove(V v);

    /**
     * Remove an edge (u, v) from the graph, return true if successful
     */
    public boolean remove(int u, int v);
}
