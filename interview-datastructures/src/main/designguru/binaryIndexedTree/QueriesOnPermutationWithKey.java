package designguru.binaryIndexedTree;

import java.util.ArrayList;
import java.util.List;

public class QueriesOnPermutationWithKey {

    // Method to calculate the prefix sum up to index 'index' using BIT
    private int getPrefixSum(int[] BIT, int index) {
        int sum = 0;
        // Traverse the BIT array backward to calculate the sum
        for (; index > 0; index -= (index & -index)) {
            sum += BIT[index];
        }
        return sum;
    }

    // Method to update the BIT array at index 'index' with a value val
    private void updateBIT(int[] BIT, int index, int val) {
        // Traverse the BIT array forward to update the values
        for (; index < BIT.length; index += (index & -index)) {
            BIT[index] += val;
        }
    }

    // Main method to process the queries
    public List<Integer> processQueries(int[] queries, int m) {

        int n = queries.length;
        List<Integer> result = new ArrayList<>();  // List to store the final result
        int[] BIT = new int[m + n + 1];  // Binary Indexed Tree with size m + n + 1
        int[] position = new int[m + 1];  // Array to store the position of each element

        // Initialize the position array and build the initial BIT
        for (int i = 1; i <= m; i++) {
            position[i] = n + i;  // Position starts after the queries array
            updateBIT(BIT, n + i, 1);  // Build the BIT by adding initial elements
        }

        // Process each query
        for (int q : queries) {
            // Get the current position of the query element
            result.add(getPrefixSum(BIT, position[q] - 1));
            // Update the BIT to remove the element from its current position
            updateBIT(BIT, position[q], -1);
            // Update the BIT to move the element to the front
            updateBIT(BIT, n, 1);
            // Update the position of the element to the new position (front)
            position[q] = n--;
        }

        return result;  // Return the result list
    }

    public static void main(String[] args) {
        QueriesOnPermutationWithKey solution = new QueriesOnPermutationWithKey();

        // Example 1
        int[] queries1 = {4, 3, 2, 7, 5};
        int m1 = 7;
        List<Integer> result1 = solution.processQueries(queries1, m1);
        System.out.println(result1);  // Output: [3, 3, 3, 6, 5]

        // Example 2
        int[] queries2 = {1, 6, 3, 6};
        int m2 = 6;
        List<Integer> result2 = solution.processQueries(queries2, m2);
        System.out.println(result2);  // Output: [0, 5, 3, 1]

        // Example 3
        int[] queries3 = {5, 1, 2, 4, 1};
        int m3 = 5;
        List<Integer> result3 = solution.processQueries(queries3, m3);
        System.out.println(result3);  // Output: [4, 1, 2, 4, 2]
    }
}

