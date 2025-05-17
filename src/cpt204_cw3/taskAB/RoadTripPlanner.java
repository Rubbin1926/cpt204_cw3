package cpt204_cw3.taskAB;

import java.util.*;

/**
 * Core class for planning optimal road trips through US cities.
 * Implements Dijkstra's algorithm for shortest path and permutations for attraction ordering.
 */
public class RoadTripPlanner {

    private WeightedGraph<City> graph;  // Graph representing cities and roads

    public RoadTripPlanner(WeightedGraph<City> graph) {
        this.graph = graph;
    }

    /**
     * Finds optimal route visiting specified attractions between start and end cities.
     *
     * @param startingCity  Name of origin city
     * @param endingCity    Name of destination city
     * @param attractions   List of required points of interest
     * @return List of cities representing optimal route
     * @throws IllegalArgumentException if input cities/attractions are invalid
     */
    public List<City> route(String startingCity, String endingCity, List<String> attractions) {
        // === Step 1: Validate Input Cities ===
        int startIndex = findCityIndex(startingCity);
        int endIndex = findCityIndex(endingCity);

        if (startIndex == -1) {
            throw new IllegalArgumentException("Starting city not found: " + startingCity);
        }
        if (endIndex == -1) {
            throw new IllegalArgumentException("Ending city not found: " + endingCity);
        }

        // === Step 2: Process Attractions ===
        List<Integer> attractionIndices = new ArrayList<>();
        for (String attraction : attractions) {
            // Skip empty inputs (e.g., from empty text fields)
            if (attraction == null || attraction.trim().isEmpty()) {
                continue;
            }

            int index = findAttractionIndex(attraction.trim());
            if (index == -1) {
                throw new IllegalArgumentException("Attraction not found: " + attraction);
            }
            attractionIndices.add(index);
        }

        // === Case: No Attractions Required ===
        if (attractionIndices.isEmpty()) {
            return getShortestPath(startIndex, endIndex);
        }

        // === Step 3: Precompute All-Pairs Shortest Paths ===
        int n = graph.getSize();
        double[][] distances = new double[n][n];     // Stores shortest distances between all pairs
        int[][] predecessors = new int[n][n];        // For path reconstruction

        // Run Dijkstra's algorithm from every node
        for (int i = 0; i < n; i++) {
            ShortestPathResult result = dijkstra(i);
            System.arraycopy(result.distances, 0, distances[i], 0, n);
            System.arraycopy(result.predecessors, 0, predecessors[i], 0, n);
        }

        // === Step 4: Find Optimal Attraction Order ===
        List<List<Integer>> permutations = generatePermutations(attractionIndices);
        double minDistance = Double.POSITIVE_INFINITY;
        List<Integer> bestPermutation = null;

        // Evaluate all possible attraction orders
        for (List<Integer> perm : permutations) {
            double currentDistance = calculateTotalDistance(startIndex, endIndex, perm, distances);
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                bestPermutation = perm;
            }
        }

