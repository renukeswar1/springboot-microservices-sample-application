package com.example.interviewdatastructures.stringmanipulation;

public class LongestPalindromicSubstring {

    public static String longestPalindrome(String s) {
        if (s == null || s.length() < 1) return "";

        int start = 0, end = 0;

        // Iterate over all possible centers
        for (int i = 0; i < s.length(); i++) {
            // Expand around single character center (odd-length palindrome)
            int len1 = expandAroundCenter(s, i, i);

            // Expand around two-character center (even-length palindrome)
            int len2 = expandAroundCenter(s, i, i + 1);

            // Take the maximum length from the two cases
            int len = Math.max(len1, len2);

            // Update the start and end indices if a longer palindrome is found
            if (len > (end - start)) {
                start = i - (len - 1) / 2;
                end = i + len / 2;
            }
        }

        // Return the longest palindrome substring
        return s.substring(start, end + 1);
    }

    // Helper method to expand around a given center
    private static int expandAroundCenter(String s, int left, int right) {
        // Expand outward as long as characters match and indices are within bounds
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }

        // Return the length of the palindrome found
        return right - left - 1; // Subtract 1 because the last mismatch broke the loop
    }

    public static void main(String[] args) {
        String s = "babad";
        System.out.println("Longest Palindromic Substring: " + longestPalindrome(s));

        s = "cbbd";
        System.out.println("Longest Palindromic Substring: " + longestPalindrome(s));

        s = "a";
        System.out.println("Longest Palindromic Substring: " + longestPalindrome(s));

        s = "ac";
        System.out.println("Longest Palindromic Substring: " + longestPalindrome(s));
    }
}
