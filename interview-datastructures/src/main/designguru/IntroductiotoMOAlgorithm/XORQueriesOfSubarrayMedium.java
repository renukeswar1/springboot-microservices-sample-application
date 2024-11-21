package designguru.IntroductiotoMOAlgorithm;

import java.util.Arrays;

public class XORQueriesOfSubarrayMedium {
    public int[] xorQueries(int[] arr, int[][] queries) {
        int n = arr.length;
        int q = queries.length;
        int blockSize = (int) Math.sqrt(n); // Size of each block for Mo's algorithm
        int[] answer = new int[q];

        // Create an array of Query objects to hold the queries with their indices
        Query[] qs = new Query[q];
        for (int i = 0; i < q; i++) {
            qs[i] = new Query(queries[i][0], queries[i][1], i);
        }

        // Sort the queries according to the block and then by the right index
        Arrays.sort(qs, (a, b) -> {
            int blockA = a.left / blockSize;
            int blockB = b.left / blockSize;
            if (blockA != blockB) return blockA - blockB; // Sort by block
            return a.right - b.right; // Sort by right index if in the same block
        });

        int currentLeft = 0, currentRight = -1, currentXor = 0; // Initialize pointers and current XOR

        // Process each query in the sorted order
        for (Query query : qs) {
            // Expand the right boundary to include new elements in the XOR calculation
            while (currentRight < query.right) {
                currentRight++;
                currentXor ^= arr[currentRight];
            }
            // Shrink the right boundary to exclude elements from the XOR calculation
            while (currentRight > query.right) {
                currentXor ^= arr[currentRight];
                currentRight--;
            }
            // Expand the left boundary to exclude elements from the XOR calculation
            while (currentLeft < query.left) {
                currentXor ^= arr[currentLeft];
                currentLeft++;
            }
            // Shrink the left boundary to include new elements in the XOR calculation
            while (currentLeft > query.left) {
                currentLeft--;
                currentXor ^= arr[currentLeft];
            }

            answer[query.index] = currentXor;
        }

        return answer;
    }

    // Main method to test the xorQueries method with provided examples
    public static void main(String[] args) {
        XORQueriesOfSubarrayMedium solution = new XORQueriesOfSubarrayMedium();

        // Example 1
        int[] arr1 = {2, 4, 7, 3};
        int[][] queries1 = {{0, 1}, {1, 3}, {0, 2}};
        int[] result1 = solution.xorQueries(arr1, queries1);
        System.out.println("Example 1 Output: " + Arrays.toString(result1)); // Expected Output: [6, 0, 1]

        // Example 2
        int[] arr2 = {5, 9, 12, 6};
        int[][] queries2 = {{2, 3}, {0, 2}, {1, 2}};
        int[] result2 = solution.xorQueries(arr2, queries2);
        System.out.println("Example 2 Output: " + Arrays.toString(result2)); // Expected Output: [10, 0, 5]

        // Example 3
        int[] arr3 = {1, 3, 5, 7, 9};
        int[][] queries3 = {{1, 4}, {0, 3}, {2, 4}};
        int[] result3 = solution.xorQueries(arr3, queries3);
        System.out.println("Example 3 Output: " + Arrays.toString(result3)); // Expected Output: [8, 0, 11]
    }


}
class Query {
    int left, right, index;
    Query(int l, int r, int i) {
        left = l;
        right = r;
        index = i;
    }
}
