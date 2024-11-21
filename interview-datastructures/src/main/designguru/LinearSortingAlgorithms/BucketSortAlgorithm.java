package designguru.LinearSortingAlgorithms;

import java.util.ArrayList;
import java.util.Collections;

public class BucketSortAlgorithm {

    // Function to perform Bucket Sort
    public void bucketSort(int[] array) {
        if (array.length <= 0) {
            return;
        }

        // Step 1: Create buckets
        int numBuckets = (int) Math.sqrt(array.length); // Number of buckets
        ArrayList<Integer>[] buckets = new ArrayList[numBuckets];

        // Initialize buckets
        for (int i = 0; i < numBuckets; i++) {
            buckets[i] = new ArrayList<>();
        }

        // Step 2: Distribute elements into buckets
        int maxVal = getMax(array);
        for (int num : array) {
            int bucketIndex = (num * numBuckets) / (maxVal + 1);
            buckets[bucketIndex].add(num);
        }

        // Step 3: Sort each bucket
        for (ArrayList<Integer> bucket : buckets) {
            Collections.sort(bucket);
        }

        // Step 4: Concatenate buckets
        int index = 0;
        for (ArrayList<Integer> bucket : buckets) {
            for (int num : bucket) {
                array[index++] = num;
            }
        }
    }

    // Helper function to find the maximum value in the array
    private int getMax(int[] array) {
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    // Main method to test Bucket Sort with 3 examples
    public static void main(String[] args) {
        BucketSortAlgorithm sol = new BucketSortAlgorithm();

        // Example 1
        int[] array1 = {29, 25, 3, 49, 9, 37, 21, 43, 45};
        sol.bucketSort(array1);
        System.out.println("Sorted array 1: " + java.util.Arrays.toString(array1));

        // Example 2
        int[] array2 = {78, 17, 39, 26, 72, 94, 21, 12, 23, 68};
        sol.bucketSort(array2);
        System.out.println("Sorted array 2: " + java.util.Arrays.toString(array2));

        // Example 3
        int[] array3 = {4, 3, 2, 1, 0, 9, 8, 7, 6, 5};
        sol.bucketSort(array3);
        System.out.println("Sorted array 3: " + java.util.Arrays.toString(array3));
    }
}
