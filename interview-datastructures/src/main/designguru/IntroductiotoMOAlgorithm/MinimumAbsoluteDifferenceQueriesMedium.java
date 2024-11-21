package designguru.IntroductiotoMOAlgorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MinimumAbsoluteDifferenceQueriesMedium {
    static class Query {
        int leftIndex, rightIndex, queryIndex;

        // Constructor to initialize a query
        Query(int leftIndex, int rightIndex, int queryIndex) {
            this.leftIndex = leftIndex;
            this.rightIndex = rightIndex;
            this.queryIndex = queryIndex;
        }
    }

    // Method to adjust the current range [L, R] to match the query's range [li, ri]
    private void adjustRange(int[] leftPointer, int[] rightPointer, Query currentQuery, int[] frequencyCount, int[] nums) {
        // Expanding or contracting the left side of the range
        while (leftPointer[0] > currentQuery.leftIndex) {
            leftPointer[0]--;
            frequencyCount[nums[leftPointer[0]]]++;
        }

        while (leftPointer[0] < currentQuery.leftIndex) {
            frequencyCount[nums[leftPointer[0]]]--;
            leftPointer[0]++;
        }

        // Expanding or contracting the right side of the range
        while (rightPointer[0] < currentQuery.rightIndex) {
            rightPointer[0]++;
            frequencyCount[nums[rightPointer[0]]]++;
        }

        while (rightPointer[0] > currentQuery.rightIndex) {
            frequencyCount[nums[rightPointer[0]]]--;
            rightPointer[0]--;
        }
    }

    // Method to find the minimum absolute difference for each query
    public int[] minDiff(int[] nums, int[][] queries) {
        int q = queries.length; // Number of queries
        List<Query> queryList = new ArrayList<>();

        // Prepare the list of queries with their respective indices
        for (int i = 0; i < q; i++) {
            int[] query = queries[i];
            queryList.add(new Query(query[0], query[1], i));
        }

        // Calculate block size for Mo's algorithm
        int blockSize = (int) Math.sqrt(nums.length);

        // Sort the queries first by block, then by right index within the block
        queryList.sort((a, b) -> {
            if (a.leftIndex / blockSize != b.leftIndex / blockSize)
                return a.leftIndex / blockSize - b.leftIndex / blockSize;
            return a.rightIndex - b.rightIndex;
        });

        // Initialize frequency array to keep track of occurrences in the current range
        int[] frequencyCount = new int[101]; // Assume numbers are in the range 1 to 100
        int[] results = new int[q]; // Array to store results for each query

        // Initialize pointers for the current range
        int[] leftPointer = {queryList.get(0).leftIndex};
        int[] rightPointer = {queryList.get(0).leftIndex};
        frequencyCount[nums[queryList.get(0).leftIndex]]++; // Initialize with the first element

        // Process each query
        for (Query query : queryList) {
            // Adjust the range to match the current query's range
            adjustRange(leftPointer, rightPointer, query, frequencyCount, nums);

            // Calculate the minimum absolute difference in the current range
            int previousNumber = -1; // To store the last number we encountered in the range
            int minDifference = Integer.MAX_VALUE; // To store the minimum difference

            // Traverse through the possible numbers and calculate the minimum difference
            for (int j = 1; j <= 100; j++) {
                if (frequencyCount[j] > 0) {
                    if (previousNumber != -1) {
                        minDifference = Math.min(minDifference, Math.abs(j - previousNumber));
                    }
                    previousNumber = j; // Update previous number to the current one
                }
            }

            // If no valid difference found, return -1, else return the minimum difference
            results[query.queryIndex] = (minDifference == Integer.MAX_VALUE) ? -1 : minDifference;
        }

        return results; // Return the results array containing answers for all queries
    }

    public static void main(String[] args) {
        MinimumAbsoluteDifferenceQueriesMedium solution = new MinimumAbsoluteDifferenceQueriesMedium();

        // Example 1
        int[] nums1 = {7, 1, 3, 3, 5};
        int[][] queries1 = {{0, 2}, {1, 4}, {2, 4}};
        int[] result1 = solution.minDiff(nums1, queries1);
        System.out.println(Arrays.toString(result1)); // Expected output: [2, 2, 2]

        // Example 2
        int[] nums2 = {4, 8, 2, 6, 10};
        int[][] queries2 = {{0, 3}, {2, 4}, {0, 4}};
        int[] result2 = solution.minDiff(nums2, queries2);
        System.out.println(Arrays.toString(result2)); // Expected output: [2, 4, 2]

        // Example 3
        int[] nums3 = {12, 3, 15, 9, 3};
        int[][] queries3 = {{0, 2}, {1, 3}, {0, 4}};
        int[] result3 = solution.minDiff(nums3, queries3);
        System.out.println(Arrays.toString(result3)); // Expected output: [3, 6, 3]
    }
}
