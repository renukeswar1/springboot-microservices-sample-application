package designguru.SimulationPattern;

public class WaterBottles {
    public int getMaxBottles(int numBottles, int numExchange) {
        int totalDrunkBottles = 0;  // This will keep track of the total number of bottles drunk

        while (numBottles >= numExchange) {
            // Drink as many bottles as we can exchange in this round
            totalDrunkBottles += numExchange;
            numBottles -= numExchange;

            // Exchange the empty bottles for one full bottle
            numBottles++;
        }

        // Add the remaining bottles that are less than the exchange rate
        return totalDrunkBottles + numBottles;
    }

    public static void main(String[] args) {
        WaterBottles solution = new WaterBottles();

        // Test cases
        System.out.println(solution.getMaxBottles(15, 4));  // Output: 19
        System.out.println(solution.getMaxBottles(7, 3));   // Output: 10
        System.out.println(solution.getMaxBottles(5, 5));   // Output: 6
    }
}
