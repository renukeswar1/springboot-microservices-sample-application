package designguru.binaryIndexedTree;

import java.util.ArrayList;
import java.util.List;

public class MinimumNumberOfMovesToMakePalindromeHard {
    List<Integer>[] characterIndices;  // Array of lists to store indices of each character
    int[][] indexPointers;  // Pointers to track positions in character indices
    int[] BITree;  // Binary Indexed Tree for efficient calculation of swaps

    public int minMovesToMakePalindrome(String s) {

        characterIndices = new List[26];  // There are 26 lowercase English letters
        indexPointers = new int[26][2];  // Two pointers for each character
        BITree = new int[s.length()];  // BITree array initialization

        // Initialize the list for each character
        for (int i = 0; i < 26; ++i) {
            characterIndices[i] = new ArrayList<>();
        }

        // Fill characterIndices with positions of each character in the string
        for (int i = 0; i < s.length(); ++i) {
            characterIndices[s.charAt(i) - 'a'].add(i);
        }

        // Initialize the pointers for each character
        for (int i = 0; i < 26; ++i) {
            indexPointers[i][0] = 0;  // Left pointer starts at 0
            indexPointers[i][1] = characterIndices[i].size() - 1;  // Right pointer starts at the last position
        }

        int left = 0;
        int leftPointer, rightPointer;
        char currentChar;
        int totalMoves = 0;
        int currentIdx;
        boolean[] visited = new boolean[s.length()];  // Track visited indices

        int mid = s.length() / 2;

        for (int right = s.length() - 1; right > left; --right) {
            if (visited[right]) continue;  // Skip if this index has already been processed

            currentChar = s.charAt(right);

            // Get the positions of the matching characters from both sides
            leftPointer = characterIndices[currentChar - 'a'].get(indexPointers[currentChar - 'a'][0]++);
            rightPointer = characterIndices[currentChar - 'a'].get(indexPointers[currentChar - 'a'][1]--);

            visited[leftPointer] = true;
            visited[rightPointer] = true;

            // If leftPointer and rightPointer are the same, it's the center character of an odd-length palindrome
            if (leftPointer == rightPointer) {
                totalMoves += Math.max(mid, 0);
                updateBITree(leftPointer);
                continue;
            }

            // Calculate the number of swaps needed to move the leftPointer to its correct position
            totalMoves += leftPointer - getBITreeValue(leftPointer);
            updateBITree(leftPointer);
            --mid;
        }

        return totalMoves;
    }

    // Update the Binary Indexed Tree with the new index
    private void updateBITree(int index) {
        ++index;

        for (int i = index; i < BITree.length; i += (i & -i)) {
            ++BITree[i];
        }
    }

    // Get the cumulative frequency from the BITree up to the given index
    private int getBITreeValue(int index) {
        index++;
        int cumulativeSum = 0;

        for (int i = index; i > 0; i -= (i & -i)) {
            cumulativeSum += BITree[i];
        }

        return cumulativeSum;
    }

    public static void main(String[] args) {
        MinimumNumberOfMovesToMakePalindromeHard solution = new MinimumNumberOfMovesToMakePalindromeHard();

        System.out.println(solution.minMovesToMakePalindrome("ababc"));  // Expected Output: 3
        System.out.println(solution.minMovesToMakePalindrome("aabb"));   // Expected Output: 2
        System.out.println(solution.minMovesToMakePalindrome("racecar")); // Expected Output: 0
    }
}
