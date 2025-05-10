package cpt204_cw3.taskC;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuickSortTest {

    private final QuickSort quickSort = new QuickSort();

    @Test
    public void testSortWithUnsortedArray() {
        String[] input = {"pear", "apple", "orange", "grape"};
        String[] expected = {"apple", "grape", "orange", "pear"};
        quickSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void testSortWithEmptyArray() {
        String[] input = {};
        String[] expected = {};
        quickSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void testSortWithSingleElement() {
        String[] input = {"mango"};
        String[] expected = {"mango"};
        quickSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void testSortWithAlreadySortedArray() {
        String[] input = {"apple", "banana", "mango", "peach"};
        String[] expected = {"apple", "banana", "mango", "peach"};
        quickSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void testSortWithReverseSortedArray() {
        String[] input = {"zebra", "peach", "banana", "apple"};
        String[] expected = {"apple", "banana", "peach", "zebra"};
        quickSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void testSortWithDuplicateElements() {
        String[] input = {"apple", "cherry", "apple", "banana"};
        String[] expected = {"apple", "apple", "banana", "cherry"};
        quickSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void testSortWithMixedCase() {
        String[] input = {"Apple", "banana", "apple", "Banana"};
        String[] expected = {"Apple", "Banana", "apple", "banana"};
        quickSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void testSortWithLargeArray() {
        String[] input = {"m", "k", "v", "a", "d", "s", "p", "z"};
        String[] expected = {"a", "d", "k", "m", "p", "s", "v", "z"};
        quickSort.sort(input);
        assertArrayEquals(expected, input);
    }
}