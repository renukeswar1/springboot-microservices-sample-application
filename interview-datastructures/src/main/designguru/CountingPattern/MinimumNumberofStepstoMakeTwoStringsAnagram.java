package designguru.CountingPattern;

import java.util.HashMap;
import java.util.Map;

public class MinimumNumberofStepstoMakeTwoStringsAnagram {
    public int calculateSteps(String s, String t) {
        Map<Character, Integer> frequencyDifference = new HashMap<>();

        // Count the difference in frequencies of characters between t and s
        for (int i = 0; i < s.length(); i++) {
            char charT = t.charAt(i);
            char charS = s.charAt(i);

            frequencyDifference.put(charT, frequencyDifference.getOrDefault(charT, 0) + 1);
            frequencyDifference.put(charS, frequencyDifference.getOrDefault(charS, 0) - 1);
        }

        int steps = 0;
        // Calculate the number of steps needed to make t an anagram of s
        for (int count : frequencyDifference.values()) {
            // Add the positive difference of character counts to steps
            // This represents the number of characters to be replaced
            if (count > 0) {
                steps += count;
            }
        }

        return steps;
    }

    public static void main(String[] args) {
        MinimumNumberofStepstoMakeTwoStringsAnagram sol = new MinimumNumberofStepstoMakeTwoStringsAnagram();
        System.out.println(sol.calculateSteps("designgurus", "garumdespgn")); // Output: 4
        System.out.println(sol.calculateSteps("abc", "def")); // Output: 3
        System.out.println(sol.calculateSteps("listen", "silent")); // Output: 0
    }
}
