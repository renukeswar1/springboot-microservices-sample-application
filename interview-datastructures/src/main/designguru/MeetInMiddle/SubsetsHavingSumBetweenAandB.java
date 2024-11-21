package designguru.MeetInMiddle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SubsetsHavingSumBetweenAandB {
    // Count the number of subsets whose sum falls between A and B, inclusive
    public int countSubsets(int[] nums, int A, int B) {
        int n = nums.length;
        int mid = n / 2;

        // Split the array into two halves
        int[] set1 = Arrays.copyOfRange(nums, 0, mid);
        int[] set2 = Arrays.copyOfRange(nums, mid, n);

        // Generate all possible subset sums for both halves
        List<Integer> sum1 = generateSubsetSums(set1);
        List<Integer> sum2 = generateSubsetSums(set2);

        // Sort sum2 for binary search
        Collections.sort(sum2);

        int count = 0;

        // For each sum in sum1, find the number of valid sums in sum2 that together fall within [A, B]
        for (int s1 : sum1) {
            int low = A - s1; // Lower bound of range in sum2
            int high = B - s1; // Upper bound of range in sum2
            count += countInRange(sum2, low, high); // Count sums in range [low, high]
        }

        return count; // Return the total count of valid subsets
    }

    // Generate all possible subset sums for a given set
    private List<Integer> generateSubsetSums(int[] set) {
        List<Integer> result = new ArrayList<>();
        int n = set.length;
        // Iterate over all possible subsets using bitmask
        for (int i = 0; i < (1 << n); i++) {
            int sum = 0;
            // Calculate the sum of the current subset
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) > 0) {
                    sum += set[j];
                }
            }
            result.add(sum); // Add the subset sum to the result list
        }
        return result; // Return the list of all subset sums
    }

    // Count the number of elements in sortedList within the range [low, high]
    private int countInRange(List<Integer> sortedList, int low, int high) {
        int left = lowerBound(sortedList, low); // Find the lower bound of low
        int right = upperBound(sortedList, high); // Find the upper bound of high
        return right - left; // Return the count of elements in the range
    }

    // Find the first position where elements are not less than target
    private int lowerBound(List<Integer> sortedList, int target) {
        int low = 0, high = sortedList.size();
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (sortedList.get(mid) < target) {
                low = mid + 1; // Move to the right side
            } else {
                high = mid; // Move to the left side
            }
        }
        return low; // Return the position of the lower bound
    }

    // Find the first position where elements are greater than target
    private int upperBound(List<Integer> sortedList, int target) {
        int low = 0, high = sortedList.size();
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (sortedList.get(mid) <= target) {
                low = mid + 1; // Move to the right side
            } else {
                high = mid; // Move to the left side
            }
        }
        return low; // Return the position of the upper bound
    }

    // Main method to test the solution
    public static void main(String[] args) {
        SubsetsHavingSumBetweenAandB solution = new SubsetsHavingSumBetweenAandB();

        int[] nums1 = {1, -1, 2, 3};
        System.out.println(solution.countSubsets(nums1, 1, 4)); // Output: 10

        int[] nums2 = {3, 5, -7};
        System.out.println(solution.countSubsets(nums2, -4, 3)); // Output: 5

        int[] nums3 = {4, 1, -2, 7, -3};
        System.out.println(solution.countSubsets(nums3, 0, 6)); // Output: 16
    }
}
