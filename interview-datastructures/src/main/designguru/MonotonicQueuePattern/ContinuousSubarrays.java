package designguru.MonotonicQueuePattern;

import java.util.ArrayDeque;
import java.util.Deque;

public class ContinuousSubarrays {
    Deque<Integer> maxQ;
    Deque<Integer> minQ;

    public int getMaxInSubarray() {
        // Return -1 if max queue is empty, otherwise return the first element
        return maxQ.size() == 0 ? -1 : maxQ.peekFirst();
    }

    public int getMinInSubarray() {
        // Return -1 if min queue is empty, otherwise return the first element
        return minQ.size() == 0 ? -1 : minQ.peekFirst();
    }

    public long continuousSubarrays(int[] nums) {
        minQ = new ArrayDeque<>();
        maxQ = new ArrayDeque<>();

        int left = 0; // Left pointer representing the start of the subarray
        int right = 0; // Right pointer representing the end of the subarray
        long ans = 0;
        int n = nums.length;

        // Iterate through the array using the right pointer
        while (right < n) {
            // Ensure maxQ contains elements in decreasing order from front to back
            while (maxQ.size() > 0 && maxQ.peekLast() < nums[right]) {
                maxQ.removeLast();
            }
            // Ensure minQ contains elements in increasing order from front to back
            while (minQ.size() > 0 && minQ.peekLast() > nums[right]) {
                minQ.removeLast();
            }
            // Add the current element to both maxQ and minQ
            maxQ.addLast(nums[right]);
            minQ.addLast(nums[right]);

            // Adjust the left pointer to maintain the subarray's condition
            while (getMaxInSubarray() - getMinInSubarray() > 2) {
                // Remove the element at the left pointer if it's the first in maxQ or minQ
                if (maxQ.size() > 0 && maxQ.peekFirst() == nums[left]) {
                    maxQ.removeFirst();
                }
                if (minQ.size() > 0 && minQ.peekFirst() == nums[left]) {
                    minQ.removeFirst();
                }
                left++;
            }

            // Add the number of subarrays ending at right to the answer
            ans += right - left + 1;
            right++;
        }
        return ans;
    }

    public static void main(String[] args) {
        ContinuousSubarrays solution = new ContinuousSubarrays();

        // Test case 1
        int[] nums1 = {1, 2, 2, 13, 1};
        System.out.println("Example 1: " + solution.continuousSubarrays(nums1));

        // Test case 2
        int[] nums2 = {3, 3, 4, 2};
        System.out.println("Example 2: " + solution.continuousSubarrays(nums2));

        // Test case 3
        int[] nums3 = {5, 17, 35, 26, 5};
        System.out.println("Example 3: " + solution.continuousSubarrays(nums3));
    }
}