        // === Step 5: Construct Full Path ===
        if (bestPermutation == null) {
            return Collections.emptyList();  // No valid path found
        }
        return constructFullPath(startIndex, endIndex, bestPermutation, predecessors);
    }

    /**
     * Finds graph index for a city by name
     * @param cityName City name to search
     * @return Vertex index or -1 if not found
     */
    public int findCityIndex(String cityName) {
        for (int i = 0; i < graph.getSize(); i++) {
            City city = graph.getVertex(i);
            if (city.getCityName().equals(cityName)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds graph index for a city containing an attraction
     * @param attraction Name of point of interest
     * @return Vertex index or -1 if not found
     */
    public int findAttractionIndex(String attraction) {
        for (int i = 0; i < graph.getSize(); i++) {
            City city = graph.getVertex(i);
            if (attraction.equals(city.getInterest())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets direct shortest path between two cities (no attractions)
     */
    private List<City> getShortestPath(int start, int end) {
        ShortestPathResult result = dijkstra(start);
        List<Integer> pathIndices = getPath(start, end, result.predecessors);
        return convertIndicesToCities(pathIndices);
    }

    /**
     * Calculates total distance for a specific attraction order
     * @param start       Origin city index
     * @param end         Destination city index
     * @param permutation Attraction visitation order
     * @param distances   Precomputed all-pairs distances
     * @return Total distance or infinity if path is invalid
     */
    private double calculateTotalDistance(int start, int end, List<Integer> permutation, double[][] distances) {
        if (permutation.isEmpty()) return Double.POSITIVE_INFINITY;

        // Sum distances: start -> attractions -> end
        double total = distances[start][permutation.get(0)];
        if (total == Double.POSITIVE_INFINITY) return Double.POSITIVE_INFINITY;

        for (int i = 0; i < permutation.size() - 1; i++) {
            double d = distances[permutation.get(i)][permutation.get(i + 1)];
            if (d == Double.POSITIVE_INFINITY) return Double.POSITIVE_INFINITY;
            total += d;
        }

        double lastSegment = distances[permutation.getLast()][end];
        if (lastSegment == Double.POSITIVE_INFINITY) return Double.POSITIVE_INFINITY;
        return total + lastSegment;
    }

    /**
     * Builds complete path from path segments
     */
    private List<City> constructFullPath(int start, int end, List<Integer> permutation, int[][] predecessors) {
        List<Integer> fullPath = new ArrayList<>();

        // Start to first attraction
        List<Integer> startToFirst = getPath(start, permutation.get(0), predecessors[start]);
        fullPath.addAll(startToFirst);

        // Between attractions
        for (int i = 0; i < permutation.size() - 1; i++) {
            int from = permutation.get(i);
            int to = permutation.get(i + 1);
            List<Integer> segment = getPath(from, to, predecessors[from]);
            if (segment.isEmpty()) return Collections.emptyList();
            fullPath.addAll(segment.subList(1, segment.size()));  // Avoid duplicate nodes
        }

        // Last attraction to end
        List<Integer> lastToEnd = getPath(permutation.getLast(), end, predecessors[permutation.getLast()]);
        if (lastToEnd.isEmpty()) return Collections.emptyList();
        fullPath.addAll(lastToEnd.subList(1, lastToEnd.size()));

        return convertIndicesToCities(fullPath);
    }

    /**
     * Reconstructs path from predecessor array
     */
    private List<Integer> getPath(int start, int end, int[] predecessors) {
        List<Integer> path = new ArrayList<>();
        Integer current = end;

        // Backtrack from end to start
        while (current != -1) {
            path.add(current);
            current = predecessors[current];
        }
        Collections.reverse(path);

        // Validate path continuity
        if (path.isEmpty() || path.get(0) != start) {
            return Collections.emptyList();
        }
        return path;
    }

    /**
     * Converts vertex indices to City objects
     */
    private List<City> convertIndicesToCities(List<Integer> indices) {
        List<City> cities = new ArrayList<>();
        for (int index : indices) {
            cities.add(graph.getVertex(index));
        }
        return cities;
    }

    /**
     * Dijkstra's algorithm implementation for shortest paths
     * @param source Starting vertex index
     * @return Shortest path results (distances and predecessors)
     */
    private ShortestPathResult dijkstra(int source) {
        int n = graph.getSize();
        double[] distances = new double[n];
        int[] predecessors = new int[n];
        Arrays.fill(distances, Double.POSITIVE_INFINITY);
        Arrays.fill(predecessors, -1);
        distances[source] = 0.0;

        PriorityQueue<VertexDistance> pq = new PriorityQueue<>();
        pq.add(new VertexDistance(source, 0.0));

        boolean[] visited = new boolean[n];

        while (!pq.isEmpty()) {
            VertexDistance current = pq.poll();
            int u = current.vertex;
            if (visited[u]) continue;
            visited[u] = true;

            // Explore all neighbors
            for (Edge edge : graph.neighbors.get(u)) {
                if (!(edge instanceof WeightedEdge)) continue;
                WeightedEdge we = (WeightedEdge) edge;
                int v = we.getV();
                double weight = we.getWeight();

                // Update distance if shorter path found
                if (!visited[v] && distances[v] > distances[u] + weight) {
                    distances[v] = distances[u] + weight;
                    predecessors[v] = u;
                    pq.add(new VertexDistance(v, distances[v]));
                }
            }
        }

        return new ShortestPathResult(distances, predecessors);
    }

    // Helper class for priority queue
    private static class VertexDistance implements Comparable<VertexDistance> {
        int vertex;
        double distance;

        VertexDistance(int vertex, double distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        @Override
        public int compareTo(VertexDistance other) {
            return Double.compare(this.distance, other.distance);
        }
    }

    // Container for Dijkstra's results
    private static class ShortestPathResult {
        double[] distances;    // Shortest distances from source
        int[] predecessors;    // Path reconstruction pointers

        ShortestPathResult(double[] distances, int[] predecessors) {
            this.distances = distances;
            this.predecessors = predecessors;
        }
    }

    /**
     * Generates all permutations of attractions (exhaustive search)
     */
    private static <T> List<List<T>> generatePermutations(List<T> list) {
        List<List<T>> result = new ArrayList<>();
        backtrack(result, new ArrayList<>(), list);
        return result;
    }

    /**
     * Recursive backtracking for permutation generation
     */
    private static <T> void backtrack(List<List<T>> result, List<T> tempList, List<T> elements) {
        if (tempList.size() == elements.size()) {
            result.add(new ArrayList<>(tempList));
        } else {
            for (int i = 0; i < elements.size(); i++) {
                if (tempList.contains(elements.get(i))) continue;  // Skip already added
                tempList.add(elements.get(i));
                backtrack(result, tempList, elements);
                tempList.remove(tempList.size() - 1);
            }
        }
    }
}