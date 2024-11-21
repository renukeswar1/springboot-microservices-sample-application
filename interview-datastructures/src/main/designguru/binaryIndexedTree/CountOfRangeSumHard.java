package designguru.binaryIndexedTree;

import java.util.Arrays;

public class CountOfRangeSumHard {
    public int countRangeSum(int[] nums, int lower, int upper) {
        int n = nums.length, ans = 0; // Initialize length of nums array and result variable
        long[] pre = new long[n+1];

        // Calculate the prefix sums for the array
        for (int i = 0; i < n; i++){
            pre[i+1] = nums[i] + pre[i];
        }

        Arrays.sort(pre); // Sort the prefix sum array for binary search
        int[] bit = new int[pre.length+2];
        long sum = 0;

        // Iterate through each element in nums to calculate valid range sums
        for (int i = 0; i < n; i++){
            update(bit, bs(sum, pre), 1); // Update the BIT with the current cumulative sum
            sum += nums[i];
            // Count the number of valid range sums that lie between lower and upper
            ans += sum(bit, bs(sum-lower, pre)) - sum(bit, bs(sum-upper-1, pre));
        }
        return ans;
    }

    // Binary search function to find the index of the first number greater than the given sum
    private int bs(long sum, long[] pre){
        int lo = 0, hi = pre.length; // Set initial low and high pointers for binary search
        while(lo < hi){
            int mid = (lo+hi) >> 1; // Calculate mid index
            if (pre[mid] > sum){
                hi = mid; // Move high pointer to mid
            }else{
                lo = mid + 1; // Move low pointer to mid + 1
            }
        }
        return lo;
    }

    // Function to update the BIT
    private void update(int[] bit, int idx, int inc){
        for (++idx; idx < bit.length; idx += idx & -idx){ // Update all relevant indices in the BIT
            bit[idx] += inc; // Increment the BIT value at the current index
        }
    }

    private int sum(int[] bit, int idx){
        int ans = 0;
        for (++idx; idx > 0; idx -= idx & -idx){ // Iterate through the BIT to sum up values
            ans += bit[idx]; // Add the BIT value at the current index to the result
        }
        return ans;
    }
    public static void main(String[] args) {
        CountOfRangeSumHard solution = new CountOfRangeSumHard();

        // Example 1
        int[] nums1 = {2, -1, 2, -3, 4};
        int lower1 = 0, upper1 = 2;
        System.out.println("Example 1 Output: " + solution.countRangeSum(nums1, lower1, upper1));

        // Example 2
        int[] nums2 = {0, 3, -2, 1, -4, 2};
        int lower2 = -2, upper2 = 2;
        System.out.println("Example 2 Output: " + solution.countRangeSum(nums2, lower2, upper2));

        // Example 3
        int[] nums3 = {1, -1, 1, -1, 1};
        int lower3 = 0, upper3 = 1;
        System.out.println("Example 3 Output: " + solution.countRangeSum(nums3, lower3, upper3));
    }
}
