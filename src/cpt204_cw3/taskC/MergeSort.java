package cpt204_cw3.taskC;

import java.util.Arrays;

public class MergeSort implements SortAlgorithm {
    @Override
    public void sort(String[] array) {
        if (array.length > 1) {
            String[] firstHalf = Arrays.copyOfRange(array, 0, array.length / 2);
            String[] secondHalf = Arrays.copyOfRange(array, array.length / 2, array.length);

            sort(firstHalf);
            sort(secondHalf);

            merge(firstHalf, secondHalf, array);
        }
    }

    private void merge(String[] list1, String[] list2, String[] temp) {
        int current1 = 0;
        int current2 = 0;
        int current3 = 0;

        while (current1 < list1.length && current2 < list2.length) {
            if (list1[current1].compareTo(list2[current2]) < 0) {
                temp[current3++] = list1[current1++];
            } else {
                temp[current3++] = list2[current2++];
            }
        }

        while (current1 < list1.length)
            temp[current3++] = list1[current1++];

        while (current2 < list2.length)
            temp[current3++] = list2[current2++];
    }
}
