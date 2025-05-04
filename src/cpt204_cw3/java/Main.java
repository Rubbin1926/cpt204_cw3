package cpt204_cw3.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            WeightedGraph<City> graph = CSVGraphBuilder.buildGraphFromCSV(
                    "src/cpt204_cw3/resources/attractions.csv",
                    "src/cpt204_cw3/resources/roads.csv"
            );

            // Use the graph instance
            graph.printWeightedEdges();

        } catch (IOException e) {
            System.err.println("File error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format: " + e.getMessage());
        }
    }
}