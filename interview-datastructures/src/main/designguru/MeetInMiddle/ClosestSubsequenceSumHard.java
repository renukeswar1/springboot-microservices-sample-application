package designguru.MeetInMiddle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ClosestSubsequenceSumHard {

    // Method to find the closest subsequence sum to the goal
    public int minAbsDifference(int[] nums, int goal) {
        // Divide the array into two halves
        int n = nums.length;
        int[] left = Arrays.copyOfRange(nums, 0, n / 2);
        int[] right = Arrays.copyOfRange(nums, n / 2, n);

        // Generate all possible sums for both halves
        List<Integer> leftSums = generateSubsequenceSums(left);
        List<Integer> rightSums = generateSubsequenceSums(right);

        // Sort the right half sums for binary search
        Collections.sort(rightSums);

        int minDiff = Integer.MAX_VALUE;

        // Compare each sum from the left half with the closest sum from the right half
        for (int sum : leftSums) {
            int target = goal - sum;
            minDiff = Math.min(minDiff, Math.abs(target)); // Compare with 0 (empty subsequence)

            int idx = Collections.binarySearch(rightSums, target);
            if (idx >= 0) {
                return 0; // If exact match is found
            }

            // If no exact match, find the closest values
            idx = -idx - 1;
            if (idx < rightSums.size()) {
                minDiff = Math.min(minDiff, Math.abs(target - rightSums.get(idx)));
            }
            if (idx > 0) {
                minDiff = Math.min(minDiff, Math.abs(target - rightSums.get(idx - 1)));
            }
        }

        return minDiff;
    }

    // Method to generate all possible sums of subsequences for a given array
    private List<Integer> generateSubsequenceSums(int[] nums) {
        List<Integer> result = new ArrayList<>();
        int n = nums.length;
        for (int i = 0; i < (1 << n); i++) {
            int sum = 0;
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) != 0) {
                    sum += nums[j];
                }
            }
            result.add(sum);
        }
        return result;
    }

    public static void main(String[] args) {
        ClosestSubsequenceSumHard solution = new ClosestSubsequenceSumHard();

        // Test case 1
        int[] nums1 = {1, 2, 3, 1, 1};
        int goal1 = 6;
        System.out.println("Actual Output: " + solution.minAbsDifference(nums1, goal1));

        // Test case 2
        int[] nums2 = {1, 2, 9, -6, 15};
        int goal2 = 7;
        System.out.println("Actual Output: " + solution.minAbsDifference(nums2, goal2));

        // Test case 3
        int[] nums3 = {1, 2, 4, 5};
        int goal3 = -1;
        System.out.println("Actual Output: " + solution.minAbsDifference(nums3, goal3));
    }
}
