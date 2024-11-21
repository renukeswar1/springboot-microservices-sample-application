package designguru.CountingPattern;

public class MaximumPopulationYear {
    public int maximumPopulation(int[][] logs) {
        int[] population = new int[101]; // Array to store population changes
        for (int[] log : logs) {
            population[log[0] - 1950]++; // Increment population at birth year
            population[log[1] - 1950]--; // Decrement population at death year
        }

        int maxPop = 0;
        int maxYear = 1950;
        int currentPop = 0;

        // Calculate maximum population year
        for (int year = 0; year < 101; year++) {
            currentPop += population[year];
            if (currentPop > maxPop) {
                maxPop = currentPop;
                maxYear = 1950 + year;
            }
        }

        return maxYear;
    }

    public static void main(String[] args) {
        MaximumPopulationYear sol = new MaximumPopulationYear();
        int[][] logs1 = {{1980, 1990}, {1975, 1985}, {1985, 1995}, {1990, 2000}, {1999, 2020}, {1994, 2032}};
        int[][] logs2 = {{1970, 1990}, {1980, 2009}, {1960, 1970}, {1959, 1982}};
        int[][] logs3 = {{2000, 2010}, {2005, 2015}, {2010, 2020}, {2015, 2025}};

        System.out.println(sol.maximumPopulation(logs1)); // Output: 1994
        System.out.println(sol.maximumPopulation(logs2)); // Output: 1980
        System.out.println(sol.maximumPopulation(logs3)); // Output: 2005
    }
}
