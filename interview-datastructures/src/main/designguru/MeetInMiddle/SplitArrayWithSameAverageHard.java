package designguru.MeetInMiddle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SplitArrayWithSameAverageHard {

    // Function to create all subsets of each half of the array
    public List<Integer>[] generateSubsetSums(int[] arrayPart) {
        int halfLength = arrayPart.length; // Get the length of the current half of the array
        List<Integer>[] subsetSums = new List[halfLength + 1]; // Create an array of lists to store subset sums based on their size

        // Initialize each list in the subsetSums array
        for (int i = 0; i <= halfLength; i++) {
            subsetSums[i] = new ArrayList<>();
        }

        // Generating all subsets using an iterative method (power set)
        for (int mask = 0; mask < (1 << halfLength); mask++) {
            int subsetSize = 0, subsetSum = 0, currentMask = mask, index = 0;
            while (currentMask != 0) {
                if ((currentMask & 1) == 1) { // Check if the lowest bit is 1 (part of the subset)
                    subsetSize++; // Increment the size of the current subset
                    subsetSum += arrayPart[index]; // Add the current element to the subset sum
                }
                index++;
                currentMask >>= 1; // Shift right to process the next bit
            }
            subsetSums[subsetSize].add(subsetSum); // Add the sum of this subset to the corresponding list
        }
        return subsetSums; // Return the array of lists with all subset sums
    }

    public boolean splitArraySameAverage(int[] nums) {
        int arrayLength = nums.length;
        if (arrayLength == 1) return false; // A single element cannot be split into two subsets
        if (arrayLength == 2) return nums[0] == nums[1]; // With two elements, they must be equal to split equally

        int halfLength = arrayLength / 2; // Calculate the length of each half
        int totalSum = Arrays.stream(nums).sum(); // Calculate the total sum of the array

        // Generate subset sums for the first half and second half of the array
        List<Integer>[] firstHalfSubsetSums = generateSubsetSums(Arrays.copyOfRange(nums, 0, halfLength + 1));
        List<Integer>[] secondHalfSubsetSums = generateSubsetSums(Arrays.copyOfRange(nums, halfLength + 1, arrayLength));

        // Sort each list in the second half subset for binary search later
        for (int i = 0; i < halfLength; i++) {
            Collections.sort(secondHalfSubsetSums[i]);
        }

        // Check if we can find two subsets (one from each half) that satisfy the condition
        for (int i = 0; i < firstHalfSubsetSums.length; i++) {
            for (int sum1 : firstHalfSubsetSums[i]) {
                for (int j = 0; j < secondHalfSubsetSums.length; j++) {
                    // Skip cases where the subset size is 0 or equal to the total length
                    if (i + j == 0 || i + j == nums.length) continue;

                    // Calculate the required sum for the second subset
                    double requiredSum = ((1.0 * totalSum * (i + j)) / nums.length) - sum1;
                    if (Math.ceil(requiredSum) != requiredSum) continue; // Check if requiredSum is an integer

                    // Check if the required sum exists in the second half subsets using binary search
                    if (Collections.binarySearch(secondHalfSubsetSums[j], (int) requiredSum) >= 0) {
                        return true; // If found, return true
                    }
                }
            }
        }
        return false; // If no valid subset found, return false
    }

    // Main method to test the code with provided examples
    public static void main(String[] args) {
        SplitArrayWithSameAverageHard solution = new SplitArrayWithSameAverageHard();

        // Example 1
        int[] nums1 = {1, 5, 3, 5, 6};
        System.out.println("Example 1: " + solution.splitArraySameAverage(nums1));

        // Example 2
        int[] nums2 = {3, 5, 6, 4, 2, 10};
        System.out.println("Example 2: " + solution.splitArraySameAverage(nums2));

        // Example 3
        int[] nums3 = {4, 8, 16, 24, 32, 40};
        System.out.println("Example 3: " + solution.splitArraySameAverage(nums3));
    }
}
