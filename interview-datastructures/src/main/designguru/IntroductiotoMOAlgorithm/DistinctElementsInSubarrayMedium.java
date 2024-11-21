package designguru.IntroductiotoMOAlgorithm;

import java.util.Arrays;

public class DistinctElementsInSubarrayMedium {
    static class Query {
        int left, right, index;

        // Constructor to initialize query details
        Query(int left, int right, int index) {
            this.left = left;
            this.right = right;
            this.index = index;
        }
    }

    public int[] distinctElements(int[] nums, int[][] queries) {
        int n = nums.length;
        int q = queries.length;
        int[] answer = new int[q];
        int sqrtN = (int) Math.sqrt(n); // Determine the block size for Mo's algorithm

        Query[] queryArr = new Query[q];
        for (int i = 0; i < q; i++) {
            queryArr[i] = new Query(queries[i][0], queries[i][1], i);
        }

        // Sort queries by the block of `left` and then by `right` within each block
        Arrays.sort(queryArr, (a, b) -> {
            if (a.left / sqrtN != b.left / sqrtN) {
                return a.left / sqrtN - b.left / sqrtN;
            } else {
                return a.right - b.right;
            }
        });

        int[] freq = new int[100001]; // Frequency array for tracking element counts
        int currentLeft = 0, currentRight = 0, currentDistinctCount = 0;

        for (Query query : queryArr) {
            int left = query.left;
            int right = query.right;

            // Expand the current right boundary
            while (currentRight <= right) {
                if (freq[nums[currentRight]] == 0) currentDistinctCount++;
                freq[nums[currentRight]]++;
                currentRight++;
            }

            // Shrink the current right boundary
            while (currentRight > right + 1) {
                currentRight--;
                if (freq[nums[currentRight]] == 1) currentDistinctCount--;
                freq[nums[currentRight]]--;
            }

            // Expand the current left boundary
            while (currentLeft > left) {
                currentLeft--;
                if (freq[nums[currentLeft]] == 0) currentDistinctCount++;
                freq[nums[currentLeft]]++;
            }

            // Shrink the current left boundary
            while (currentLeft < left) {
                if (freq[nums[currentLeft]] == 1) currentDistinctCount--;
                freq[nums[currentLeft]]--;
                currentLeft++;
            }

            // Store the result for this query
            answer[query.index] = currentDistinctCount;
        }

        return answer;
    }

    public static void main(String[] args) {
        DistinctElementsInSubarrayMedium solution = new DistinctElementsInSubarrayMedium();

        // Example 1
        int[] nums1 = {2, 1, 2, 3, 4};
        int[][] queries1 = {{0, 2}, {1, 3}, {2, 4}};
        System.out.println(Arrays.toString(solution.distinctElements(nums1, queries1))); // [2, 3, 3]

        // Example 2
        int[] nums2 = {5, 3, 5, 7, 5, 3, 8};
        int[][] queries2 = {{0, 5}, {2, 4}, {3, 6}};
        System.out.println(Arrays.toString(solution.distinctElements(nums2, queries2))); // [3, 2, 4]

        // Example 3
        int[] nums3 = {9, 9, 8, 8, 7, 6, 6, 5};
        int[][] queries3 = {{0, 3}, {2, 5}, {4, 7}};
        System.out.println(Arrays.toString(solution.distinctElements(nums3, queries3))); // [2, 3, 3]
    }
}
