package cpt204_cw3;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        //Test the weightedGraph class
        City city0 = new City("Seattle");
        City city1 = new City("San Francisco");
        City city2 = new City("Los Angeles");

        ArrayList<City> cities = new ArrayList<>();
        cities.add(city0);
        cities.add(city1);
        cities.add(city2);

        WeightedEdge edge1 = new WeightedEdge(0, 1, 1.5);
        WeightedEdge edge2 = new WeightedEdge(1, 2, 2.5);
        ArrayList<WeightedEdge> edges = new ArrayList<>();
        edges.add(edge1);
        edges.add(edge2);

        WeightedGraph<City> graph = new WeightedGraph<>(cities, edges);

        System.out.println(graph.getVertex(0).getCityName());
        System.out.println(graph.addWeightedEdges(edges));
        try {
            graph.printWeightedEdges();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}