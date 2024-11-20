package com.example.interviewdatastructures.arraybased;

public class KadaneAlgorithm {
    public static void main(String[] args) {
        int[] arr = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        System.out.println("Maximum Subarray Sum: " + maxSubArraySum(arr)); // Output: 6
    }

    private static int[] maxSubArraySum(int[] arr) {
        int maxSum = Integer.MIN_VALUE;
        int maxEndingHere = 0;
        int start = 0, tempStart = 0, end =0;
        for (int i = 0; i < arr.length; i++) {
            maxEndingHere += arr[i];

            if(maxSum < maxEndingHere){
                maxSum = maxEndingHere;
                start = tempStart;
                end = i;
            }
            if(maxEndingHere < 0){
                maxEndingHere = 0;
                tempStart = i + 1;
            }
        }
        return new int[] {start, end};
    }
}
