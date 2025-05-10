package cpt204_cw3.taskC;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MergeSortTest {

    private final MergeSort mergeSort = new MergeSort();

    @Test
    void testSortWithUnsortedArray() {
        String[] input = {"pear", "apple", "orange", "grape"};
        String[] expected = {"apple", "grape", "orange", "pear"};
        mergeSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    void testSortWithEmptyArray() {
        String[] input = {};
        String[] expected = {};
        mergeSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    void testSortWithSingleElement() {
        String[] input = {"mango"};
        String[] expected = {"mango"};
        mergeSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    void testSortWithAlreadySortedArray() {
        String[] input = {"apple", "banana", "mango", "peach"};
        String[] expected = {"apple", "banana", "mango", "peach"};
        mergeSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    void testSortWithReverseSortedArray() {
        String[] input = {"zebra", "peach", "banana", "apple"};
        String[] expected = {"apple", "banana", "peach", "zebra"};
        mergeSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    void testSortWithDuplicateElements() {
        String[] input = {"apple", "cherry", "apple", "banana"};
        String[] expected = {"apple", "apple", "banana", "cherry"};
        mergeSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    void testSortWithOddLengthArray() {
        String[] input = {"cat", "apple", "dog", "banana", "ant"};
        String[] expected = {"ant", "apple", "banana", "cat", "dog"};
        mergeSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    void testSortWithMixedCase() {
        String[] input = {"Apple", "apple", "Banana", "banana"};
        String[] expected = {"Apple", "Banana", "apple", "banana"};
        mergeSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    void testSortWithLargeArray() {
        String[] input = {"m", "k", "v", "a", "d", "s", "p", "z"};
        String[] expected = {"a", "d", "k", "m", "p", "s", "v", "z"};
        mergeSort.sort(input);
        assertArrayEquals(expected, input);
    }
}