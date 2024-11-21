package designguru.MonotonicQueuePattern;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

public class MinimumNumberofCoinsforFruits {
    public int minimumCoins(int[] prices) {
        int n = prices.length;
        Deque<Integer> deque = new LinkedList<>();
        deque.addLast(n);
        prices = Arrays.copyOf(prices, n + 1);
        prices[n] = 0;  // Add a dummy fruit with 0 coins to avoid boundary issues.

        // Iterate from the last fruit to the first
        for (int i = n - 1; i >= 0; i--) {
            // Calculate the maximum index that can be covered by the current fruit
            int maxCoveredIndex = Math.min(n, 2 * i + 2);

            // Remove all elements from the front that are outside the range of current fruit's reward
            while (!deque.isEmpty() && deque.peekFirst() > maxCoveredIndex) {
                deque.pollFirst();
            }

            // Update the cost of the current fruit by adding the minimum cost of reachable fruits
            prices[i] += prices[deque.peekFirst()];

            // Maintain the deque to keep it increasing
            while (!deque.isEmpty() && prices[i] < prices[deque.peekLast()]) {
                deque.pollLast();
            }

            // Add the current fruit index to the deque
            deque.addLast(i);
        }

        // Return the minimum cost to get all fruits starting from the first one
        return prices[0];
    }

    public static void main(String[] args) {
        MinimumNumberofCoinsforFruits solution = new MinimumNumberofCoinsforFruits();

        // Example 1
        int[] prices1 = {1, 6, 1, 2, 4};
        System.out.println("Example 1: " + solution.minimumCoins(prices1)); // Expected Output: 2

        // Example 2
        int[] prices2 = {2, 3, 5, 1};
        System.out.println("Example 2: " + solution.minimumCoins(prices2)); // Expected Output: 5

        // Example 3
        int[] prices3 = {3, 2, 1, 4};
        System.out.println("Example 3: " + solution.minimumCoins(prices3)); // Expected Output: 4
    }
}
