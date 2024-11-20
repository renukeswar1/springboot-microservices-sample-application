package com.example.interviewdatastructures.arraybased;

import java.util.Arrays;

public class RemoveDuplicatesFromSortedArray {
    public static void main(String[] args) {
        int[] arr = {1, 1, 2, 2, 3, 4, 4};
        int[] newLength = removeDuplicates(arr);

        System.out.println("New Length: " + newLength);
        System.out.print("Modified Array: ");
        for (int k = 0; k < newLength.length; k++) {
            System.out.print(arr[k] + " ");
        }
    }

    private static int[] removeDuplicates(int[] arr) {
        int j = 1;
        for (int i = 1; i < arr.length ; i++) {
            if (arr[i] != arr[i-1]) { // 1,1,2,2,3,4,5
                arr[j] = arr[i];
                j++;
            }
        }
        return Arrays.copyOfRange(arr, 0,j);
    }
}
