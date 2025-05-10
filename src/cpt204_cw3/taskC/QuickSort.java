package cpt204_cw3.taskC;

public class QuickSort implements SortAlgorithm {
    @Override
    public void sort(String[] array) {
        quickSort(array, 0, array.length - 1);
    }

    private void quickSort(String[] array, int first, int last) {
        if (last > first) {
            int pivotIndex = partition(array, first, last);
            quickSort(array, first, pivotIndex - 1);
            quickSort(array, pivotIndex + 1, last);
        }
    }

    private int partition(String[] array, int first, int last) {
        String pivot = array[first];
        int low = first + 1;
        int high = last;

        while (high > low) {
            while (low <= high && array[low].compareTo(pivot) <= 0)
                low++;

            while (low <= high && array[high].compareTo(pivot) > 0)
                high--;

            if (high > low) {
                swap(array, high, low);
            }
        }

        while (high > first && array[high].compareTo(pivot) >= 0)
            high--;

        if (pivot.compareTo(array[high]) > 0) {
            array[first] = array[high];
            array[high] = pivot;
            return high;
        } else {
            return first;
        }
    }

    private void swap(String[] array, int i, int j) {
        String temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
