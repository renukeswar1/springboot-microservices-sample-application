package designguru.CountingPattern;

import java.util.HashMap;
import java.util.Map;

public class CountElementsWithMaximumFrequency {
    public int maxFrequencyElements(int[] nums) {
        Map<Integer, Integer> frequencies = new HashMap<>();
        int maxFrequency = 0;
        int totalFrequencies = 0;

        // Calculate the frequency of each element
        for (int num : nums) {
            frequencies.put(num, frequencies.getOrDefault(num, 0) + 1);
            int frequency = frequencies.get(num);

            // If we find a new maximum frequency, update maxFrequency and totalFrequencies to frequency
            if (frequency > maxFrequency) {
                maxFrequency = frequency;
                totalFrequencies = frequency;
            } else if (frequency == maxFrequency) {
                totalFrequencies += frequency;
            }
        }
        return totalFrequencies;
    }

    public static void main(String[] args) {
        CountElementsWithMaximumFrequency solution = new CountElementsWithMaximumFrequency();

        // Test example 1
        int[] nums1 = {3, 2, 2, 3, 1, 4};
        System.out.println(solution.maxFrequencyElements(nums1));  // Output: 4

        // Test example 2
        int[] nums2 = {5, 5, 5, 2, 2, 3};
        System.out.println(solution.maxFrequencyElements(nums2));  // Output: 3

        // Test example 3
        int[] nums3 = {7, 8, 8, 7, 9, 9};
        System.out.println(solution.maxFrequencyElements(nums3));  // Output: 6
    }
}
