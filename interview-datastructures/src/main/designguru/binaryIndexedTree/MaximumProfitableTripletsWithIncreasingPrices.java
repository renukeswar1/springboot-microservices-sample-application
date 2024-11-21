package designguru.binaryIndexedTree;

public class MaximumProfitableTripletsWithIncreasingPrices {
    public int maxProfit(int[] prices, int[] profits) {
        // Find the maximum price from the prices array
        int maxPrice = 0, n = prices.length;
        for (int price : prices) {
            maxPrice = Math.max(maxPrice, price);
        }

        // Initialize Binary Indexed Trees (BIT) for left and right max profits
        int[] bitForMaxLeft = new int[maxPrice + 1];
        int[] bitForMaxRight = new int[maxPrice + 1];

        // Array to store max profit for the left side of each price
        int[] maxLeftProfit = new int[n];

        // Calculate maxLeftProfit using BIT for each price
        for (int i = 0; i < n; i++) {
            maxLeftProfit[i] = getMaxProfit(bitForMaxLeft, prices[i] - 1);
            updateBIT(bitForMaxLeft, prices[i], profits[i]);
        }

        // Initialize the result as -1 indicating no valid triplet found yet
        int maxTotalProfit = -1;

        // Traverse the prices array from right to left to calculate the result
        for (int i = n - 1; i >= 0; i--) {
            int adjustedPrice = maxPrice - prices[i] + 1;
            int maxRightProfit = getMaxProfit(bitForMaxRight, adjustedPrice - 1);

            // Update maxTotalProfit if both maxLeftProfit and maxRightProfit are greater than 0
            if (maxLeftProfit[i] > 0 && maxRightProfit > 0) {
                maxTotalProfit = Math.max(maxTotalProfit, maxLeftProfit[i] + profits[i] + maxRightProfit);
            }
            updateBIT(bitForMaxRight, adjustedPrice, profits[i]);
        }

        return maxTotalProfit;
    }

    // Update BIT with the new profit value at the given price index
    private void updateBIT(int[] bit, int priceIndex, int profit) {
        for (int i = priceIndex; i > 0 && i < bit.length; i = i + (i & -i)) {
            bit[i] = Math.max(bit[i], profit);
        }
    }

    // Get the maximum profit value from BIT for a given price index
    private int getMaxProfit(int[] bit, int priceIndex) {
        int maxProfit = 0;
        for (int i = priceIndex; i > 0; i = i - (i & -i)) {
            maxProfit = Math.max(bit[i], maxProfit);
        }
        return maxProfit;
    }

    // Main method to test the examples
    public static void main(String[] args) {
        MaximumProfitableTripletsWithIncreasingPrices solution = new MaximumProfitableTripletsWithIncreasingPrices();

        // Example 1
        int[] prices1 = {3, 1, 4, 6, 2};
        int[] profits1 = {5, 2, 8, 10, 3};
        System.out.println("Example 1 Result: " + solution.maxProfit(prices1, profits1));

        // Example 2
        int[] prices2 = {2, 7, 1, 5, 8, 10};
        int[] profits2 = {3, 9, 1, 4, 7, 6};
        System.out.println("Example 2 Result: " + solution.maxProfit(prices2, profits2));

        // Example 3
        int[] prices3 = {9, 8, 7, 6, 5};
        int[] profits3 = {10, 9, 8, 7, 6};
        System.out.println("Example 3 Result: " + solution.maxProfit(prices3, profits3));
    }
}
