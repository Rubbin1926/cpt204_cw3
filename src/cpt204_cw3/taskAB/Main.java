package cpt204_cw3.taskAB;

import java.io.IOException;
import java.util.*;

public class Main {
    private static WeightedGraph<City> graph;
    private static RoadTripPlanner planner;

    public static void main(String[] args) {
        try {
            // Initialize the graph
            graph = CSVGraphBuilder.buildGraphFromCSV(
                    "src/cpt204_cw3/resources/attractions.csv",
                    "src/cpt204_cw3/resources/roads.csv"
            );
            planner = new RoadTripPlanner(graph);

            // Get user input
            Scanner scanner = new Scanner(System.in);

            // 1. Process starting city with immediate validation
            System.out.print("Enter starting city (e.g., New York NY): ");
            String start = scanner.nextLine().replaceAll("_", "").trim();
            validateCity(start, true);

            // 2. Process destination city with immediate validation
            System.out.print("Enter destination city (e.g., Chicago IL): ");
            String end = scanner.nextLine().replaceAll("_", "").trim();
            validateCity(end, false);

            // 3. Process attractions with immediate validation
            System.out.print("Enter attractions (comma-separated, e.g., Hollywood Sign): ");
            String attractionsInput = scanner.nextLine()
                    .replaceAll("_", "")
                    .trim();
            List<String> attractions = processAttractions(attractionsInput);

            // Calculate the route
            List<City> route = planner.route(start, end, attractions);

            // Output the result
            printResult(start, end, attractions, route);

        } catch (IOException e) {
            System.err.println("File error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Input error: " + e.getMessage());
        }
    }

    private static List<String> processAttractions(String input) {
        List<String> attractions = new ArrayList<>();
        Set<String> uniqueAttractions = new HashSet<>();
        List<String> duplicates = new ArrayList<>();

        if (!input.isEmpty()) {
            for (String item : input.split("\\s*,\\s*")) {
                String trimmedItem = item.trim();
                if (!trimmedItem.isEmpty()) {
                    validateAttraction(trimmedItem);

                    // detect duplicates
                    if (uniqueAttractions.contains(trimmedItem)) {
                        duplicates.add(trimmedItem);
                    } else {
                        uniqueAttractions.add(trimmedItem);
                        attractions.add(trimmedItem);
                    }
                }
            }
        }

        // return the attractions list without duplicates
        // Warning: Duplicate attractions detected and removed
        if (!duplicates.isEmpty()) {
            System.err.println("Warning: Duplicate attractions detected and removed: " + duplicates);
        }

        return attractions;
    }

    private static void validateCity(String cityName, boolean isStart) {
        int index = planner.findCityIndex(cityName);
        if (index == -1) {
            String errorType = isStart ? "Starting" : "Ending";
            throw new IllegalArgumentException(errorType + " city not found: " + cityName);
        }
    }

    private static void validateAttraction(String attraction) {
        int index = planner.findAttractionIndex(attraction);
        if (index == -1) {
            throw new IllegalArgumentException("Attraction not found: " + attraction);
        }
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