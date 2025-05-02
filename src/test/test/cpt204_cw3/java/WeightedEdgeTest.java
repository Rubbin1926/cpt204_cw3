package cpt204_cw3.java;
import cpt204_cw3.java.WeightedEdge;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WeightedEdgeTest {
    @Test
    public void testWeightedEdgeConstructor() {
        WeightedEdge edge = new WeightedEdge(1, 2, 3.5);
        assertEquals(1, edge.getU());
        assertEquals(2, edge.getV());
        assertEquals(3.5, edge.getWeight());
    }

    @Test
    public void testSetWeight() {
        WeightedEdge edge = new WeightedEdge(1, 2, 3.5);
        edge.setWeight(4.5);
        assertEquals(4.5, edge.getWeight());
    }

    @Test
    public void testCompareTo() {
        WeightedEdge edge1 = new WeightedEdge(1, 2, 3.5);
        WeightedEdge edge2 = new WeightedEdge(2, 3, 4.5);
        assertTrue(edge1.compareTo(edge2) < 0);
        assertTrue(edge2.compareTo(edge1) > 0);
        assertEquals(0, edge1.compareTo(edge1));
    }
}
