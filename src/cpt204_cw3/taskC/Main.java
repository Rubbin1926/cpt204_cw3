package cpt204_cw3.taskC;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String[] datasets = {
                "src/cpt204_cw3/resources/1000places_sorted.csv",
                "src/cpt204_cw3/resources/1000places_random.csv",
                "src/cpt204_cw3/resources/10000places_sorted.csv",
                "src/cpt204_cw3/resources/10000places_random.csv"
        };

        SortAlgorithm[] algorithms = {
                new InsertionSort(),
                new QuickSort(),
                new MergeSort()
        };

        Map<String, long[]> results = new LinkedHashMap<>();

        for (String dataset : datasets) {
            String[] data = FileUtils.readNamesFromFile(dataset);
            if (data.length == 0) {
                System.err.println("Error reading dataset: " + dataset);
                continue;
            }
            long[] times = new long[algorithms.length];
            for (int i = 0; i < algorithms.length; i++) {
                times[i] = PerformanceTester.testSort(algorithms[i], data);
            }
            results.put(dataset, times);
        }

        printResults(results);
    }

    private static void printResults(Map<String, long[]> results) {
        System.out.println("Dataset\t\t\t\t\t\t\t\t\t\t\tInsertion Sort (ns)\tQuick Sort (ns)\tMerge Sort (ns)");
        results.forEach((dataset, times) -> {
            System.out.printf("%-24s", dataset);
            for (long time : times) {
                if (time == -1) {
                    System.out.print("\tStackOverflow");
                } else if (time == -2) {
                    System.out.print("\tSortError");
                } else {
                    System.out.printf("\t%,15d", time);
                }
            }
            System.out.println();
        });
    }
}
