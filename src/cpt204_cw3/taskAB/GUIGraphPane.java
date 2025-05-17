package cpt204_cw3.taskAB;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUIGraphPane extends Pane {

    private final WeightedGraph<City> graph;
    private final Map<City, Point2D> nodePositions = new HashMap<>();
    private List<City> highlightedRoute;
    private City startCity;
    private City endCity;

    public GUIGraphPane(WeightedGraph<City> graph) {
        this.graph = graph;
        this.setPrefSize(800, 800);
    }

    public void highlightRoute(List<City> route) {
        this.highlightedRoute = route;
        if (!route.isEmpty()) {
            this.startCity = route.getFirst();
            this.endCity = route.getLast();
        }
        drawGraph();
    }

    public void layoutGraph() {
        int nodeCount = graph.getSize();
        double centerX = getPrefWidth() / 2;
        double centerY = getPrefHeight() / 2;
        double layoutRadius = Math.min(centerX, centerY) * 0.8;

        for (int i = 0; i < nodeCount; i++) {
            double angle = 2 * Math.PI * i / nodeCount;
            double x = centerX + layoutRadius * Math.cos(angle);
            double y = centerY + layoutRadius * Math.sin(angle);
            nodePositions.put(graph.getVertex(i), new Point2D(x, y));
        }
        drawGraph();
    }

    private void drawGraph() {
        getChildren().clear();

        // 1. Draw base network
        drawAllEdges();

        // 2. Draw city nodes
        drawAllNodes();

        // 3. Draw highlighted elements on top
        if (highlightedRoute != null) {
            drawHighlightedElements();
        }
    }

    private void drawAllEdges() {
        graph.getVertices().forEach(source -> {
            int sourceIndex = graph.getIndex(source);
            graph.neighbors.get(sourceIndex).forEach(edge -> {
                if (edge instanceof WeightedEdge) {
                    City target = graph.getVertex(edge.getV());
                    drawEdge(source, target, "#cccccc", 1);
                }
            });
        });
    }

    private void drawAllNodes() {
        graph.getVertices().forEach(city -> {
            Point2D position = nodePositions.get(city);
            drawCityNode(city, position);
        });
    }

    private void drawHighlightedElements() {
        // Draw thick path lines first
        for (int i = 1; i < highlightedRoute.size(); i++) {
            City prev = highlightedRoute.get(i - 1);
            City current = highlightedRoute.get(i);
            drawEdge(prev, current, "#e74c3c", 3);
        }

        // Draw arrows on top of lines
        for (int i = 1; i < highlightedRoute.size(); i++) {
            City prev = highlightedRoute.get(i - 1);
            City current = highlightedRoute.get(i);
            drawDirectionalArrow(prev, current);
        }
    }

    private void drawCityNode(City city, Point2D position) {
        Circle node = new Circle(position.getX(), position.getY(), 8);
        String color = "#3498db";

        if (city == startCity) color = "#2ecc71";
        else if (city == endCity) color = "#e74c3c";

        node.setStyle("-fx-fill: " + color + ";");

        Text label = new Text(position.getX() + 10, position.getY() - 5, city.getCityName());
        label.setStyle("-fx-font-size: 10; -fx-fill: #2c3e50;");

        getChildren().addAll(node, label);
    }

    private void drawEdge(City source, City target, String color, double width) {
        Point2D start = nodePositions.get(source);
        Point2D end = nodePositions.get(target);

        Line line = new Line(
                start.getX(), start.getY(),
                end.getX(), end.getY()
        );
        line.setStyle("-fx-stroke: " + color + "; -fx-stroke-width: " + width + ";");
        getChildren().add(line);
    }

    private void drawDirectionalArrow(City source, City target) {
        final double arrowSize = 12;
        Point2D start = nodePositions.get(source);
        Point2D end = nodePositions.get(target);

        // Create arrow shape (pointing away from source)
        Polygon arrow = new Polygon();
        arrow.getPoints().addAll(
                -arrowSize/2, (double) 0,   // Left base point
                arrowSize/2, (double) 0,    // Right base point
                (double) 0, arrowSize       // Tip point
        );

        // Position at target end with offset
        double dx = end.getX() - start.getX();
        double dy = end.getY() - start.getY();
        double length = Math.hypot(dx, dy);

        // Calculate offset to prevent overlap with node
        double offset = 8 + 3; // Node radius + line width
        double ratio = (length - offset) / length;
        Point2D arrowPosition = new Point2D(
                start.getX() + dx * ratio,
                start.getY() + dy * ratio
        );

        arrow.setTranslateX(arrowPosition.getX());
        arrow.setTranslateY(arrowPosition.getY());
        arrow.setStyle("-fx-fill: #e74c3c;");

        // Calculate rotation angle
        double angle = Math.toDegrees(Math.atan2(dy, dx)) - 90;
        arrow.getTransforms().add(new Rotate(angle, 0, 0));

        getChildren().add(arrow);
    }
}