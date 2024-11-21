package designguru.LinearSortingAlgorithms;

public class CountingSortAlgorithm {
    // Function to perform Counting Sort
    public void countingSort(int[] array) {
        // Find the maximum value in the array
        int max = findMax(array);

        // Create a count array to store the count of each unique element
        int[] countArray = new int[max + 1];

        // Initialize count array with all zeros
        for (int i = 0; i < countArray.length; i++) {
            countArray[i] = 0;
        }

        // Store the count of each element in the count array
        for (int i = 0; i < array.length; i++) {
            countArray[array[i]]++;
        }

        // Modify the count array by adding the previous counts
        for (int i = 1; i < countArray.length; i++) {
            countArray[i] += countArray[i - 1];
        }

        // Create an output array to store the sorted elements
        int[] outputArray = new int[array.length];

        // Build the output array using the count array
        for (int i = array.length - 1; i >= 0; i--) {
            outputArray[countArray[array[i]] - 1] = array[i];
            countArray[array[i]]--;
        }

        // Copy the sorted elements back into the original array
        for (int i = 0; i < array.length; i++) {
            array[i] = outputArray[i];
        }
    }

    // Helper function to find the maximum value in the array
    private static int findMax(int[] array) {
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    // Main method to test Counting Sort with 3 examples
    public static void main(String[] args) {
        // Example 1
        int[] array1 = {4, 2, 2, 8, 3, 3, 1};
        CountingSortAlgorithm sol = new CountingSortAlgorithm();
        sol.countingSort(array1);
        System.out.println("Sorted array 1: " + java.util.Arrays.toString(array1));

        // Example 2
        int[] array2 = {10, 7, 8, 9, 1, 5};
        sol.countingSort(array2);
        System.out.println("Sorted array 2: " + java.util.Arrays.toString(array2));

        // Example 3
        int[] array3 = {3, 6, 4, 2, 1, 7, 5, 8};
        sol.countingSort(array3);
        System.out.println("Sorted array 3: " + java.util.Arrays.toString(array3));
    }
}
