package designguru.binaryIndexedTree;

import java.util.*;

public class ReversePairsHard {
    public int reversePairs(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;

        // Create a list to store double values of each element in nums
        List<Long> doubledValues = new ArrayList<>();
        for (int num : nums) {
            doubledValues.add(2L * num);
        }

        // Create a sorted set to remove duplicates and get a sorted list of numbers
        Set<Long> uniqueNumbers = new TreeSet<>();
        for (int num : nums) uniqueNumbers.add((long) num);
        uniqueNumbers.addAll(doubledValues);

        // Create a rank map to map each unique number to its rank
        Map<Long, Integer> rankMap = new HashMap<>();
        int rank = 0;
        for (long num : uniqueNumbers) {
            rankMap.put(num, ++rank);
        }

        // Initialize the Fenwick Tree with the size of unique numbers
        FenwickTree fenwickTree = new FenwickTree(rankMap.size());

        int reversePairCount = 0;
        for (int i = n - 1; i >= 0; i--) {
            // Query for the count of elements less than nums[i] / 2
            reversePairCount += fenwickTree.query(rankMap.get((long) nums[i]) - 1);
            // Update the Fenwick Tree with the rank of doubled value of nums[i]
            fenwickTree.update(rankMap.get(2L * nums[i]), 1);
        }

        return reversePairCount;
    }

    public static void main(String[] args) {
        ReversePairsHard solution = new ReversePairsHard();

        // Example 1
        int[] nums1 = {7, 1, 5, 2, 3};
        int expectedOutput1 = 4;
        System.out.println("Input: " + Arrays.toString(nums1));
        System.out.println("Expected Output: " + expectedOutput1);
        System.out.println("Actual Output: " + solution.reversePairs(nums1));

        // Example 2
        int[] nums2 = {10, 5, 2, 1};
        int expectedOutput2 = 4;
        System.out.println("Input: " + Arrays.toString(nums2));
        System.out.println("Expected Output: " + expectedOutput2);
        System.out.println("Actual Output: " + solution.reversePairs(nums2));

        // Example 3
        int[] nums3 = {7, 3, 2, 5};
        int expectedOutput3 = 2;
        System.out.println("Input: " + Arrays.toString(nums3));
        System.out.println("Expected Output: " + expectedOutput3);
        System.out.println("Actual Output: " + solution.reversePairs(nums3));
    }
}

class FenwickTree {
    private int[] sums;

    // Constructor to initialize the Fenwick Tree
    public FenwickTree(int n) {
        sums = new int[n + 1];
    }

    // Method to update the Fenwick Tree
    public void update(int index, int value) {
        while (index < sums.length) {
            sums[index] += value;
            index += lowbit(index); // Move to the next node
        }
    }

    // Method to query the cumulative frequency up to index
    public int query(int index) {
        int sum = 0;
        while (index > 0) {
            sum += sums[index];
            index -= lowbit(index); // Move to the parent node
        }
        return sum;
    }

    // Helper method to get the lowest bit
    private int lowbit(int x) {
        return x & (-x);
    }
}