package com.example.interviewdatastructures.arraybased;

public class RotateArrayByKElements {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7};
        int k = 2;

        System.out.println("Original Array: ");
        for (int num : arr) {
            System.out.print(num + " ");
        }
        System.out.println();

        rotateLeft(arr, k);

        System.out.println("Rotated Array: ");
        for (int num : arr) {
            System.out.print(num + " ");
        }
    }

    private static void rotateLeft(int[] arr, int k) {
        int n = arr.length;
         k = k % n;
         rotate(arr, 0, k-1);
         rotate(arr, k, n-1);
         rotate(arr, 0, n-1);
    }

    private static void rotate(int[] arr, int start, int end) {
        while (start < end) {
            int temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            start++;
            end--;
        }
    }
}
