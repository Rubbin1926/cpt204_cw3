package cpt204_cw3.java;
import cpt204_cw3.java.Edge;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EdgeTest{
    @Test
    public void testEdgeConstructor() {
        Edge edge = new Edge(1, 2);
        assertEquals(1, edge.getU());
        assertEquals(2, edge.getV());
    }

    @Test
    public void testSettersAndGetters() {
        Edge edge = new Edge(1, 2);
        edge.setU(3);
        edge.setV(4);
        assertEquals(3, edge.getU());
        assertEquals(4, edge.getV());
    }

    @Test
    public void testEquals() {
        Edge edge1 = new Edge(1, 2);
        Edge edge2 = new Edge(1, 2);
        Edge edge3 = new Edge(2, 3);
        assertEquals(edge1, edge2);
        assertNotEquals(edge1, edge3);
    }
}