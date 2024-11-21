package designguru.MonotonicQueuePattern;

import java.util.Deque;
import java.util.LinkedList;

public class MaxValueofEquation {
    public int findMaxValueOfEquation(int[][] points, int k) {
        Deque<int[]> deque = new LinkedList<>();
        int maxValue = Integer.MIN_VALUE;

        for (int[] point : points) {
            int x = point[0];
            int y = point[1];

            // Remove points that are out of the x range (xi - xj > k)
            while (!deque.isEmpty() && x - deque.peekFirst()[0] > k) {
                deque.pollFirst();
            }

            // Calculate the equation value with the front of the deque
            if (!deque.isEmpty()) {
                maxValue = Math.max(maxValue, y + x + deque.peekFirst()[1] - deque.peekFirst()[0]);
            }

            // Remove points from the back of the deque that are less efficient
            while (!deque.isEmpty() && y - x >= deque.peekLast()[1] - deque.peekLast()[0]) {
                deque.pollLast();
            }

            // Add the current point to the deque
            deque.offerLast(new int[]{x, y});
        }

        return maxValue;
    }

    public static void main(String[] args) {
        MaxValueofEquation solution = new MaxValueofEquation();

        // Example 1
        int[][] points1 = {{0, 0}, {2, 1}, {3, 3}, {7, 10}};
        int k1 = 4;
        System.out.println("Example 1: " + solution.findMaxValueOfEquation(points1, k1)); // Expected output: 17

        // Example 2
        int[][] points2 = {{1, 2}, {3, 4}, {5, 6}};
        int k2 = 3;
        System.out.println("Example 2: " + solution.findMaxValueOfEquation(points2, k2)); // Expected output: 12

        // Example 3
        int[][] points3 = {{2, 4}, {5, 1}, {8, 7}, {10, 3}};
        int k3 = 5;
        System.out.println("Example 3: " + solution.findMaxValueOfEquation(points3, k3)); // Expected output: 12
    }
}
