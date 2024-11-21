package designguru.binaryIndexedTree;

public class ImplementationOfBinaryIndexedTree {

    int[] bit;
    int n;

    // Constructor to initialize the BIT with the given array
    public ImplementationOfBinaryIndexedTree(int[] arr) {
        n = arr.length;
        bit = new int[n + 1];  // BIT is 1-indexed, so we use n+1 size

        // Build the BIT by updating it with each element of the array
        for (int i = 0; i < n; i++) {
            update(i, arr[i]);
        }
    }

    // Function to update the BIT with the value at a specific index
    private void update(int index, int value) {
        index++;  // Convert to 1-based index for the BIT

        // Update the BIT by adding 'value' to the appropriate indices
        while (index <= n) {
            bit[index] += value;  // Add the value to the current node
            index += index & -index;  // Move to the next node to update
        }
    }

    // Function to get the prefix sum up to a specific index
    public int getSum(int index) {
        int sum = 0;
        index++;  // Convert to 1-based index for the BIT

        // Traverse the BIT, summing the values until we reach the root
        while (index > 0) {
            sum += bit[index];  // Add the value at the current node
            index -= index & -index;  // Move to the parent node
        }

        return sum;  // Return the final computed sum
    }

    // Main method to demonstrate the functionality
    public static void main(String[] args) {
        int[] arr = {3, 2, -1, 6, 5, 4, -3, 3, 7, 2, 3};  // Example array
        ImplementationOfBinaryIndexedTree solution = new ImplementationOfBinaryIndexedTree(arr);

        // Get sum from index 0 to 10
        int sumBeforeUpdate = solution.getSum(10);
        System.out.println("Sum from 0 to 10 before update: " + sumBeforeUpdate);

        // Update element at index 3 (change value from -1 to 1)
        solution.update(3, 2);  // 2 is the difference (new value 1 - old value -1)

        // Recalculate the sum from index 0 to 10 after the update
        int sumAfterUpdate = solution.getSum(10);
        System.out.println("Sum from 0 to 10 after update: " + sumAfterUpdate);
    }
}
