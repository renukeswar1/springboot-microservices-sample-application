package designguru.LinearSortingAlgorithms;

public class HeightChecker {
    public int heightChecker(int[] heights) {
        int max_height = 100;

        // Create a count array to keep track of the number of students with each height
        int[] count = new int[max_height + 1];

        // Populate the count array based on the heights in the input
        for (int height : heights) {
            count[height]++;
        }

        int result = 0;
        // Start checking from the smallest possible height
        int currentHeight = 0;

        // Iterate over each height in the input array
        for (int i = 0; i < heights.length; i++) {
            // Find the next height with a non-zero count (skip heights not in the array)
            while (count[currentHeight] == 0) {
                currentHeight++;
            }

            // If the current height in the input array does not match the expected sorted height
            if (heights[i] != currentHeight) {
                result++;
            }

            // Decrement the count of the current height (since we've used one occurrence of it)
            count[currentHeight]--;
        }

        return result;
    }

    public static void main(String[] args) {
        HeightChecker sol = new HeightChecker();

        System.out.println(sol.heightChecker(new int[]{5, 1, 2, 3, 4, 8, 1})); // Output: 3
        System.out.println(sol.heightChecker(new int[]{10, 6, 8, 5, 9})); // Output: 3
        System.out.println(sol.heightChecker(new int[]{7, 3, 2, 1, 4})); // Output: 5
    }
}
