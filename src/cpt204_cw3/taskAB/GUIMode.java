package cpt204_cw3.taskAB;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class GUIMode{
    private WeightedGraph<City> graph;
    private RoadTripPlanner planner;
    private SpellChecker citySpellChecker;
    private GraphPane graphPane;

    public GUIMode(Stage primaryStage) {
        try {
            initializeGraph();
            initializePlanner();
            initializeSpellChecker();

            // Main layout components
            BorderPane root = new BorderPane();
            root.setPadding(new Insets(10));

            TextField startField = new TextField();
            TextField endField = new TextField();
            ListView<String> attractionsList = new ListView<>();
            Button planButton = new Button("Plan Route");

            populateAttractionsList(attractionsList);

            planButton.setOnAction(e -> handleRoutePlanning(
                    sanitizeInput(startField.getText()),
                    sanitizeInput(endField.getText()),
                    attractionsList.getSelectionModel().getSelectedItems()
            ));

            HBox inputPanel = new HBox(10,
                    new Label("Start:"), startField,
                    new Label("End:"), endField,
                    new Label("Attractions:"), attractionsList,
                    planButton
            );
            inputPanel.setPadding(new Insets(10));

            graphPane = new GraphPane(graph);
            root.setTop(inputPanel);
            root.setCenter(graphPane);

            Scene scene = new Scene(root, 1200, 800);
            primaryStage.setTitle("USA Road Trip Planner");
            primaryStage.setScene(scene);
            primaryStage.show();

            graphPane.layoutGraph();

        } catch (IOException e) {
            ValidationUtil.showAlert("File Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            ValidationUtil.showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void handleRoutePlanning(String start, String end, List<String> attractions) {
        try {
            String validatedStart = validateCity(start, "Starting");
            String validatedEnd = validateCity(end, "Destination");

            if (validatedStart == null || validatedEnd == null) return;

            List<City> route = planner.route(validatedStart, validatedEnd, attractions);
            graphPane.highlightRoute(route);

        } catch (Exception ex) {
            ValidationUtil.showAlert("Planning Error", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private String validateCity(String input, String fieldName) {
        if (planner.findCityIndex(input) != -1) {
            return input;
        }

        List<String> suggestions = citySpellChecker.getSuggestions(input, 3);
        String corrected = ValidationUtil.showSuggestionDialog(input, suggestions);

        if (corrected == null) {
            ValidationUtil.showAlert("Invalid Input",
                    String.format("Invalid %s city: %s", fieldName.toLowerCase(), input),
                    Alert.AlertType.WARNING);
        }
        return corrected;
    }

    private void populateAttractionsList(ListView<String> listView) {
        listView.getItems().addAll(graph.getVertices().stream()
                .filter(c -> !c.getInterest().isEmpty())
                .map(City::getInterest)
                .collect(Collectors.toList()));

        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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
        System.out.println("Starting GUI mode...");
    }
}