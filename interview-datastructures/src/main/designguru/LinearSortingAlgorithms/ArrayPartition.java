package designguru.LinearSortingAlgorithms;

public class ArrayPartition {
    // Method to find the maximum sum of min pairs
    public int arrayPairSum(int[] nums) {
        // Determine the maximum value in the array to define the range for counting sort
        int max = 10000; // given constraint for the problem

        // Array to store the frequency of each element
        int[] count = new int[2 * max + 1];

        // Fill the count array
        for (int num : nums) {
            count[num + max]++;
        }

        // Initialize variables for calculating the sum and a flag to toggle between pairs
        int sum = 0;
        boolean add = true;

        // Traverse the count array
        for (int i = 0; i < count.length; i++) {
            while (count[i] > 0) {
                // Add to sum if the flag is true
                if (add) {
                    sum += i - max;
                }
                // Toggle the flag
                add = !add;
                // Decrement the count for the current number
                count[i]--;
            }
        }

        return sum;
    }

    public static void main(String[] args) {
        ArrayPartition solution = new ArrayPartition();

        // Test case 1
        int[] nums1 = {5, 3, 1, 2, 4, 6, 1, 4};
        int result1 = solution.arrayPairSum(nums1);
        System.out.println("Test Case 1 Result: " + result1); // Expected Output: 12

        // Test case 2
        int[] nums2 = {1, 1, 1, 1, 2, 2};
        int result2 = solution.arrayPairSum(nums2);
        System.out.println("Test Case 2 Result: " + result2); // Expected Output: 4

        // Test case 3
        int[] nums3 = {8, 9, 5, 6, 3, 4};
        int result3 = solution.arrayPairSum(nums3);
        System.out.println("Test Case 3 Result: " + result3); // Expected Output: 16
    }
}
