package com.example.interviewdatastructures.arraybased;

import java.util.*;

public class PairSum {
    public static void main(String[] args) {
        int[] arr = {2, 7, 4, -1, 5, 1};
        int target = 6;
        List<int[]> pairs = findPairs(arr, target);
        for (int[] pair : pairs) {
            System.out.println(Arrays.toString(pair));
        }
    }

    private static List<int[]> findPairs(int[] arr, int target) {
        Map<Integer,Integer> pairs = new HashMap<>();
        List<int[]> result = new ArrayList<>();
        for(int num: arr){
            int complement = target - num;
            if(pairs.containsKey(complement)){
                result.add(new int[]{complement,num});
            }
            pairs.put(num,pairs.getOrDefault(num,0)+1);
        }
        return result;
    }
}
