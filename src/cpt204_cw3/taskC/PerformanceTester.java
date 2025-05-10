package cpt204_cw3.taskC;

import java.util.Arrays;

public class PerformanceTester {
    public static long testSort(SortAlgorithm algorithm, String[] data) {
        String[] copy = Arrays.copyOf(data, data.length);
        try {
            long startTime = System.nanoTime();
            algorithm.sort(copy);
            long endTime = System.nanoTime();
            if (!isSorted(copy)) {
                return -2;
            }
            return endTime - startTime;
        } catch (StackOverflowError e) {
            return -1;
        }
    }

    private static boolean isSorted(String[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i].compareTo(array[i + 1]) > 0) {
                return false;
            }
        }
        return true;
    }
}
