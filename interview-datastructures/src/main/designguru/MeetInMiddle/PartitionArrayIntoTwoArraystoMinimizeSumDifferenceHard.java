package designguru.MeetInMiddle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PartitionArrayIntoTwoArraystoMinimizeSumDifferenceHard {
    // Generate all possible subset sums for each subset size
    public List<Integer>[] generateSubsetSums(int[] nums) {
        int length = nums.length; // Length of the array
        List<Integer>[] subsetSums = new ArrayList[length + 1]; // Array of lists for subset sums
        for (int i = 0; i <= length; i++) subsetSums[i] = new ArrayList<>(); // Initialize each list

        // Generate all subsets using bitmask
        for (int mask = 0; mask < (1 << length); mask++) {
            int sum = 0;
            int subsetSize = 0, index = 0;
            int bitmask = mask;

            // Calculate the sum of the current subset
            while (bitmask != 0) {
                if ((bitmask & 1) == 1) {
                    sum += nums[index];
                    subsetSize++;
                }
                index++;
                bitmask >>>= 1; // Shift right to process next bit
            }
            // Add the subset sum to the list for its size
            subsetSums[subsetSize].add(sum);
        }
        return subsetSums;
    }

    // Compute the minimum difference between two subsets
    public int minimumDifference(int[] nums) {
        int n = nums.length;
        int minimumDifference = Integer.MAX_VALUE; // Start with a large value
        int totalSum = 0;

        // Calculate total sum of the array
        for (int num : nums) totalSum += num;

        // Generate subset sums for each half of the array
        List<Integer>[] leftSubsetSums = generateSubsetSums(Arrays.copyOfRange(nums, 0, n / 2));
        List<Integer>[] rightSubsetSums = generateSubsetSums(Arrays.copyOfRange(nums, n / 2, n));

        // Sort sums of the second half for efficient searching
        for (int i = 0; i < rightSubsetSums.length; i++) Collections.sort(rightSubsetSums[i]);

        // Compare subset sums from both halves to find the minimum difference
        for (int i = 0; i < leftSubsetSums.length; i++) {
            for (Integer leftSum : leftSubsetSums[i]) {
                int remainingElements = n / 2 - i; // Number of elements left in the second half
                int targetSum = totalSum / 2 - leftSum; // Target sum to find in the second half

                // Find the closest sum in the second half using binary search
                int index = Collections.binarySearch(rightSubsetSums[remainingElements], targetSum);
                index = index < 0 ? -(index + 1) : index; // Adjust index for insertion point

                // Check the closest sums and update the minimum difference
                if (index < rightSubsetSums[remainingElements].size()) {
                    // Compute the difference using the closest sum at the found index
                    minimumDifference = Math.min(minimumDifference,
                            Math.abs(totalSum - 2 * leftSum - 2 * rightSubsetSums[remainingElements].get(index)));
                }
                // If the index is greater than 0, check the sum at the previous index
                if (index > 0) {
                    minimumDifference = Math.min(minimumDifference,
                            Math.abs(totalSum - 2 * leftSum - 2 * rightSubsetSums[remainingElements].get(index - 1)));
                }
            }
        }
        return minimumDifference; // Return the minimum difference found
    }

    // Main method to test the solution
    public static void main(String[] args) {
        PartitionArrayIntoTwoArraystoMinimizeSumDifferenceHard solution = new PartitionArrayIntoTwoArraystoMinimizeSumDifferenceHard();

        // Test cases
        System.out.println(solution.minimumDifference(new int[]{1, 6, 11, 5})); // Output: 1
        System.out.println(solution.minimumDifference(new int[]{2, 3, 9, 12})); // Output: 2
        System.out.println(solution.minimumDifference(new int[]{7, 4, 5, 9}));  // Output: 1
    }
}
