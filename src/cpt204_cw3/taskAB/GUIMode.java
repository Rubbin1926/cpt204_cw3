package cpt204_cw3.taskAB;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class GUIMode{
    private WeightedGraph<City> graph;
    private RoadTripPlanner planner;
    private SpellChecker citySpellChecker;
    private GUIGraphPane GUIGraphPane;
    private TextArea resultArea;

    public GUIMode(Stage primaryStage) {
        try {
            initializeGraph();
            initializePlanner();
            initializeSpellChecker();

            // Main layout components
            BorderPane root = new BorderPane();
            root.setPadding(new Insets(10));

            // Input components
            TextField startField = new TextField();
            TextField endField = new TextField();
            ListView<String> attractionsList = new ListView<>();
            Button planButton = new Button("Plan Route");

            // Result display components
            resultArea = new TextArea();
            resultArea.setEditable(false);
            resultArea.setPrefHeight(150);
            ScrollPane resultScroll = new ScrollPane(resultArea);
            resultScroll.setFitToWidth(true);

            // Configure attractions list
            populateAttractionsList(attractionsList);
            Label multiSelectHint = new Label("(Ctrl/Cmd-click to select multiple)");
            multiSelectHint.setStyle("-fx-text-fill: #666; -fx-font-size: 10;");

            // Layout organization
            VBox inputPanel = new VBox(10,
                    createInputRow("Start:", startField),
                    createInputRow("End:  ", endField),
                    new VBox(5,
                            new Label("Attractions:"),
                            multiSelectHint,
                            attractionsList
                    ),
                    new HBox(10, planButton, resultScroll)
            );
            inputPanel.setPadding(new Insets(10));

            // Graph display area
            GUIGraphPane = new GUIGraphPane(graph);
            root.setLeft(inputPanel);
            root.setCenter(GUIGraphPane);

            // Event handling
            planButton.setOnAction(e -> handleRoutePlanning(
                    sanitizeInput(startField.getText()),
                    sanitizeInput(endField.getText()),
                    attractionsList.getSelectionModel().getSelectedItems()
            ));

            // Stage configuration
            Scene scene = new Scene(root, 1600, 900);
            primaryStage.setTitle("USA Road Trip Planner");
            primaryStage.setScene(scene);
            primaryStage.show();

            GUIGraphPane.layoutGraph();

        } catch (IOException e) {
            GUIValidationUtil.showAlert("File Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            GUIValidationUtil.showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private HBox createInputRow(String label, Control field) {
        HBox row = new HBox(5, new Label(label), field);
        field.setPrefWidth(200);
        return row;
    }

    private void handleRoutePlanning(String start, String end, List<String> attractions) {
        try {
            resultArea.clear();  // Clear previous results

            String validatedStart = validateCity(start, "Starting");
            String validatedEnd = validateCity(end, "Destination");

            if (validatedStart == null || validatedEnd == null) return;

            List<City> route = planner.route(validatedStart, validatedEnd, attractions);

            // Update graph visualization
            GUIGraphPane.highlightRoute(route);

            // Update text results
            if (route.isEmpty()) {
                resultArea.setText("No valid route found!");
            } else {
                resultArea.setText(formatResult(validatedStart, validatedEnd, attractions, route));
            }

        } catch (Exception ex) {
            GUIValidationUtil.showAlert("Planning Error", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Formats results similar to terminal output
    private String formatResult(String start, String end, List<String> attractions, List<City> route) {
        StringBuilder sb = new StringBuilder();
        sb.append("Result:\n");
        sb.append("Start: ").append(start).append("\n");
        sb.append("Destination: ").append(end).append("\n");
        sb.append("Attractions: ").append(attractions).append("\n");

        if (route.isEmpty()) {
            sb.append("No valid route found!");
        } else {
            sb.append("Optimal Route: [");
            route.forEach(city -> sb.append(city.getCityName()).append(", "));
            sb.setLength(sb.length()-2);  // Remove trailing comma
            sb.append("]\n");
            sb.append(String.format("Total Distance: %.1f miles", calculateTotalDistance(route)));
        }
        return sb.toString();
    }

    private double calculateTotalDistance(List<City> route) {
        double total = 0;
        for (int i = 1; i < route.size(); i++) {
            try {
                int u = graph.getIndex(route.get(i - 1));
                int v = graph.getIndex(route.get(i));
                total += graph.getWeight(u, v);
            } catch (Exception e) {
                return -1;
            }
        }
        return total;
    }

    private String validateCity(String input, String fieldName) {
        if (planner.findCityIndex(input) != -1) {
            return input;
        }

        List<String> suggestions = citySpellChecker.getSuggestions(input, 3);
        String corrected = GUIValidationUtil.showSuggestionDialog(input, suggestions);

        if (corrected == null) {
            GUIValidationUtil.showAlert("Invalid Input",
                    String.format("Invalid %s city: %s", fieldName.toLowerCase(), input),
                    Alert.AlertType.WARNING);
        }
        return corrected;
    }

    private void populateAttractionsList(ListView<String> listView) {
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listView.setPrefHeight(200);

        listView.getItems().setAll(graph.getVertices().stream()
                .filter(c -> !c.getInterest().isEmpty())
                .map(City::getInterest)
                .distinct()
                .collect(Collectors.toList()));
    }

    private String sanitizeInput(String input) {
        return input.replaceAll("_", "").trim();
    }

    private void initializeGraph() throws IOException {
        graph = CSVGraphBuilder.buildGraphFromCSV(
                "src/cpt204_cw3/resources/attractions.csv",
                "src/cpt204_cw3/resources/roads.csv"
        );
    }

    private void initializePlanner() {
        planner = new RoadTripPlanner(graph);
    }

    private void initializeSpellChecker() {
        List<String> cityNames = graph.getVertices().stream()
                .map(City::getCityName)
                .collect(Collectors.toList());
        citySpellChecker = new SpellChecker(cityNames);
    }

    public static void main(String[] args) {
        System.out.println("Please run Main.java");
    }
}