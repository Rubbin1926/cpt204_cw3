package cpt204_cw3.java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVGraphBuilder {
    public static void readAttractions(String filename, Map<String, City> cityMap) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            br.readLine(); // Skip the header line
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2); // Split into two parts
                if (parts.length < 2) continue;
                String interest = parts[0].trim();
                String cityName = parts[1].trim();

                // Merge attractions for the same city
                if (cityMap.containsKey(cityName)) {
                    City existing = cityMap.get(cityName);
                    existing.setInterest(existing.getInterest() + ", " + interest);
                } else {
                    cityMap.put(cityName, new City(cityName, interest));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readRoads(String filename, Map<String, City> cityMap, List<Road> tempRoads) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            br.readLine(); // Skip the header line
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 3) continue;
                String cityA = parts[0].trim();
                String cityB = parts[1].trim();
                int distance = Integer.parseInt(parts[2].trim());

                // Ensure all cities are in cityMap
                if (!cityMap.containsKey(cityA)) {
                    cityMap.put(cityA, new City(cityA, "")); // City without attractions
                }
                if (!cityMap.containsKey(cityB)) {
                    cityMap.put(cityB, new City(cityB, ""));
                }

                tempRoads.add(new Road(cityA, cityB, distance));
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    // Helper class to temporarily store road information
    public static class Road {
        String cityA;
        String cityB;
        int distance;

        Road(String cityA, String cityB, int distance) {
            this.cityA = cityA;
            this.cityB = cityB;
            this.distance = distance;
        }
    }

    //    public static void main(String[] args) {
//        // Map to store city names to City objects
//        Map<String, City> cityMap = new HashMap<>();
//        // Temporarily store road information
//        List<Road> tempRoads = new ArrayList<>();
//
//        // Read attractions.csv and populate cityMap
//        readAttractions("src/cpt204_cw3/resources/attractions.csv", cityMap);
//
//        // Read roads.csv and populate cityMap and tempRoads
//        readRoads("src/cpt204_cw3/resources/roads.csv", cityMap, tempRoads);
//
//        // Create a list of vertices (all City objects)
//        List<City> vertices = new ArrayList<>(cityMap.values());
//        // Create a mapping from city names to their indices
//        Map<String, Integer> cityNameToIndex = new HashMap<>();
//        for (int i = 0; i < vertices.size(); i++) {
//            cityNameToIndex.put(vertices.get(i).getCityName(), i);
//        }
//
//        // Create a list of edges
//        List<WeightedEdge> edges = new ArrayList<>();
//        for (Road road : tempRoads) {
//            int u = cityNameToIndex.get(road.cityA);
//            int v = cityNameToIndex.get(road.cityB);
//            edges.add(new WeightedEdge(u, v, road.distance));
//        }
//
//        // Construct the weighted undirected graph
//        WeightedGraph<City> graph = new WeightedGraph<>(vertices, edges);
//
//        // Example: print the edge information of the graph
//        graph.printGraphDetails();
//    }
}