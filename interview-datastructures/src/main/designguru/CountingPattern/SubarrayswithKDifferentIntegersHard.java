package designguru.CountingPattern;

public class SubarrayswithKDifferentIntegersHard {

    public int subarraysWithKDistinct(int[] nums, int k) {
        // Array to count distinct values in the current window
        int[] count = new int[nums.length + 1];

        int result = 0; // Total number of subarrays with exactly k distinct integers
        int left = 0;   // Left boundary of the window
        int right = 0;  // Right boundary of the window
        int windowCount = 0;  // Current number of valid subarrays in the window

        while (right < nums.length) {
            // Increase the count for the current element and move the right boundary
            if (count[nums[right++]]++ == 0) {
                // If this is a new distinct element, decrement k
                k--;
            }

            // If there are more than k distinct elements, adjust the window from the left
            if (k < 0) {
                // Reduce the count for the element at the left boundary and move the left boundary
                --count[nums[left++]];
                k++;
                windowCount = 0;
            }

            // If there are exactly k distinct elements, count the subarrays
            if (k == 0) {
                // Move the left boundary to reduce the window size while maintaining k distinct elements
                while (count[nums[left]] > 1) {
                    --count[nums[left++]];
                    windowCount++;
                }
                // Add the current number of valid subarrays to the result
                result += (windowCount + 1);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        SubarrayswithKDifferentIntegersHard solution = new SubarrayswithKDifferentIntegersHard();

        // Example 1
        int[] nums1 = {4, 2, 4, 2, 5};
        int k1 = 2;
        System.out.println(solution.subarraysWithKDistinct(nums1, k1)); // Output: 7

        // Example 2
        int[] nums2 = {2, 4, 1, 3, 1, 3};
        int k2 = 3;
        System.out.println(solution.subarraysWithKDistinct(nums2, k2)); // Output: 4

        // Example 3
        int[] nums3 = {4, 4, 4, 5, 5, 6};
        int k3 = 1;
        System.out.println(solution.subarraysWithKDistinct(nums3, k3)); // Output: 10
    }
}
