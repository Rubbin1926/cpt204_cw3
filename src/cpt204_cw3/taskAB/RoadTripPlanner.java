package cpt204_cw3.taskAB;

import java.util.*;

public class RoadTripPlanner {

    private WeightedGraph<City> graph;

    public RoadTripPlanner(WeightedGraph<City> graph) {
        this.graph = graph;
    }

    public List<City> route(String startingCity, String endingCity, List<String> attractions) {
        // Step 1: Validate starting and ending cities
        int startIndex = findCityIndex(startingCity);
        int endIndex = findCityIndex(endingCity);

        if (startIndex == -1) {
            throw new IllegalArgumentException("Starting city not found: " + startingCity);
        }
        if (endIndex == -1) {
            throw new IllegalArgumentException("Ending city not found: " + endingCity);
        }

        // Step 2: Convert attractions to city indices
        List<Integer> attractionIndices = new ArrayList<>();
        for (String attraction : attractions) {
            // Skip empty or blank attractions from input
            if (attraction == null || attraction.trim().isEmpty()) {
                continue; // Handle empty strings from comma-only inputs
            }

            int index = findAttractionIndex(attraction.trim());
            if (index == -1) {
                throw new IllegalArgumentException("Attraction not found: " + attraction);
            }
            attractionIndices.add(index);
        }

        // Handle case with no valid attractions (including empty input)
        if (attractionIndices.isEmpty()) {
            return getShortestPath(startIndex, endIndex);
        }

        // Step 3: Precompute the shortest paths between all nodes
        int n = graph.getSize();
        double[][] distances = new double[n][n];
        int[][] predecessors = new int[n][n];
        for (int i = 0; i < n; i++) {
            ShortestPathResult result = dijkstra(i);
            System.arraycopy(result.distances, 0, distances[i], 0, n);
            System.arraycopy(result.predecessors, 0, predecessors[i], 0, n);
        }

        // Step 4: Generate permutations and find the minimal path
        List<List<Integer>> permutations = generatePermutations(attractionIndices);
        double minDistance = Double.POSITIVE_INFINITY;
        List<Integer> bestPermutation = null;

        for (List<Integer> perm : permutations) {
            double currentDistance = calculateTotalDistance(startIndex, endIndex, perm, distances);
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                bestPermutation = perm;
            }
        }

        if (bestPermutation == null) {
            return Collections.emptyList(); // No valid path found
        }

        // Step 5: Build the complete path
        return constructFullPath(startIndex, endIndex, bestPermutation, predecessors);
    }

    private int findCityIndex(String cityName) {
        for (int i = 0; i < graph.getSize(); i++) {
            City city = graph.getVertex(i);
            if (city.getCityName().equals(cityName)) {
                return i;
            }
        }
        return -1;
    }

    private int findAttractionIndex(String attraction) {
        for (int i = 0; i < graph.getSize(); i++) {
            City city = graph.getVertex(i);
            if (attraction.equals(city.getInterest())) {
                return i;
            }
        }
        return -1;
    }

    private List<City> getShortestPath(int start, int end) {
        ShortestPathResult result = dijkstra(start);
        List<Integer> pathIndices = getPath(start, end, result.predecessors);
        return convertIndicesToCities(pathIndices);
    }

    private double calculateTotalDistance(int start, int end, List<Integer> permutation, double[][] distances) {
        if (permutation.isEmpty()) return Double.POSITIVE_INFINITY;

        double total = distances[start][permutation.get(0)];
        if (total == Double.POSITIVE_INFINITY) return Double.POSITIVE_INFINITY;

        for (int i = 0; i < permutation.size() - 1; i++) {
            double d = distances[permutation.get(i)][permutation.get(i + 1)];
            if (d == Double.POSITIVE_INFINITY) return Double.POSITIVE_INFINITY;
            total += d;
        }

        double lastSegment = distances[permutation.get(permutation.size() - 1)][end];
        if (lastSegment == Double.POSITIVE_INFINITY) return Double.POSITIVE_INFINITY;
        total += lastSegment;

        return total;
    }

    private List<City> constructFullPath(int start, int end, List<Integer> permutation, int[][] predecessors) {
        List<Integer> fullPath = new ArrayList<>();

        // Add path from start to first attraction
        List<Integer> startToFirst = getPath(start, permutation.get(0), predecessors[start]);
        fullPath.addAll(startToFirst);

        // Add paths between attractions
        for (int i = 0; i < permutation.size() - 1; i++) {
            int from = permutation.get(i);
            int to = permutation.get(i + 1);
            List<Integer> segment = getPath(from, to, predecessors[from]);
            if (segment.isEmpty()) return Collections.emptyList();
            segment = segment.subList(1, segment.size()); // Remove duplicate
            fullPath.addAll(segment);
        }

        // Add path from last attraction to end
        List<Integer> lastToEnd = getPath(permutation.get(permutation.size() - 1), end, predecessors[permutation.get(permutation.size() - 1)]);
        if (lastToEnd.isEmpty()) return Collections.emptyList();
        lastToEnd = lastToEnd.subList(1, lastToEnd.size());
        fullPath.addAll(lastToEnd);

        return convertIndicesToCities(fullPath);
    }

    private List<Integer> getPath(int start, int end, int[] predecessors) {
        List<Integer> path = new ArrayList<>();
        Integer current = end;
        while (current != -1) {
            path.add(current);
            current = predecessors[current];
        }
        Collections.reverse(path);

        if (path.isEmpty() || path.get(0) != start) {
            return Collections.emptyList();
        }
        return path;
    }

    private List<City> convertIndicesToCities(List<Integer> indices) {
        List<City> cities = new ArrayList<>();
        for (int index : indices) {
            cities.add(graph.getVertex(index));
        }
        return cities;
    }

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

            for (Edge edge : graph.neighbors.get(u)) {
                if (!(edge instanceof WeightedEdge)) continue;
                WeightedEdge we = (WeightedEdge) edge;
                int v = we.getV();
                double weight = we.getWeight();

                if (!visited[v] && distances[v] > distances[u] + weight) {
                    distances[v] = distances[u] + weight;
                    predecessors[v] = u;
                    pq.add(new VertexDistance(v, distances[v]));
                }
            }
        }

        return new ShortestPathResult(distances, predecessors);
    }

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

    private static class ShortestPathResult {
        double[] distances;
        int[] predecessors;

        ShortestPathResult(double[] distances, int[] predecessors) {
            this.distances = distances;
            this.predecessors = predecessors;
        }
    }

    private static <T> List<List<T>> generatePermutations(List<T> list) {
        List<List<T>> result = new ArrayList<>();
        backtrack(result, new ArrayList<>(), list);
        return result;
    }

    private static <T> void backtrack(List<List<T>> result, List<T> tempList, List<T> elements) {
        if (tempList.size() == elements.size()) {
            result.add(new ArrayList<>(tempList));
        } else {
            for (int i = 0; i < elements.size(); i++) {
                if (tempList.contains(elements.get(i))) continue; // element already exists
                tempList.add(elements.get(i));
                backtrack(result, tempList, elements);
                tempList.remove(tempList.size() - 1);
            }
        }
    }
}