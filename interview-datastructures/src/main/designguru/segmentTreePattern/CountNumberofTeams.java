package designguru.segmentTreePattern;

import java.util.Arrays;

public class CountNumberofTeams {
    public void update(int index, int value, int low, int high, int position, int[] segTree) {
        if (low == high) {  // Base case: Single element
            segTree[index] += value;  // Update the value at the leaf node
        } else {
            int mid = (low + high) / 2;  // Find the middle point
            // If position is in the left subtree
            if (position <= mid && position >= low)
                update(2 * index + 1, value, low, mid, position, segTree);
            else
                update(2 * index + 2, value, mid + 1, high, position, segTree);
            // Update the current node
            segTree[index] = segTree[2 * index + 1] + segTree[2 * index + 2];
        }
    }

    // Get the sum range in the segment tree
    private int sumRange(int left, int right, int low, int high, int index, int[] segTree) {
        if (low >= left && high <= right) {  // Total overlap
            return segTree[index];
        }
        if (right < low || high < left) {  // No overlap
            return 0;
        }
        int mid = (low + high) / 2;  // Partial overlap
        // Sum the ranges from both children
        return sumRange(left, right, low, mid, 2 * index + 1, segTree) +
                sumRange(left, right, mid + 1, high, 2 * index + 2, segTree);
    }

    // Main method to count the number of valid teams
    public int numTeams(int[] ratings) {
        int teamCount = 0;  // Initialize team count
        int maxRating = Arrays.stream(ratings).max().getAsInt() + 1;  // Find max rating
        int[] leftSegmentTree = new int[4 * maxRating];  // Segment tree for ratings to the left
        int[] rightSegmentTree = new int[4 * maxRating];  // Segment tree for ratings to the right

        // Update right segment tree with all ratings except the first
        for (int i = 1; i < ratings.length; i++) {
            update(0, 1, 0, maxRating - 1, ratings[i], rightSegmentTree);
        }
        // Update left segment tree with the first rating
        update(0, 1, 0, maxRating - 1, ratings[0], leftSegmentTree);

        // Iterate through ratings from the second element
        for (int i = 1; i < ratings.length; i++) {
            // Count teams with a lower rating on the left and a higher rating on the right
            int leftLessCount = sumRange(0, ratings[i] - 1, 0, maxRating - 1, 0, leftSegmentTree);
            int rightGreaterCount = sumRange(ratings[i] + 1, maxRating - 1, 0, maxRating - 1, 0, rightSegmentTree);
            teamCount += leftLessCount * rightGreaterCount;

            // Count teams with a higher rating on the left and a lower rating on the right
            int leftGreaterCount = sumRange(ratings[i] + 1, maxRating - 1, 0, maxRating - 1, 0, leftSegmentTree);
            int rightLessCount = sumRange(0, ratings[i] - 1, 0, maxRating - 1, 0, rightSegmentTree);
            teamCount += leftGreaterCount * rightLessCount;

            // Update the segment trees with the current rating
            update(0, 1, 0, maxRating - 1, ratings[i], leftSegmentTree);
            update(0, -1, 0, maxRating - 1, ratings[i], rightSegmentTree);
        }

        return teamCount;  // Return the total number of valid teams
    }

    // Main method to test the solution
    public static void main(String[] args) {
        CountNumberofTeams solution = new CountNumberofTeams();

        // Test case 1
        int[] rating1 = {3, 6, 2, 8, 1, 5};
        int result1 = solution.numTeams(rating1);
        System.out.println("Expected Output: 3, Actual Output: " + result1);

        // Test case 2
        int[] rating2 = {1, 5, 3, 2, 6, 4};
        int result2 = solution.numTeams(rating2);
        System.out.println("Expected Output: 6, Actual Output: " + result2);

        // Test case 3
        int[] rating3 = {10, 20, 30, 25, 15};
        int result3 = solution.numTeams(rating3);
        System.out.println("Expected Output: 3, Actual Output: " + result3);
    }
}
