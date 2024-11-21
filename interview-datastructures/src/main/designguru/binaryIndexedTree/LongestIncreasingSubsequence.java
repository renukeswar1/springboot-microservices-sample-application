package designguru.binaryIndexedTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LongestIncreasingSubsequence {
    // Define a class to represent each node in the BIT
    class BITNode {
        int frequency; // frequency of subsequences
        int maxLength; // maximum length of increasing subsequence

        BITNode() {
            frequency = 0;
            maxLength = 0;
        }
    }

    private int n; // length of the input array
    private List<BITNode> bit; // Binary Indexed Tree (BIT) to store max lengths and frequencies
    private List<Integer> nums; // the input array

    // Comparator to sort indices based on values in nums and then by index
    private boolean cmp(int index1, int index2) {
        if (nums.get(index1) != nums.get(index2)) {
            return nums.get(index1) < nums.get(index2);
        }
        return index1 > index2;
    }

    public int findNumberOfLIS(List<Integer> Nums) {
        this.n = Nums.size(); // get the size of the input array
        this.nums = Nums; // store the input array

        // Create a list of indices
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < n; i++) indices.add(i);

        // Sort indices using the custom comparator
        indices.sort((a, b) -> cmp(a, b) ? -1 : 1);

        // Initialize BIT with n+1 nodes
        bit = new ArrayList<>();
        for (int i = 0; i <= n; i++) bit.add(new BITNode());

        // Traverse through the sorted indices
        for (int index : indices) {
            int maxLength = getMax(index); // Get max length of increasing subsequence ending before this index

            if (maxLength == 0) { // If no subsequence is found, start a new one
                update(index, 1, 1);
                continue;
            }

            int frequency = getFrequency(index, maxLength); // Get frequency of subsequences with max length
            update(index, maxLength + 1, frequency); // Update BIT with new max length and frequency
        }

        int maxLength = getMax(n - 1); // Get max length of increasing subsequence in the whole array
        return getFrequency(n - 1, maxLength); // Return the frequency of subsequences with max length
    }

    private int getMax(int index) {
        index++;
        int result = 0;
        while (index > 0) {
            result = Math.max(result, bit.get(index).maxLength); // Update result with the max length found
            index -= index & -index; // Move to the parent node
        }
        return result;
    }

    private int getFrequency(int index, int maxLength) {
        index++;
        int result = 0;
        while (index > 0) {
            if (bit.get(index).maxLength == maxLength) {
                result += bit.get(index).frequency; // Accumulate frequency of matching subsequences
            }
            index -= index & -index; // Move to the parent node
        }
        return result;
    }

    private void update(int index, int maxLength, int frequency) {
        index++;
        while (index <= n) {
            if (bit.get(index).maxLength < maxLength) {
                bit.get(index).maxLength = maxLength; // Update max length
                bit.get(index).frequency = frequency; // Set the frequency
            } else if (bit.get(index).maxLength == maxLength) {
                bit.get(index).frequency += frequency; // Increment frequency for existing max length
            }
            index += index & -index; // Move to the next node
        }
    }

    public static void main(String[] args) {
        LongestIncreasingSubsequence solution = new LongestIncreasingSubsequence();

        // Example 1
        List<Integer> nums1 = Arrays.asList(2, 6, 4, 3, 5);
        int result1 = solution.findNumberOfLIS(nums1);
        System.out.println("Number of Longest Increasing Subsequences: " + result1); // Expected output: 2

        // Example 2
        List<Integer> nums2 = Arrays.asList(10, 9, 2, 5, 3, 7, 101, 18);
        int result2 = solution.findNumberOfLIS(nums2);
        System.out.println("Number of Longest Increasing Subsequences: " + result2); // Expected output: 4

        // Example 3
        List<Integer> nums3 = Arrays.asList(1, 5, 2, 6, 3, 7);
        int result3 = solution.findNumberOfLIS(nums3);
        System.out.println("Number of Longest Increasing Subsequences: " + result3); // Expected output: 3
    }
}
