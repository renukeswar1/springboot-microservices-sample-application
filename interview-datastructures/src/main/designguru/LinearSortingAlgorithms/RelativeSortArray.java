package designguru.LinearSortingAlgorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RelativeSortArray {

    public int[] relativeSortArray(int[] arr1, int[] arr2) {
        // Determine the largest value in arr1 to size the count array correctly
        int maxElement = Arrays.stream(arr1).max().orElse(0);
        // Create a count array to keep track of occurrences
        int[] count = new int[maxElement + 1];

        // Populate the count array with the frequency of each element in arr1
        for (int element : arr1) {
            count[element]++;
        }

        List<Integer> result = new ArrayList<>();
        // Append elements from arr2 to the result array, maintaining the given order
        for (int value : arr2) {
            while (count[value] > 0) {
                result.add(value);
                count[value]--;
            }
        }

        // Add the remaining elements that are not in arr2 to the result array in ascending order
        for (int num = 0; num <= maxElement; num++) {
            while (count[num] > 0) {
                result.add(num);
                count[num]--;
            }
        }

        // Convert the result ArrayList back to an array and return it
        return result.stream().mapToInt(Integer::intValue).toArray();
    }

    public static void main(String[] args) {
        RelativeSortArray solution = new RelativeSortArray();

        // Test case 1
        int[] arr1_1 = {3, 5, 2, 1, 6, 4, 5, 6};
        int[] arr2_1 = {5, 6, 4, 1};
        System.out.println(Arrays.toString(solution.relativeSortArray(arr1_1, arr2_1))); // Expected Output: [5, 5, 6, 6, 4, 1, 2, 3]

        // Test case 2
        int[] arr1_2 = {8, 3, 9, 1, 7, 5};
        int[] arr2_2 = {3, 9, 8};
        System.out.println(Arrays.toString(solution.relativeSortArray(arr1_2, arr2_2))); // Expected Output: [3, 9, 8, 1, 5, 7]

        // Test case 3
        int[] arr1_3 = {10, 10, 7, 10, 7, 9};
        int[] arr2_3 = {10, 7};
        System.out.println(Arrays.toString(solution.relativeSortArray(arr1_3, arr2_3))); // Expected Output: [10, 10, 10, 7, 7, 9]
    }
}
