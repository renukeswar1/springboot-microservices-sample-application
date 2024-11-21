package designguru.CountingPattern;

public class MinimumIncrementtoMakeArrayUnique {
    public int minIncrementForUnique(int[] nums) {
        int n = nums.length;
        int max = 0;
        int minMoves = 0;

        // Find the maximum value in the array
        for (int value : nums) {
            max = Math.max(max, value);
        }

        // Create a frequency array to count occurrences
        int[] frequency = new int[n + max];

        // Populate the frequency array
        for (int value : nums) {
            frequency[value]++;
        }

        // Adjust frequencies to make all values unique
        for (int i = 0; i < frequency.length; i++) {
            if (frequency[i] <= 1) continue; // Skip if frequency is 1 or less

            int excess = frequency[i] - 1; // Calculate excess duplicates
            frequency[i + 1] += excess;    // Move excess to the next value
            minMoves += excess;           // Count moves required
        }

        return minMoves; // Return total moves
    }

    public static void main(String[] args) {
        MinimumIncrementtoMakeArrayUnique solution = new MinimumIncrementtoMakeArrayUnique();
        System.out.println(solution.minIncrementForUnique(new int[]{4, 3, 2, 2, 1, 4})); // Output: 5
        System.out.println(solution.minIncrementForUnique(new int[]{5, 5, 5, 5, 5}));    // Output: 10
        System.out.println(solution.minIncrementForUnique(new int[]{1, 1, 1, 1, 2}));    // Output: 9
    }
}
