package cpt204_cw3.taskAB;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static WeightedGraph<City> graph;
    private static RoadTripPlanner planner;
    private static SpellChecker citySpellChecker;

    public static void main(String[] args) {
        try {
            initializeGraph();
            initializePlanner();
            initializeSpellChecker();

            Scanner scanner = new Scanner(System.in);
            InputValidator validator = new InputValidator(
                    citySpellChecker,
                    planner,
                    scanner
            );

            String start = validator.getValidCity("starting", "New York NY");
            String end = validator.getValidCity("destination", "Chicago IL");
            List<String> attractions = validator.getValidAttractions();

            List<City> route = planner.route(start, end, attractions);
            printResult(start, end, attractions, route);

        } catch (IOException e) {
            System.err.println("File error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

    private static void initializeGraph() throws IOException {
        graph = CSVGraphBuilder.buildGraphFromCSV(
                "src/cpt204_cw3/resources/attractions.csv",
                "src/cpt204_cw3/resources/roads.csv"
        );
    }

    private static void initializePlanner() throws IOException {
        planner = new RoadTripPlanner(graph);
    }

    private static void initializeSpellChecker() {
        List<String> cityNames = graph.getVertices().stream()
                .map(City::getCityName)
                .collect(Collectors.toList());
        citySpellChecker = new SpellChecker(cityNames);
    }

    private static void printResult(String start, String end, List<String> attractions, List<City> route) {
        System.out.println("\nResult: ");
        System.out.println("Start: " + start);
        System.out.println("Destination: " + end);
        System.out.println("Attractions: " + attractions);

        if (route.isEmpty()) {
            System.out.println("No valid route found!");
        } else {
            System.out.print("Optimal Route: [");
            for (int i = 0; i < route.size(); i++) {
                System.out.print(route.get(i).getCityName());
                if (i != route.size() - 1) System.out.print(", ");
            }
            System.out.println("]");
            System.out.printf("Total Distance: %.1f miles%n", calculateTotalDistance(route));
        }
    }

    private static double calculateTotalDistance(List<City> route) {
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
}