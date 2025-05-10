package cpt204_cw3.taskC;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InsertionSortTest {

    private final InsertionSort insertionSort = new InsertionSort();

    @Test
    public void testSortWithUnsortedArray() {
        String[] input = {"banana", "apple", "cherry"};
        String[] expected = {"apple", "banana", "cherry"};
        insertionSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void testSortWithEmptyArray() {
        String[] input = {};
        String[] expected = {};
        insertionSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void testSortWithSingleElement() {
        String[] input = {"apple"};
        String[] expected = {"apple"};
        insertionSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void testSortWithAlreadySortedArray() {
        String[] input = {"apple", "banana", "cherry"};
        String[] expected = {"apple", "banana", "cherry"};
        insertionSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void testSortWithReverseSortedArray() {
        String[] input = {"cherry", "banana", "apple"};
        String[] expected = {"apple", "banana", "cherry"};
        insertionSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void testSortWithDuplicateElements() {
        String[] input = {"apple", "cherry", "banana", "apple"};
        String[] expected = {"apple", "apple", "banana", "cherry"};
        insertionSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void testSortWithMixedCase() {
        String[] input = {"Apple", "apple", "Banana", "banana"};
        String[] expected = {"Apple", "Banana", "apple", "banana"};
        insertionSort.sort(input);
        assertArrayEquals(expected, input);
    }
}