package designguru.MeetInMiddle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SubsetSumEqualToTarget {

    // Method to find the number of subsets that sum up to the target x using the meet in the middle approach
    public int countSubsets(int[] nums, int x) {
        int n = nums.length;

        // Split the array into two halves
        int[] left = Arrays.copyOfRange(nums, 0, n / 2);
        int[] right = Arrays.copyOfRange(nums, n / 2, n);

        // Generate all possible subset sums for each half
        List<Integer> leftSums = generateSubsetSums(left);
        List<Integer> rightSums = generateSubsetSums(right);

        // Sort the right sums to use binary search
        Collections.sort(rightSums);

        int count = 0;

        // For each sum in the leftSums list, find the complementary sum in rightSums that sums up to x
        for (int leftSum : leftSums) {
            int complement = x - leftSum;
            count += countOccurrences(rightSums, complement);
        }

        return count;
    }

    // Helper method to generate all possible subset sums for a given array
    private List<Integer> generateSubsetSums(int[] nums) {
        List<Integer> sums = new ArrayList<>();
        int n = nums.length;

        // There are 2^n possible subsets
        for (int i = 0; i < (1 << n); i++) {
            int sum = 0;
            for (int j = 0; j < n; j++) {
                // Check if jth element is included in the subset
                if ((i & (1 << j)) != 0) {
                    sum += nums[j];
                }
            }
            sums.add(sum);
        }
        return sums;
    }

    // Helper method to count occurrences of a target value in a sorted list using binary search
    private int countOccurrences(List<Integer> list, int target) {
        // Find the first occurrence of the target using binary search
        int leftIndex = Collections.binarySearch(list, target);
        if (leftIndex < 0) {
            return 0; // Target not found
        }

        // Find the range of elements equal to target
        int count = 0;
        int index = leftIndex;

        // Count occurrences to the left
        while (index >= 0 && list.get(index) == target) {
            count++;
            index--;
        }

        // Count occurrences to the right
        index = leftIndex + 1;
        while (index < list.size() && list.get(index) == target) {
            count++;
            index++;
        }

        return count;
    }

    // Main method to test the function with different examples
    public static void main(String[] args) {
        SubsetSumEqualToTarget solution = new SubsetSumEqualToTarget();

        // Test case 1
        int[] nums2 = {1, 2, 3, 4, 5};
        int x2 = 5;
        System.out.println("Number of subsets that sum to " + x2 + ": " + solution.countSubsets(nums2, x2));
        // Expected output: 3

        // Test case 2
        int[] nums1 = {2, 4, 6, 10};
        int x1 = 16;
        System.out.println("Number of subsets that sum to " + x1 + ": " + solution.countSubsets(nums1, x1));
        // Expected output: 2

        // Test case 3
        int[] nums3 = {7, -3, 2, 5, 1};
        int x3 = 9;
        System.out.println("Number of subsets that sum to " + x3 + ": " + solution.countSubsets(nums3, x3));
        // Expected output: 2
    }
}
