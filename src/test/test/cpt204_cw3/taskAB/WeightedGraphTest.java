package cpt204_cw3.taskAB;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class WeightedGraphTest {
    private WeightedGraph<String> graph;

    @BeforeEach
    public void setUp() {
        graph = new WeightedGraph<>();
    }

    // Test constructor with vertices and weighted edges
    @Test
    public void testConstructorWithVerticesAndEdges() {
        List<String> vertices = List.of("A", "B", "C");
        List<WeightedEdge> edges = List.of(
                new WeightedEdge(0, 1, 1.5),
                new WeightedEdge(1, 2, 2.0)
        );

        WeightedGraph<String> graph = new WeightedGraph<>(vertices, edges);
        assertEquals(3, graph.getSize());
        assertEquals(1, graph.getNeighbors(0).size());
    }

    // Test adding a single weighted edge
    @Test
    public void testAddWeightedEdge() {
        graph.addVertex("A");
        graph.addVertex("B");
        assertTrue(graph.addWeightedEdge(0, 1, 3.0));
        assertFalse(graph.addWeightedEdge(0, 1, 3.0)); // Duplicate edge
    }

    // Test adding invalid edges (out-of-bounds vertices)
    @Test
    public void testAddWeightedEdgeWithInvalidVertex() {
        graph.addVertex("A");
        assertThrows(IllegalArgumentException.class, () -> graph.addWeightedEdge(0, 1, 2.0));
    }

    // Test adding self-loop edge (should fail)
    @Test
    public void testAddSelfLoopEdge() {
        graph.addVertex("A");
        assertFalse(graph.addWeightedEdge(0, 0, 5.0));
    }

    // Test batch addition of weighted edges
    @Test
    public void testAddWeightedEdges() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        List<WeightedEdge> edges = List.of(
                new WeightedEdge(0, 1, 1.0),
                new WeightedEdge(1, 2, 2.0)
        );
        assertTrue(graph.addWeightedEdges(edges));
        assertEquals(2, graph.getNeighbors(1).size());
    }

    // Test retrieving edge weight
    @Test
    public void testGetWeight() throws Exception {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addWeightedEdge(0, 1, 4.5);
        assertEquals(4.5, graph.getWeight(0, 1), 0.001);
    }

    // Test retrieving non-existent edge weight (throws exception)
    @Test
    public void testGetWeightOfNonExistentEdge() {
        graph.addVertex("A");
        graph.addVertex("B");
        assertThrows(Exception.class, () -> graph.getWeight(0, 1));
    }

    // Test clear functionality inherited from UnWeightedGraph
    @Test
    public void testClear() {
        graph.addVertex("A");
        graph.addWeightedEdge(0, 0, 1.0);
        graph.clear();
        assertEquals(0, graph.getSize());
    }

    // Test edge weight via WeightedEdge object
    @Test
    public void testGetWeightWithEdgeObject() throws Exception {
        graph.addVertex("A");
        graph.addVertex("B");
        WeightedEdge edge = new WeightedEdge(0, 1, 3.5);
        graph.addWeightedEdge(edge.getU(), edge.getV(), edge.getWeight());
        assertEquals(3.5, graph.getWeight(edge), 0.001);
    }
}
