package designguru.segmentTreePattern;

public class RangeMinimumQuery {
    int rangeMinQuery(int[] segmentTree, int segmentStart, int segmentEnd, int queryStart, int queryEnd, int index) {
        // If the segment of this node is a part of the given range
        if (queryStart <= segmentStart && queryEnd >= segmentEnd)
            return segmentTree[index];

        // If the segment of this node is outside the given range
        if (segmentEnd < queryStart || segmentStart > queryEnd)
            return Integer.MAX_VALUE;

        // If a part of this segment overlaps with the given range
        int mid = segmentStart + (segmentEnd - segmentStart) / 2;
        return Math.min(rangeMinQuery(segmentTree, segmentStart, mid, queryStart, queryEnd, 2 * index + 1),
                rangeMinQuery(segmentTree, mid + 1, segmentEnd, queryStart, queryEnd, 2 * index + 2));
    }

    // Return minimum of elements in range from index queryStart to queryEnd
    int[] rangeMinQueryAll(int[] segmentTree, int n, int[][] queries) {
        int[] results = new int[queries.length];

        for (int i = 0; i < queries.length; i++) {
            int queryStart = queries[i][0];
            int queryEnd = queries[i][1];

            results[i] = rangeMinQuery(segmentTree, 0, n - 1, queryStart, queryEnd, 0);
        }

        return results;
    }

    // Recursive function that constructs Segment Tree for array[segmentStart..segmentEnd]
    int constructSegmentTree(int[] nums, int segmentStart, int segmentEnd, int[] segmentTree, int index) {
        // If there is one element in the array, store it in the current node of the segment tree and return
        if (segmentStart == segmentEnd) {
            segmentTree[index] = nums[segmentStart];
            return nums[segmentStart];
        }

        // If there are more than one elements, then recur for left and right subtrees
        int mid = segmentStart + (segmentEnd - segmentStart) / 2;
        segmentTree[index] = Math.min(constructSegmentTree(nums, segmentStart, mid, segmentTree, index * 2 + 1),
                constructSegmentTree(nums, mid + 1, segmentEnd, segmentTree, index * 2 + 2));
        return segmentTree[index];
    }

    // Function to construct segment tree from given array
    int[] constructSegmentTree(int[] nums, int n) {
        // Height of segment tree
        int height = (int) (Math.ceil(Math.log(n) / Math.log(2)));

        // Maximum size of segment tree
        int maxSize = 2 * (int) Math.pow(2, height) - 1;
        int[] segmentTree = new int[maxSize];

        // Fill the allocated memory segmentTree
        constructSegmentTree(nums, 0, n - 1, segmentTree, 0);

        // Return the constructed segment tree
        return segmentTree;
    }

    // Wrapper method to construct segment tree and process queries
    int[] processQueries(int[] nums, int[][] queries) {
        int[] segmentTree = constructSegmentTree(nums, nums.length);
        return rangeMinQueryAll(segmentTree, nums.length, queries);
    }

    public static void main(String[] args) {
        RangeMinimumQuery sol = new RangeMinimumQuery();

        int[] nums1 = {2, 6, 1, 12, 9, 5, 3, 7};
        int[][] queries1 = {{0, 3}, {2, 5}, {0, 1}, {3, 7}, {0, 7}, {4, 6}, {4, 5}};
        int[] results1 = sol.processQueries(nums1, queries1);

        System.out.println("Example 1: ");
        for (int i = 0; i < queries1.length; i++) {
            System.out.println("Range [" + queries1[i][0] + ", " + queries1[i][1] + "] -> Minimum: " + results1[i]);
        }

        int[] nums2 = {4, 8, 6, 2, 10, 15};
        int[][] queries2 = {{1, 3}, {0, 2}, {2, 4}, {3, 5}};
        int[] results2 = sol.processQueries(nums2, queries2);

        System.out.println("Example 2: ");
        for (int i = 0; i < queries2.length; i++) {
            System.out.println("Range [" + queries2[i][0] + ", " + queries2[i][1] + "] -> Minimum: " + results2[i]);
        }

        int[] nums3 = {11, 13, 5, 8, 10, 12, 7, 14};
        int[][] queries3 = {{0, 4}, {1, 3}, {2, 5}, {3, 6}};
        int[] results3 = sol.processQueries(nums3, queries3);

        System.out.println("Example 3: ");
        for (int i = 0; i < queries3.length; i++) {
            System.out.println("Range [" + queries3[i][0] + ", " + queries3[i][1] + "] -> Minimum: " + results3[i]);
        }
    }
}
