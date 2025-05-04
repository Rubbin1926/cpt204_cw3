package cpt204_cw3.taskAB;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVGraphBuilder {

    // Static factory method to build graph from CSV files
    public static WeightedGraph<City> buildGraphFromCSV(String attractionsPath, String roadsPath)
            throws IOException, NumberFormatException {

        Map<String, City> cityMap = new HashMap<>();
        List<Road> tempRoads = new ArrayList<>();

        // Read data from CSVs
        readAttractions(attractionsPath, cityMap);
        readRoads(roadsPath, cityMap, tempRoads);

        // Create vertex list and index mapping
        List<City> vertices = new ArrayList<>(cityMap.values());
        Map<String, Integer> cityIndices = new HashMap<>();
        for (int i = 0; i < vertices.size(); i++) {
            cityIndices.put(vertices.get(i).getCityName(), i);
        }

        // Convert roads to weighted edges
        List<WeightedEdge> edges = new ArrayList<>();
        for (Road road : tempRoads) {
            int u = cityIndices.get(road.cityA);
            int v = cityIndices.get(road.cityB);
            edges.add(new WeightedEdge(u, v, road.distance));
            edges.add(new WeightedEdge(v, u, road.distance)); // For undirected graph
        }

        return new WeightedGraph<>(vertices, edges);
    }

    private static void readAttractions(String filename, Map<String, City> cityMap)
            throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length < 2) continue;

                String interest = parts[0].trim();
                String cityName = parts[1].trim();

                // Merge attractions for existing cities
                if (cityMap.containsKey(cityName)) {
                    City existing = cityMap.get(cityName);
                    existing.setInterest(existing.getInterest() + ", " + interest);
                } else {
                    cityMap.put(cityName, new City(cityName, interest));
                }
            }
        }
    }

    private static void readRoads(String filename, Map<String, City> cityMap, List<Road> tempRoads)
            throws IOException, NumberFormatException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 3) continue;

                String cityA = parts[0].trim();
                String cityB = parts[1].trim();
                int distance = Integer.parseInt(parts[2].trim());

                // Add missing cities without attractions
                if (!cityMap.containsKey(cityA)) {
                    cityMap.put(cityA, new City(cityA, ""));
                }
                if (!cityMap.containsKey(cityB)) {
                    cityMap.put(cityB, new City(cityB, ""));
                }

                tempRoads.add(new Road(cityA, cityB, distance));
            }
        }
    }

    // Internal road data carrier
    private static class Road {
        final String cityA;
        final String cityB;
        final int distance;

        Road(String cityA, String cityB, int distance) {
            this.cityA = cityA;
            this.cityB = cityB;
            this.distance = distance;
        }
    }
}