package designguru.LinearSortingAlgorithms;

public class MaximumGap {
    public int findmaximumGap(int[] nums) {
        // If the array is null or has fewer than 2 elements, return 0.
        if (nums == null || nums.length < 2) {
            return 0;
        }

        // Find the maximum value in the array to determine the number of digits
        int maxValue = Integer.MIN_VALUE;
        for (int number : nums) {
            maxValue = Math.max(maxValue, number);
        }

        int digitPlace = 1; // Start with the least significant digit
        int base = 10; // We're sorting by decimal digits
        int[] buffer = new int[nums.length]; // Auxiliary array for sorting

        // Perform radix sort based on each digit
        while (maxValue / digitPlace > 0) {
            int[] count = new int[base]; // Count array for digits (0-9)

            // Count occurrences of each digit in the current digit place
            for (int number : nums) {
                count[(number / digitPlace) % 10]++;
            }

            // Accumulate the count array to get positions of elements
            for (int i = 1; i < count.length; i++) {
                count[i] += count[i - 1];
            }

            // Build the output array by placing elements in the correct position
            for (int i = nums.length - 1; i >= 0; i--) {
                buffer[--count[(nums[i] / digitPlace) % 10]] = nums[i];
            }

            // Copy the sorted nums back into the original array
            System.arraycopy(buffer, 0, nums, 0, nums.length);

            // Move to the next digit place
            digitPlace *= 10;
        }

        // Calculate the maximum gap between consecutive elements
        int maximumGap = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            maximumGap = Math.max(nums[i + 1] - nums[i], maximumGap);
        }

        return maximumGap;
    }

    // Main method to test the function
    public static void main(String[] args) {
        MaximumGap solution = new MaximumGap();

        int[] nums1 = {10, 50, 20, 90, 60};
        System.out.println("Maximum Gap: " + solution.findmaximumGap(nums1)); // Output: 30

        int[] nums2 = {5, 100, 1, 50, 9};
        System.out.println("Maximum Gap: " + solution.findmaximumGap(nums2)); // Output: 50

        int[] nums3 = {300, 10, 200, 100, 600};
        System.out.println("Maximum Gap: " + solution.findmaximumGap(nums3)); // Output: 300
    }
}
