package cpt204_cw3.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Map to store city names to City objects
        Map<String, City> cityMap = new HashMap<>();
        // Temporarily store road information
        List<CSVGraphBuilder.Road> tempRoads = new ArrayList<>();

        // Read attractions.csv and populate cityMap
        CSVGraphBuilder.readAttractions("src/cpt204_cw3/resources/attractions.csv", cityMap);

        // Read roads.csv and populate cityMap and tempRoads
        CSVGraphBuilder.readRoads("src/cpt204_cw3/resources/roads.csv", cityMap, tempRoads);

        // Create a list of vertices (all City objects)
        List<City> vertices = new ArrayList<>(cityMap.values());
        // Create a mapping from city names to their indices
        Map<String, Integer> cityNameToIndex = new HashMap<>();
        for (int i = 0; i < vertices.size(); i++) {
            cityNameToIndex.put(vertices.get(i).getCityName(), i);
        }

        // Create a list of edges
        List<WeightedEdge> edges = new ArrayList<>();
        for (CSVGraphBuilder.Road road : tempRoads) {
            int u = cityNameToIndex.get(road.cityA);
            int v = cityNameToIndex.get(road.cityB);
            edges.add(new WeightedEdge(u, v, road.distance));
        }

        // Construct the weighted undirected graph
        WeightedGraph<City> graph = new WeightedGraph<>(vertices, edges);

        // Example: print the edge information of the graph
        graph.printGraphDetails();
    }
}