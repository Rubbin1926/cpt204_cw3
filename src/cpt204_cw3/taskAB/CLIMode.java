package cpt204_cw3.taskAB;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Command-line interface for the Road Trip Planner application
 * Handles terminal-based user interaction and result display
 */
public class CLIMode {
    // Core application components
    private static WeightedGraph<City> graph;         // Road network graph structure
    private static RoadTripPlanner planner;          // Route planning algorithm
    private static SpellChecker citySpellChecker;    // City name validation helper

    /**
     * Main entry point for terminal mode execution
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            // Initialize data structures
            initializeGraph();
            initializePlanner();
            initializeSpellChecker();

            // Set up input handling
            Scanner scanner = new Scanner(System.in);
            InputValidator validator = new InputValidator(
                    citySpellChecker,
                    planner,
                    scanner
            );

            // Get validated user inputs
            String start = validator.getValidCity("starting", "New York NY");
            String end = validator.getValidCity("destination", "Chicago IL");
            List<String> attractions = validator.getValidAttractions();

            // Calculate and display optimal route
            List<City> route = planner.route(start, end, attractions);
            printResult(start, end, attractions, route);

        } catch (IOException e) {
            System.err.println("File error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

    /**
     * Initializes road network graph from CSV files
     */
    private static void initializeGraph() throws IOException {
        graph = CSVGraphBuilder.buildGraphFromCSV(
                "src/cpt204_cw3/resources/attractions.csv",
                "src/cpt204_cw3/resources/roads.csv"
        );
    }

    /**
     * Initializes the route planning algorithm
     */
    private static void initializePlanner() throws IOException {
        planner = new RoadTripPlanner(graph);
    }

    /**
     * Initializes spell checker with city names from the graph
     */
    private static void initializeSpellChecker() {
        List<String> cityNames = graph.getVertices().stream()
                .map(City::getCityName)
                .collect(Collectors.toList());
        citySpellChecker = new SpellChecker(cityNames);
    }

    /**
     * Displays formatted planning results to the console
     * @param start Validated starting city
     * @param end Validated destination city
     * @param attractions Selected points of interest
     * @param route Calculated optimal route (empty if none found)
     */
    private static void printResult(String start, String end, List<String> attractions, List<City> route) {
        System.out.println("\nResult: ");
        System.out.println("Start: " + start);
        System.out.println("Destination: " + end);
        System.out.println("Attractions: " + attractions);

        if (route.isEmpty()) {
            System.out.println("No valid route found!");
        } else {
            // Format route as comma-separated city names
            System.out.print("Optimal Route: [");
            for (int i = 0; i < route.size(); i++) {
                System.out.print(route.get(i).getCityName());
                if (i != route.size() - 1) System.out.print(", ");
            }
            System.out.println("]");

            // Display total distance with 1 decimal place
            System.out.printf("Total Distance: %.1f miles%n", calculateTotalDistance(route));
        }
    }

    /**
     * Calculates total distance of a given route
     * @param route List of cities in travel order
     * @return Total distance in miles, or -1 for invalid routes
     */
    private static double calculateTotalDistance(List<City> route) {
        double total = 0;
        for (int i = 1; i < route.size(); i++) {
            try {
                // Sum weights between consecutive cities
                int u = graph.getIndex(route.get(i - 1));
                int v = graph.getIndex(route.get(i));
                total += graph.getWeight(u, v);
            } catch (Exception e) {
                return -1;  // Indicate calculation error
            }
        }
        return total;
    }
}