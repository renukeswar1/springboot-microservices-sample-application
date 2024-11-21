package designguru.MonotonicQueuePattern;

import java.util.ArrayDeque;
import java.util.Deque;

public class SumofSubarrayMinimums {
    public int sumSubarrayMins(int[] arr) {
        int MOD = 1_000_000_007;  // Modulo value as per the problem statement
        int n = arr.length;
        long result = 0;  // To store the final sum of subarray minimums
        Deque<Integer> monotonicQueue = new ArrayDeque<>();  // Monotonic Queue to store indices

        for (int currentIndex = 0; currentIndex <= n; currentIndex++) {
            // Use 0 as a sentinel value when currentIndex equals array length
            int currentElement = currentIndex == n ? 0 : arr[currentIndex];

            // Maintain the Queue in increasing order of elements
            while (!monotonicQueue.isEmpty() && arr[monotonicQueue.peekLast()] > currentElement) {
                int minElementIndex = monotonicQueue.pollLast();
                int previousIndex = monotonicQueue.isEmpty() ? -1 : monotonicQueue.peekLast();

                // Calculate the number of subarrays where arr[minElementIndex] is the minimum
                int countSubarrays = (minElementIndex - previousIndex) * (currentIndex - minElementIndex);

                // Update result with the contribution of arr[minElementIndex]
                result = (result + (long) arr[minElementIndex] * countSubarrays % MOD) % MOD;
            }

            // Add the current index to the stack
            monotonicQueue.offerLast(currentIndex);
        }

        return (int) (result % MOD);  // Return the result modulo 10^9 + 7
    }

    public static void main(String[] args) {
        SumofSubarrayMinimums solution = new SumofSubarrayMinimums();

        // Example 1
        int[] arr1 = {3, 1, 2, 4, 5};
        System.out.println("Example 1: " + solution.sumSubarrayMins(arr1));

        // Example 2
        int[] arr2 = {2, 6, 5, 4};
        System.out.println("Example 2: " + solution.sumSubarrayMins(arr2));

        // Example 3
        int[] arr3 = {7, 3, 8};
        System.out.println("Example 3: " + solution.sumSubarrayMins(arr3));
    }
}
