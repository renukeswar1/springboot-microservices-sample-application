package designguru.segmentTreePattern;

import java.util.*;

public class RectangleAreaII {
    public int rectangleArea(int[][] rectangles) {
        // Define constants for event types
        int OPEN = 1, CLOSE = -1;

        // Initialize arrays to hold events and x-coordinates
        int[][] events = new int[rectangles.length * 2][];
        Set<Integer> xCoordinates = new HashSet<>();
        int index = 0;

        // Loop through each rectangle to create events and collect x-coordinates
        for (int[] rectangle : rectangles) {
            // Ensure the rectangle is valid (non-degenerate)
            if ((rectangle[0] < rectangle[2]) && (rectangle[1] < rectangle[3])) {
                // Add opening event for the bottom edge of the rectangle
                events[index++] = new int[]{rectangle[1], OPEN, rectangle[0], rectangle[2]};
                // Add closing event for the top edge of the rectangle
                events[index++] = new int[]{rectangle[3], CLOSE, rectangle[0], rectangle[2]};
                // Add x-coordinates to the set
                xCoordinates.add(rectangle[0]);
                xCoordinates.add(rectangle[2]);
            }
        }

        // Sort events by y-coordinate to process them from bottom to top
        Arrays.sort(events, 0, index, (a, b) -> Integer.compare(a[0], b[0]));

        // Convert x-coordinates to an array and sort them
        Integer[] xArray = xCoordinates.toArray(new Integer[0]);
        Arrays.sort(xArray);

        // Create a mapping from x-coordinate to its index in the sorted array
        Map<Integer, Integer> xIndexMap = new HashMap<>();
        for (int i = 0; i < xArray.length; ++i)
            xIndexMap.put(xArray[i], i);

        // Initialize the segment tree to manage active intervals
        SegmentTree segmentTree = new SegmentTree(0, xArray.length - 1, xArray);
        long totalArea = 0;
        long currentXSum = 0;
        int currentY = events[0][0];

        // Process all events to calculate the total area
        for (int[] event : events) {
            if (event == null) break; // Stop if there are no more events
            int y = event[0], eventType = event[1], x1 = event[2], x2 = event[3];
            // Update the total area using the length of active intervals
            totalArea += currentXSum * (y - currentY);
            // Update the active intervals in the segment tree
            currentXSum = segmentTree.update(xIndexMap.get(x1), xIndexMap.get(x2), eventType);
            // Move to the next y-coordinate
            currentY = y;
        }

        // Return the total area modulo 10^9 + 7
        totalArea %= 1_000_000_007;
        return (int) totalArea;
    }
}

// Segment tree to manage active intervals
class SegmentTree {
    int start, end;
    Integer[] xArray;
    SegmentTree left, right;
    int count;
    long total;

    // Constructor to initialize the segment tree
    public SegmentTree(int start, int end, Integer[] xArray) {
        this.start = start;
        this.end = end;
        this.xArray = xArray;
        this.left = null;
        this.right = null;
        this.count = 0;
        this.total = 0;
    }

    // Calculate the midpoint of the current range
    public int getRangeMid() {
        return start + (end - start) / 2;
    }

    // Get the left child of the segment tree
    public SegmentTree getLeft() {
        if (left == null) left = new SegmentTree(start, getRangeMid(), xArray);
        return left;
    }

    // Get the right child of the segment tree
    public SegmentTree getRight() {
        if (right == null) right = new SegmentTree(getRangeMid(), end, xArray);
        return right;
    }

    // Update the segment tree with new intervals and calculate the total length
    public long update(int i, int j, int val) {
        if (i >= j) return 0; // No range to update
        if (start == i && end == j) {
            count += val; // Update the count of the interval
        } else {
            // Update the left and right children
            getLeft().update(i, Math.min(getRangeMid(), j), val);
            getRight().update(Math.max(getRangeMid(), i), j, val);
        }

        // Calculate the total length of the active intervals
        if (count > 0) total = xArray[end] - xArray[start];
        else total = getLeft().total + getRight().total;

        return total;
    }
}
