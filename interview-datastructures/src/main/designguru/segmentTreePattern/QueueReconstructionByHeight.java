package designguru.segmentTreePattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QueueReconstructionByHeight {
    private static void buildSegmentTree(int[] segmentTree, int index, int left, int right) {
        // If it's a leaf node
        if (left == right) {
            segmentTree[index] = 1; // One vacant spot
            return;
        }

        int mid = left + (right - left) / 2;
        buildSegmentTree(segmentTree, 2 * index + 1, left, mid);
        buildSegmentTree(segmentTree, 2 * index + 2, mid + 1, right);

        // Number of vacant spots in the segment
        segmentTree[index] = segmentTree[2 * index + 1] + segmentTree[2 * index + 2];
    }

    // Query to place the person at the correct position
    private static void query(int[] segmentTree, List<int[]> resultQueue, int index, int left, int right, int position, int height, int originalPosition) {
        // If it's a leaf node
        if (left == right) {
            resultQueue.set(left, new int[]{height, originalPosition});
            segmentTree[index]--;
            return;
        }

        // If segment can't accommodate the position
        if (segmentTree[index] < position) return;

        int mid = left + (right - left) / 2;

        // If the left subtree can accommodate the position
        if (segmentTree[2 * index + 1] >= position) {
            query(segmentTree, resultQueue, 2 * index + 1, left, mid, position, height, originalPosition);
        } else {
            // If the right subtree needs to be considered
            query(segmentTree, resultQueue, 2 * index + 2, mid + 1, right, position - segmentTree[2 * index + 1], height, originalPosition);
        }

        // Reduce the number of vacant spots
        segmentTree[index]--;
    }

    // Reconstruct the queue
    public int[][] reconstructQueue(int[][] people) {
        int n = people.length;
        int[] segmentTree = new int[4 * n];
        List<int[]> resultQueue = new ArrayList<>(Collections.nCopies(n, null));

        // Sort people using custom comparator
        Arrays.sort(people, (a, b) -> {
            if (a[0] == b[0]) {
                return Integer.compare(b[1], a[1]);
            }
            return Integer.compare(a[0], b[0]);
        });

        // Build the segment tree
        buildSegmentTree(segmentTree, 0, 0, n - 1);

        // Place each person in the correct position
        for (int[] person : people) {
            query(segmentTree, resultQueue, 0, 0, n - 1, person[1] + 1, person[0], person[1]);
        }

        // Convert resultQueue to 2D array
        return resultQueue.toArray(new int[n][2]);
    }

    // Main method to test the algorithm with examples
    public static void main(String[] args) {
        QueueReconstructionByHeight solution = new QueueReconstructionByHeight();

        int[][] example1 = {{6, 0}, {5, 1}, {5, 0}, {7, 0}, {7, 1}, {6, 2}};
        int[][] example2 = {{4, 0}, {5, 1}, {7, 0}, {6, 1}};
        int[][] example3 = {{7, 0}, {6, 1}, {5, 2}, {6, 0}, {4, 4}, {5, 1}};

        int[][] result1 = solution.reconstructQueue(example1);
        int[][] result2 = solution.reconstructQueue(example2);
        int[][] result3 = solution.reconstructQueue(example3);

        System.out.println("Example 1: " + Arrays.deepToString(result1));
        System.out.println("Example 2: " + Arrays.deepToString(result2));
        System.out.println("Example 3: " + Arrays.deepToString(result3));
    }
}
