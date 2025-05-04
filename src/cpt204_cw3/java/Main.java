package cpt204_cw3.java;

import java.io.IOException;
import java.util.*;

public class Main {
    private static WeightedGraph<City> graph;
    private static RoadTripPlanner planner;

    public static void main(String[] args) {
        try {
            // 初始化图
            graph = CSVGraphBuilder.buildGraphFromCSV(
                    "src/cpt204_cw3/resources/attractions.csv",
                    "src/cpt204_cw3/resources/roads.csv"
            );
            planner = new RoadTripPlanner(graph);

            // 获取用户输入
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter starting city (e.g., New York NY): ");
            String start = scanner.nextLine().replaceAll("_", "").trim();

            System.out.print("Enter destination city (e.g., Miami FL): ");
            String end = scanner.nextLine().replaceAll("_", "").trim();

            System.out.print("Enter attractions (comma-separated, e.g., Hollywood Sign) ");
            String attractionsInput = scanner.nextLine()
                    .replaceAll("_", "")
                    .trim();

            // Process and clean attractions input
            List<String> attractions = new ArrayList<>();
            if (!attractionsInput.isEmpty()) {
                for (String item : attractionsInput.split("\\s*,\\s*")) {
                    if (!item.trim().isEmpty()) {  // Filter empty items
                        attractions.add(item.trim());
                    }
                }
            }

            // 计算路线
            List<City> route = planner.route(start, end, attractions);

            // 输出结果
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

        } catch (IOException e) {
            System.err.println("File error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Input error: " + e.getMessage());
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