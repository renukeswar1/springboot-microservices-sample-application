package designguru.IntroductiotoMOAlgorithm;

import java.util.Arrays;

public class RangeFrequencyQueriesMedium {
    private int[] nums;
    private int[] frequency;

    public int[] rangeFrequencyQueries(int[] nums, int[][] queries) {
        this.nums = nums;
        int n = nums.length;
        frequency = new int[100001]; // assuming nums[i] is <= 100000
        Query[] queryObjects = new Query[queries.length];

        // Prepare the queries for Mo's algorithm
        for (int i = 0; i < queries.length; i++) {
            queryObjects[i] = new Query(queries[i][0], queries[i][1], queries[i][2], i);
        }

        // Sort the queries
        Arrays.sort(queryObjects, (q1, q2) -> {
            int block1 = q1.left / (int) Math.sqrt(n);
            int block2 = q2.left / (int) Math.sqrt(n);
            if (block1 != block2) {
                return block1 - block2;
            }
            return q1.right - q2.right;
        });

        int[] result = new int[queries.length];
        int currentLeft = 0, currentRight = 0;
        for (Query query : queryObjects) {
            // Adjust right boundary
            while (currentRight <= query.right) frequency[nums[currentRight++]]++;
            while (currentRight > query.right + 1) frequency[nums[--currentRight]]--;

            // Adjust left boundary
            while (currentLeft < query.left) frequency[nums[currentLeft++]]--;
            while (currentLeft > query.left) frequency[nums[--currentLeft]]++;

            // Store the result for this query
            result[query.index] = frequency[query.value];
        }

        return result;
    }

    private class Query {
        int left, right, value, index;
        Query(int left, int right, int value, int index) {
            this.left = left;
            this.right = right;
            this.value = value;
            this.index = index;
        }
    }

    // Main method for testing the examples
    public static void main(String[] args) {
        RangeFrequencyQueriesMedium solution = new RangeFrequencyQueriesMedium();

        // Example 1
        int[] nums1 = {2, 1, 2, 3, 2, 1, 3};
        int[][] queries1 = {{0, 4, 2}, {2, 6, 3}, {1, 5, 1}};
        int[] result1 = solution.rangeFrequencyQueries(nums1, queries1);
        System.out.println(Arrays.toString(result1)); // Output: [3, 2, 2]

        // Example 2
        int[] nums2 = {4, 4, 4, 2, 4, 2, 2};
        int[][] queries2 = {{0, 2, 4}, {3, 6, 2}, {1, 4, 4}};
        int[] result2 = solution.rangeFrequencyQueries(nums2, queries2);
        System.out.println(Arrays.toString(result2)); // Output: [3, 3, 3]

        // Example 3
        int[] nums3 = {5, 3, 5, 5, 3, 3, 5, 3, 5};
        int[][] queries3 = {{0, 8, 5}, {1, 5, 3}, {2, 7, 5}};
        int[] result3 = solution.rangeFrequencyQueries(nums3, queries3);
        System.out.println(Arrays.toString(result3)); // Output: [5, 3, 3]
    }
}
