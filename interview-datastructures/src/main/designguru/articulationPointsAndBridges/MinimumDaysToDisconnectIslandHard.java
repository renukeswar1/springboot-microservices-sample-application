package designguru.articulationPointsAndBridges;

import java.util.Arrays;

public class MinimumDaysToDisconnectIslandHard {
    // Directions to check adjacent cells
    private static final int[][] DIRECTIONS = {
            {0, 1},
            {1, 0},
            {0, -1},
            {-1, 0}
    };

    public int minDays(int[][] grid) {
        int numRows = grid.length, numCols = grid[0].length;
        IslandSeparationInfo separationInfo = new IslandSeparationInfo(false, 0); // To keep track of articulation points
        int totalLandCells = 0, totalIslands = 0;

        // Arrays to store discovery time, the lowest reachable time, and the parent cell in DFS
        int[][] discoveryTime = new int[numRows][numCols];
        int[][] lowestTimeReachable = new int[numRows][numCols];
        int[][] parentCell = new int[numRows][numCols];

        // Initialize arrays with default values
        for (int i = 0; i < numRows; i++) {
            Arrays.fill(discoveryTime[i], -1); // Set discovery time as not visited
            Arrays.fill(lowestTimeReachable[i], -1); // Set the lowest reachable time
            Arrays.fill(parentCell[i], -1); // Set parent of each cell to be undefined
        }

        // Loop through the grid to find all land cells and discover islands
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                if (grid[row][col] == 1) { // If it's a land cell
                    totalLandCells++; // Count total land cells
                    if (discoveryTime[row][col] == -1) { // If this cell is not visited
                        // Perform DFS to find articulation points and islands
                        findCriticalPoints(
                                grid, row, col, discoveryTime,
                                lowestTimeReachable, parentCell, separationInfo
                        );
                        totalIslands++; // Count the number of islands
                    }
                }
            }
        }

        // Determine the minimum number of days required to disconnect the grid
        if (totalIslands == 0 || totalIslands >= 2) return 0; // Already disconnected or no land
        if (totalLandCells == 1) return 1; // Only one land cell present
        if (separationInfo.hasCriticalPoint) return 1; // At least one critical point found
        return 2; // No critical point, need to remove any two land cells
    }

    private void findCriticalPoints(
            int[][] grid, int row, int col,
            int[][] discoveryTime, int[][] lowestTimeReachable,
            int[][] parentCell, IslandSeparationInfo separationInfo
    ) {
        int numRows = grid.length, numCols = grid[0].length;
        discoveryTime[row][col] = separationInfo.discoveryTime; // Set discovery time
        separationInfo.discoveryTime++; // Increment the time for the next cell
        lowestTimeReachable[row][col] = discoveryTime[row][col]; // Initially, lowest reachable is the discovery time itself
        int childCount = 0; // Count children in DFS tree

        // Explore all four directions
        for (int[] direction : DIRECTIONS) {
            int newRow = row + direction[0]; // New row position
            int newCol = col + direction[1]; // New column position
            if (isValidLand(grid, newRow, newCol)) { // Check if the new cell is valid
                if (discoveryTime[newRow][newCol] == -1) { // If the new cell is not yet visited
                    childCount++; // Increment child count
                    parentCell[newRow][newCol] = row * numCols + col; // Set parent for the new cell
                    findCriticalPoints(grid, newRow, newCol, discoveryTime, lowestTimeReachable, parentCell, separationInfo);

                    // Update the lowest reachable time based on the child's lowest reachable time
                    lowestTimeReachable[row][col] = Math.min(lowestTimeReachable[row][col], lowestTimeReachable[newRow][newCol]);

                    // Check if the current cell is a critical point
                    if (lowestTimeReachable[newRow][newCol] >= discoveryTime[row][col] && parentCell[row][col] != -1) {
                        separationInfo.hasCriticalPoint = true; // Mark that a critical point exists
                    }
                } else if (newRow * numCols + newCol != parentCell[row][col]) {
                    // Update the lowest reachable time considering a back edge
                    lowestTimeReachable[row][col] = Math.min(lowestTimeReachable[row][col], discoveryTime[newRow][newCol]);
                }
            }
        }

        // Root of DFS tree is a critical point if it has more than one child
        if (parentCell[row][col] == -1 && childCount > 1) {
            separationInfo.hasCriticalPoint = true;
        }
    }

    // Check if the cell is a valid land cell
    private boolean isValidLand(int[][] grid, int row, int col) {
        int numRows = grid.length, numCols = grid[0].length;
        return (row >= 0 && col >= 0 && row < numRows && col < numCols && grid[row][col] == 1);
    }

    // Helper class to store information about grid separation
    private class IslandSeparationInfo {
        boolean hasCriticalPoint; // To indicate if there is any critical point
        int discoveryTime; // To keep track of discovery time

        IslandSeparationInfo(boolean hasCriticalPoint, int discoveryTime) {
            this.hasCriticalPoint = hasCriticalPoint;
            this.discoveryTime = discoveryTime;
        }
    }

    public static void main(String[] args) {
        MinimumDaysToDisconnectIslandHard solution = new MinimumDaysToDisconnectIslandHard();

        // Example 1
        int[][] grid1 = {
                {1, 1, 0},
                {1, 1, 0},
                {0, 1, 1}
        };
        System.out.println("Output: " + solution.minDays(grid1)); // Expected Output: 1

        // Example 2
        int[][] grid2 = {
                {1, 0, 0, 1},
                {0, 1, 1, 0},
                {1, 0, 0, 1}
        };
        System.out.println("Output: " + solution.minDays(grid2)); // Expected Output: 0

        // Example 3
        int[][] grid3 = {
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 1, 1, 1}
        };
        System.out.println("Output: " + solution.minDays(grid3)); // Expected Output: 2
    }
}
