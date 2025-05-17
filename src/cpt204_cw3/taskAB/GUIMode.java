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

/**
 * GUI implementation for USA Road Trip Planner application
 * Handles user interface components and interaction logic
 */
public class GUIMode {
    // Core data structures
    private WeightedGraph<City> graph;          // Graph representing cities and roads
    private RoadTripPlanner planner;           // Algorithm implementation for route planning
    private SpellChecker citySpellChecker;      // Spell checking for city name validation
    private GUIGraphPane GUIGraphPane;          // Custom component for graph visualization
    private TextArea resultArea;                // Displays planning results and route info

    /**
     * Constructs the GUI interface and initializes core components
     * @param primaryStage Main application window
     */
    public GUIMode(Stage primaryStage) {
        try {
            // Initialize data structures
            initializeGraph();
            initializePlanner();
            initializeSpellChecker();

            // Main layout container with padding
            BorderPane root = new BorderPane();
            root.setPadding(new Insets(10));

            // Input components setup
            TextField startField = new TextField();
            TextField endField = new TextField();
            ListView<String> attractionsList = new ListView<>();
            Button planButton = new Button("Plan Route");

            // Configure results display area
            resultArea = new TextArea();
            resultArea.setEditable(false);
            resultArea.setPrefHeight(150);
            ScrollPane resultScroll = new ScrollPane(resultArea);
            resultScroll.setFitToWidth(true);

            // Setup multi-select attractions list
            populateAttractionsList(attractionsList);
            Label multiSelectHint = new Label("(Ctrl/Cmd-click to select multiple)");
            multiSelectHint.setStyle("-fx-text-fill: #666; -fx-font-size: 10;");

            // Organize input components vertically
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

            // Initialize graph visualization pane
            GUIGraphPane = new GUIGraphPane(graph);
            root.setLeft(inputPanel);
            root.setCenter(GUIGraphPane);

            // Configure route planning button action
            planButton.setOnAction(e -> handleRoutePlanning(
                    sanitizeInput(startField.getText()),
                    sanitizeInput(endField.getText()),
                    attractionsList.getSelectionModel().getSelectedItems()
            ));

            // Set up main application window
            Scene scene = new Scene(root, 1600, 900);
            primaryStage.setTitle("USA Road Trip Planner");
            primaryStage.setScene(scene);
            primaryStage.show();

            // Perform initial graph layout
            GUIGraphPane.layoutGraph();

        } catch (IOException e) {
            GUIValidationUtil.showAlert("File Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            GUIValidationUtil.showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Creates a standardized input row with label and control
     * @param label Text description for the input field
     * @param field Input component (TextField, etc.)
     * @return HBox containing formatted input row
     */
    private HBox createInputRow(String label, Control field) {
        HBox row = new HBox(5, new Label(label), field);
        field.setPrefWidth(200);
        return row;
    }

    /**
     * Handles route planning process when user clicks the plan button
     * @param start Origin city name
     * @param end Destination city name
     * @param attractions List of selected attractions/points of interest
     */
    private void handleRoutePlanning(String start, String end, List<String> attractions) {
        try {
            resultArea.clear();  // Clear previous results

            // Validate and correct city names
            String validatedStart = validateCity(start, "Starting");
            String validatedEnd = validateCity(end, "Destination");
            if (validatedStart == null || validatedEnd == null) return;

            // Calculate optimal route
            List<City> route = planner.route(validatedStart, validatedEnd, attractions);

            // Update visualization and results display
            GUIGraphPane.highlightRoute(route);
            if (route.isEmpty()) {
                resultArea.setText("No valid route found!");
            } else {
                resultArea.setText(formatResult(validatedStart, validatedEnd, attractions, route));
            }

        } catch (Exception ex) {
            GUIValidationUtil.showAlert("Planning Error", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Formats planning results into human-readable string
     * @param start Validated origin city
     * @param end Validated destination city
     * @param attractions Selected points of interest
     * @param route Calculated optimal route
     * @return Formatted result string
     */
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

    /**
     * Calculates total distance of a given route
     * @param route List of cities in travel order
     * @return Total distance in miles
     */
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

    /**
     * Validates city name and provides spelling suggestions
     * @param input Raw city name input
     * @param fieldName Context for error messages
     * @return Validated city name or null if invalid
     */
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

    /**
     * Populates attractions list with unique values from graph data
     * @param listView ListView component to populate
     */
    private void populateAttractionsList(ListView<String> listView) {
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listView.setPrefHeight(200);

        listView.getItems().setAll(graph.getVertices().stream()
                .filter(c -> !c.getInterest().isEmpty())
                .map(City::getInterest)
                .distinct()
                .collect(Collectors.toList()));
    }

    /**
     * Sanitizes user input by removing underscores and trimming whitespace
     * @param input Raw user input
     * @return Cleaned input string
     */
    private String sanitizeInput(String input) {
        return input.replaceAll("_", "").trim();
    }

    // Initialization methods
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