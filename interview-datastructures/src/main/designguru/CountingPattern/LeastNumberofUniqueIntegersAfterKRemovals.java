package designguru.CountingPattern;

import java.util.HashMap;
import java.util.Map;

public class LeastNumberofUniqueIntegersAfterKRemovals {
    public int reduceUniqueInts(int[] arr, int k) {
        // Step 1: Count the frequency of each element
        Map<Integer, Integer> elementFrequency = new HashMap<>();
        for (int num : arr) {
            elementFrequency.put(num, elementFrequency.getOrDefault(num, 0) + 1);
        }

        int n = arr.length;

        // Step 2: Array to count how many elements have a certain frequency
        int[] frequencyCount = new int[n + 1];

        // Step 3: Fill the frequencyCount array based on elementFrequency map
        for (int freq : elementFrequency.values()) {
            frequencyCount[freq]++;
        }

        // Total unique elements
        int uniqueElements = elementFrequency.size();

        // Step 4: Iterate through possible frequencies and remove elements
        for (int i = 1; i <= n; i++) {
            int elementsToRemove = Math.min(k / i, frequencyCount[i]);
            k -= (i * elementsToRemove);
            uniqueElements -= elementsToRemove;

            if (k < i) {
                return uniqueElements;
            }
        }

        return 0;
    }

    public static void main(String[] args) {
        LeastNumberofUniqueIntegersAfterKRemovals reducer = new LeastNumberofUniqueIntegersAfterKRemovals();
        System.out.println(reducer.reduceUniqueInts(new int[]{5, 5, 4, 3, 2, 3, 2, 3, 3, 2}, 6)); // Output: 1
        System.out.println(reducer.reduceUniqueInts(new int[]{7, 7, 7, 8, 8, 9}, 2)); // Output: 2
        System.out.println(reducer.reduceUniqueInts(new int[]{1, 2, 2, 3, 4, 3}, 4)); // Output: 1
    }
}
