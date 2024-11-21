package designguru.SimulationPattern;

public class SpiralMatrixIII {
    public int[][] spiralMatrixIII(int rows, int cols, int rStart, int cStart) {
        int[][] result = new int[rows * cols][2];
        int dx = 0, dy = 1; // Initial direction: right
        int numVisited = 0; // Number of cells visited
        int directionChange = 0; // Number of direction changes
        int temp; // Temporary variable for swapping directions

        for (int steps = 0; numVisited < rows * cols; ++directionChange) {
            for (int step = 0; step < directionChange / 2 + 1; ++step) {
                // Check if the current cell is within grid boundaries
                if (0 <= rStart && rStart < rows && 0 <= cStart && cStart < cols)
                    result[numVisited++] = new int[] {rStart, cStart};

                // Move to the next cell in the current direction
                rStart += dx;
                cStart += dy;
            }
            // Change direction: right -> down -> left -> up
            temp = dx;
            dx = dy;
            dy = -temp;
        }
        return result;
    }

    public static void main(String[] args) {
        SpiralMatrixIII solution = new SpiralMatrixIII();
        int[][] result1 = solution.spiralMatrixIII(4, 4, 2, 2);
        printResult(result1);
        int[][] result2 = solution.spiralMatrixIII(3, 3, 0, 0);
        printResult(result2);
        int[][] result3 = solution.spiralMatrixIII(5, 5, 4, 4);
        printResult(result3);
    }

    private static void printResult(int[][] result) {
        for (int[] cell : result) {
            System.out.print("[" + cell[0] + "," + cell[1] + "] ");
        }
        System.out.println();
    }
}
