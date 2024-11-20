package com.example.interviewdatastructures.arraybased;

public class RearrangeArrayAlternatively {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7};
        rearrange(arr);

        System.out.println("Rearranged Array:");
        for (int num : arr) {
            System.out.print(num + " ");
        }
    }

    private static void rearrange(int[] arr) {
        int n = arr.length;
        int maxNum = arr[n - 1]+1;
        int minId = 0;
        int maxId = n-1;

        for(int i=0;i<n;i++) {
            if(i%2 == 0){
                arr[i] += (arr[maxId--] % maxNum) * maxNum;
            }else{
                arr[i] += (arr[minId++] % maxNum) * maxNum;
            }
        }
        for (int i = 0; i < n; i++) {
            System.out.println("Division" + arr[i] / maxNum);
            System.out.println("Modulo" + arr[i] % maxNum);
        }
        for (int i = 0; i < n; i++) {
            arr[i] = arr[i] / maxNum;
        }

    }
}
