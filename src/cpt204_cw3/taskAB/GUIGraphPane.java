package cpt204_cw3.taskAB;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUIGraphPane extends Pane {

    private final WeightedGraph<City> graph;
    private final Map<City, Point2D> positions = new HashMap<>();
    private List<City> highlightedRoute;

    public GUIGraphPane(WeightedGraph<City> graph) {
        this.graph = graph;
        this.setPrefSize(800, 800);
    }

    public void layoutGraph() {
        // Simple circular layout for demonstration
        int numCities = graph.getSize();
        double centerX = getPrefWidth() / 2;
        double centerY = getPrefHeight() / 2;
        double radius = Math.min(centerX, centerY) * 0.8;

        for (int i = 0; i < numCities; i++) {
            double angle = 2 * Math.PI * i / numCities;
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);
            positions.put(graph.getVertex(i), new Point2D(x, y));
        }
        drawGraph();
    }

    public void highlightRoute(List<City> route) {
        this.highlightedRoute = route;
        drawGraph();
    }

    private void drawGraph() {
        getChildren().clear();

        // Draw edges
        graph.getVertices().forEach(city -> {
            int u = graph.getIndex(city);
            graph.neighbors.get(u).forEach(edge -> {
                if (edge instanceof WeightedEdge) {
                    City neighbor = graph.getVertex(edge.getV());
                    drawEdge(city, neighbor);
                }
            });
        });

        // Draw nodes
        graph.getVertices().forEach(city -> {
            Point2D pos = positions.get(city);
            drawNode(city, pos);
        });

        // Highlight path
        if (highlightedRoute != null) {
            for (int i = 1; i < highlightedRoute.size(); i++) {
                City prev = highlightedRoute.get(i - 1);
                City current = highlightedRoute.get(i);
                drawHighlightedEdge(prev, current);
            }
        }
    }

    private void drawEdge(City u, City v) {
        Line line = new Line(
                positions.get(u).getX(), positions.get(u).getY(),
                positions.get(v).getX(), positions.get(v).getY()
        );
        line.setStyle("-fx-stroke: #cccccc; -fx-stroke-width: 1;");
        getChildren().add(line);
    }

    private void drawNode(City city, Point2D pos) {
        Circle circle = new Circle(pos.getX(), pos.getY(), 5);
        circle.setStyle("-fx-fill: #3498db;");
        getChildren().add(circle);

        Text label = new Text(pos.getX() + 5, pos.getY() - 5, city.getCityName());
        label.setStyle("-fx-font-size: 10;");
        getChildren().add(label);
    }

    private void drawHighlightedEdge(City u, City v) {
        Line line = new Line(
                positions.get(u).getX(), positions.get(u).getY(),
                positions.get(v).getX(), positions.get(v).getY()
        );
        line.setStyle("-fx-stroke: #e74c3c; -fx-stroke-width: 3;");
        getChildren().add(line);
    }
}