package designguru.LinearSortingAlgorithms;

import java.util.Arrays;

public class RadixSortAlgorithm {
    // Function to get the maximum value in the array
    private static int getMax(int[] array) {
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    // Function to perform Counting Sort on the array based on the digit represented by exp
    private static void countingSort(int[] array, int exp) {
        int n = array.length;
        int[] output = new int[n]; // Output array to store sorted numbers
        int[] count = new int[10]; // Count array to store the count of digits (0-9)

        // Initialize count array with all zeros
        Arrays.fill(count, 0);

        // Store count of occurrences of each digit in count[]
        for (int i = 0; i < n; i++) {
            int digit = (array[i] / exp) % 10;
            count[digit]++;
        }

        // Update count array to contain the actual position of each digit in the output[]
        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        // Build the output array by placing the numbers in their correct positions
        // Traverse the input array in reverse order to maintain stable sorting
        for (int i = n - 1; i >= 0; i--) {
            int digit = (array[i] / exp) % 10;
            // Place the number in the output array at the index of its position
            output[count[digit] - 1] = array[i];
            // Decrement the count of the current digit
            count[digit]--;
        }

        // Copy the sorted numbers back into the original array
        for (int i = 0; i < n; i++) {
            array[i] = output[i];
        }
    }

    // Function to perform Radix Sort
    public void radixSort(int[] array) {
        // Find the maximum number to determine the number of digits
        int max = getMax(array);

        // Perform counting sort for every digit. Note that exp is 10^i where i is the current digit number
        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingSort(array, exp);
        }
    }

    // Main method to test Radix Sort with 3 examples
    public static void main(String[] args) {
        // Example 1
        int[] array1 = {180, 55, 85, 90, 903, 243, 2, 6};
        RadixSortAlgorithm sol = new RadixSortAlgorithm();
        sol.radixSort(array1);
        System.out.println("Sorted array 1: " + Arrays.toString(array1));

        // Example 2
        int[] array2 = {123, 456, 789, 234, 567, 890, 345, 678};
        sol.radixSort(array2);
        System.out.println("Sorted array 2: " + Arrays.toString(array2));

        // Example 3
        int[] array3 = {9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
        sol.radixSort(array3);
        System.out.println("Sorted array 3: " + Arrays.toString(array3));
    }
}
