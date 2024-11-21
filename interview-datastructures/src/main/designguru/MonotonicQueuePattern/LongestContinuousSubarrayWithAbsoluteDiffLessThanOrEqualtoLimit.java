package designguru.MonotonicQueuePattern;

import java.util.Deque;
import java.util.LinkedList;

public class LongestContinuousSubarrayWithAbsoluteDiffLessThanOrEqualtoLimit {
    // Method to find the length of the longest subarray with an absolute difference less than or equal to limit
    public int longestSubarray(int[] nums, int limit) {
        // Monotonic queues to store the indices of elements in the order of minimum and maximum
        Deque<Integer> maxDeque = new LinkedList<>();
        Deque<Integer> minDeque = new LinkedList<>();

        int start = 0; // The starting index of the current subarray
        int maxLength = 0; // The maximum length of the subarray found so far

        for (int end = 0; end < nums.length; end++) {
            // Maintain the maxDeque to have the maximum value at the front
            while (!maxDeque.isEmpty() && nums[maxDeque.peekLast()] <= nums[end]) {
                maxDeque.pollLast();
            }
            maxDeque.addLast(end);

            // Maintain the minDeque to have the minimum value at the front
            while (!minDeque.isEmpty() && nums[minDeque.peekLast()] >= nums[end]) {
                minDeque.pollLast();
            }
            minDeque.addLast(end);

            // Check the difference between the current maximum and minimum in the window
            while (nums[maxDeque.peekFirst()] - nums[minDeque.peekFirst()] > limit) {
                // If the difference exceeds the limit, move the start pointer to the right
                start++;
                // Remove the elements from the deque if they are out of the current window
                if (maxDeque.peekFirst() < start) {
                    maxDeque.pollFirst();
                }
                if (minDeque.peekFirst() < start) {
                    minDeque.pollFirst();
                }
            }

            // Update the maximum length found
            maxLength = Math.max(maxLength, end - start + 1);
        }

        return maxLength;
    }

    // Main method to test the solution with provided examples
    public static void main(String[] args) {
        LongestContinuousSubarrayWithAbsoluteDiffLessThanOrEqualtoLimit solution = new LongestContinuousSubarrayWithAbsoluteDiffLessThanOrEqualtoLimit();

        // Example 1
        int[] nums1 = {1, 3, 6, 7, 9, 10};
        int limit1 = 3;
        System.out.println("Output 1: " + solution.longestSubarray(nums1, limit1)); // Expected output: 3

        // Example 2
        int[] nums2 = {8, 2, 4, 10, 12};
        int limit2 = 6;
        System.out.println("Output 2: " + solution.longestSubarray(nums2, limit2)); // Expected output: 3

        // Example 3
        int[] nums3 = {1, 5, 9, 13, 14};
        int limit3 = 4;
        System.out.println("Output 3: " + solution.longestSubarray(nums3, limit3)); // Expected output: 2
    }
}
