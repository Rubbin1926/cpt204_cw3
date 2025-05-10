package cpt204_cw3.taskC;

import java.util.Arrays;

public class MergeSort implements SortAlgorithm {
    @Override
    public void sort(String[] array) {
        mergeSort(array, 0, array.length - 1);
    }

    private void mergeSort(String[] array, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(array, left, mid);
            mergeSort(array, mid + 1, right);
            merge(array, left, mid, right);
        }
    }

    private void merge(String[] array, int left, int mid, int right) {
        String[] leftArray = Arrays.copyOfRange(array, left, mid + 1);
        String[] rightArray = Arrays.copyOfRange(array, mid + 1, right + 1);

        int i = 0, j = 0, k = left;
        while (i < leftArray.length && j < rightArray.length) {
            if (leftArray[i].compareTo(rightArray[j]) <= 0) {
                array[k++] = leftArray[i++];
            } else {
                array[k++] = rightArray[j++];
            }
        }

        while (i < leftArray.length) {
            array[k++] = leftArray[i++];
        }

        while (j < rightArray.length) {
            array[k++] = rightArray[j++];
        }
    }
}
