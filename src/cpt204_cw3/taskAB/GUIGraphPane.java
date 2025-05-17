package cpt204_cw3.taskAB;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Custom JavaFX Pane for visualizing the road network graph
 * Handles graph layout, rendering, and route highlighting
 */
public class GUIGraphPane extends Pane {

    // Graph data and visualization state
    private final WeightedGraph<City> graph;           // Underlying graph data structure
    private final Map<City, Point2D> nodePositions = new HashMap<>();  // City coordinates mapping
    private List<City> highlightedRoute;   // Currently highlighted optimal route
    private City startCity;                // Route start city (for special styling)
    private City endCity;                  // Route end city (for special styling)

    public GUIGraphPane(WeightedGraph<City> graph) {
        this.graph = graph;
        this.setPrefSize(800, 800);  // Default pane size
    }

    /**
     * Highlights a specific route and updates the visualization
     * @param route List of cities representing the optimal path
     */
    public void highlightRoute(List<City> route) {
        this.highlightedRoute = route;
        if (!route.isEmpty()) {
            this.startCity = route.getFirst();
            this.endCity = route.getLast();
        }
        drawGraph();  // Redraw with new highlighted route
    }

    /**
     * Arranges cities in a circular layout around the center
     * Uses radial positioning algorithm for node placement
     */
    public void layoutGraph() {
        int nodeCount = graph.getSize();
        double centerX = getPrefWidth() / 2;
        double centerY = getPrefHeight() / 2;
        double layoutRadius = Math.min(centerX, centerY) * 0.8;  // 80% of available space

        // Position each city at equal angles around the circle
        for (int i = 0; i < nodeCount; i++) {
            double angle = 2 * Math.PI * i / nodeCount;  // Angle in radians
            double x = centerX + layoutRadius * Math.cos(angle);
            double y = centerY + layoutRadius * Math.sin(angle);
            nodePositions.put(graph.getVertex(i), new Point2D(x, y));
        }
        drawGraph();  // Initial draw after layout
    }

    /**
     * Main drawing method that coordinates visualization updates
     * Draw order: base edges -> nodes -> highlighted elements
     */
    private void drawGraph() {
        getChildren().clear();  // Clear previous drawing

        // Layered drawing approach
        drawAllEdges();     // 1. Draw all base connections
        drawAllNodes();     // 2. Draw all city nodes
        drawHighlightedElements();  // 3. Draw route highlights on top
    }

    /**
     * Draws all road connections as thin gray lines
     */
    private void drawAllEdges() {
        graph.getVertices().forEach(source -> {
            int sourceIndex = graph.getIndex(source);
            graph.neighbors.get(sourceIndex).forEach(edge -> {
                if (edge instanceof WeightedEdge) {
                    City target = graph.getVertex(edge.getV());
                    drawEdge(source, target, "#cccccc", 1);  // Base edge style
                }
            });
        });
    }

    /**
     * Draws all city nodes with labels
     */
    private void drawAllNodes() {
        graph.getVertices().forEach(city -> {
            Point2D position = nodePositions.get(city);
            drawCityNode(city, position);
        });
    }

    /**
     * Draws highlighted route elements (thick lines + direction arrows)
     */
    private void drawHighlightedElements() {
        if (highlightedRoute == null) return;

        // Draw thick red path lines
        for (int i = 1; i < highlightedRoute.size(); i++) {
            City prev = highlightedRoute.get(i - 1);
            City current = highlightedRoute.get(i);
            drawEdge(prev, current, "#e74c3c", 3);  // Highlight color and width
        }

        // Add directional arrows on top of lines
        for (int i = 1; i < highlightedRoute.size(); i++) {
            City prev = highlightedRoute.get(i - 1);
            City current = highlightedRoute.get(i);
            drawDirectionalArrow(prev, current);
        }
    }

    /**
     * Draws a single city node with appropriate styling
     * @param city City to visualize
     * @param position Screen coordinates for node placement
     */
    private void drawCityNode(City city, Point2D position) {
        Circle node = new Circle(position.getX(), position.getY(), 8);
        String color = "#3498db";  // Default node color (blue)

        // Special case styling
        if (city == startCity) color = "#2ecc71";    // Green for start
        else if (city == endCity) color = "#e74c3c"; // Red for end

        node.setStyle("-fx-fill: " + color + ";");

        // Create city name label
        Text label = new Text(position.getX() + 10, position.getY() - 5, city.getCityName());
        label.setStyle("-fx-font-size: 10; -fx-fill: #2c3e50;");  // Dark gray text

        getChildren().addAll(node, label);
    }

    /**
     * Draws a connection line between two cities
     * @param source Origin city
     * @param target Destination city
     * @param color CSS color string
     * @param width Line thickness in pixels
     */
    private void drawEdge(City source, City target, String color, double width) {
        Point2D start = nodePositions.get(source);
        Point2D end = nodePositions.get(target);

        Line line = new Line(start.getX(), start.getY(), end.getX(), end.getY());
        line.setStyle("-fx-stroke: " + color + "; -fx-stroke-width: " + width + ";");
        getChildren().add(line);
    }

    /**
     * Draws directional arrow at the end of an edge
     * @param source Origin city of the edge
     * @param target Destination city of the edge
     */
    private void drawDirectionalArrow(City source, City target) {
        final double arrowSize = 12;  // Pixel size of the arrow
        Point2D start = nodePositions.get(source);
        Point2D end = nodePositions.get(target);

        // Create arrowhead shape (triangle)
        Polygon arrow = new Polygon();
        arrow.getPoints().addAll(
                -arrowSize/2, 0.0,   // Left base point
                arrowSize/2, 0.0,     // Right base point
                0.0, arrowSize        // Tip point
        );

        // Calculate arrow positioning
        double dx = end.getX() - start.getX();
        double dy = end.getY() - start.getY();
        double length = Math.hypot(dx, dy);

        // Offset to prevent arrow overlapping the target node
        double offset = 8 + 3;  // Node radius + line width
        double ratio = (length - offset) / length;
        Point2D arrowPosition = new Point2D(
                start.getX() + dx * ratio,
                start.getY() + dy * ratio
        );

        // Position and style the arrow
        arrow.setTranslateX(arrowPosition.getX());
        arrow.setTranslateY(arrowPosition.getY());
        arrow.setStyle("-fx-fill: #e74c3c;");  // Red color matching highlight

        // Calculate and apply rotation based on edge direction
        double angle = Math.toDegrees(Math.atan2(dy, dx)) - 90;  // Adjust for triangle orientation
        arrow.getTransforms().add(new Rotate(angle, 0, 0));

        getChildren().add(arrow);
    }
}