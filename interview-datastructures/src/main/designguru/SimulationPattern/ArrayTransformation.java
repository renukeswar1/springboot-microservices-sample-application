package designguru.SimulationPattern;

import java.util.Arrays;

public class ArrayTransformation {
    public int[] transformArray(int[] arr) {

        boolean changed = true;

        // Continue transforming until no changes occur
        while (changed) {
            changed = false;
            // Store the first three elements for comparison
            int prev = arr[0], curr = arr[1], next = arr[2];

            // Loop through the array starting from the second element to the second last element
            for (int i = 1; i < arr.length - 1; i++) {
                // If current element is less than both its neighbors, increment it
                if (curr < prev && curr < next) {
                    arr[i]++;
                    changed = true;
                }
                // If current element is greater than both its neighbors, decrement it
                else if (curr > prev && curr > next) {
                    arr[i]--;
                    changed = true;
                }
                // Move to the next set of elements
                if (i == arr.length - 2) break;
                prev = curr;
                curr = next;
                next = arr[i + 2];
            }
        }
        return arr;
    }

    public static void main(String[] args) {
        ArrayTransformation sol = new ArrayTransformation();

        // Test the method with different inputs
        int[] arr1 = {5, 2, 8, 1, 6};
        int[] arr2 = {3, 7, 1, 5, 2};
        int[] arr3 = {9, 9, 9, 9, 9};

        System.out.println(Arrays.toString(sol.transformArray(arr1)));  // [5, 5, 5, 5, 6]
        System.out.println(Arrays.toString(sol.transformArray(arr2)));  // [3, 3, 3, 3, 2]
        System.out.println(Arrays.toString(sol.transformArray(arr3)));  // [9, 9, 9, 9, 9]
    }
}
