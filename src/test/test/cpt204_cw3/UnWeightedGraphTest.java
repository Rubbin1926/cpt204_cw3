package cpt204_cw3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class UnWeightedGraphTest {
    private UnWeightedGraph<String> graph;

    @BeforeEach
    public void setUp() {
        graph = new UnWeightedGraph<>();
    }

    // Test vertex operations
    @Test
    public void testAddVertex() {
        assertTrue(graph.addVertex("A"));
        assertFalse(graph.addVertex("A")); // Duplicate addition should fail
        assertEquals(1, graph.getSize());
    }

    @Test
    public void testAddVertexes() {
        ArrayList<String> vertices = new ArrayList<>(List.of("A", "B", "C"));
        assertTrue(graph.addVertexes(vertices));
        assertEquals(3, graph.getVertices().size());
    }

    @Test
    public void testRemoveVertex() {
        graph.addVertex("A");
        assertTrue(graph.remove("A"));
        assertFalse(graph.remove("B")); // Non-existent vertex
        assertEquals(0, graph.getSize());
    }

    // Test edge operations
    @Test
    public void testAddEdge() {
        graph.addVertex("A");
        graph.addVertex("B");
        assertTrue(graph.addEdge(0, 1)); // A -> B
        assertFalse(graph.addEdge(0, 1)); // Duplicate edge
        assertEquals(1, graph.getDegree(0));
    }

    @Test
    public void testAddEdges() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        List<Edge> edges = List.of(new Edge(0, 1), new Edge(1, 2));
        assertTrue(graph.addEdges(edges));
        assertEquals(2, graph.getNeighbors(1).size());
    }

    @Test
    public void testRemoveEdge() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge(0, 1);
        assertTrue(graph.remove(0, 1));
        assertFalse(graph.remove(0, 1)); // Already removed edge
        assertEquals(0, graph.getDegree(0));
    }

    // Test query methods
    @Test
    public void testGetNeighbors() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge(0, 1); // A -> B
        graph.addEdge(0, 2); // A -> C

        List<Integer> neighbors = graph.getNeighbors(0);
        assertEquals(2, neighbors.size());
        assertTrue(neighbors.contains(1) && neighbors.contains(2));
    }

    @Test
    public void testGetIndex() {
        graph.addVertex("A");
        graph.addVertex("B");
        assertEquals(0, graph.getIndex("A"));
        assertEquals(-1, graph.getIndex("C")); // Non-existent vertex
    }

    @Test
    public void testClear() {
        graph.addVertex("A");
        graph.addEdge(0, 0); // Self-loop edge
        graph.clear();
        assertEquals(0, graph.getSize());
        assertTrue(graph.getVertices().isEmpty());
    }

    // Test exception cases
    @Test
    public void testInvalidEdgeAddition() {
        graph.addVertex("A");
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge(0, 1)); // Vertex 1 doesn't exist
    }
}