package designguru.LinearSortingAlgorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopKFrequentWords {
    private int k; // Number of top frequent words to find
    private List<String> result; // List to store top K frequent words

    // Method to get the top K frequent words
    public List<String> topKFrequent(String[] words, int k) {
        this.k = k;
        result = new ArrayList<>();
        int n = words.length;
        TrieNode[] bucket = new TrieNode[n + 1]; // Array to hold Trie nodes for different frequencies
        Map<String, Integer> count = new HashMap<>(); // Map to count word frequencies

        // Count the frequency of each word
        for (String word : words) {
            count.put(word, count.getOrDefault(word, 0) + 1);
        }

        // Add words to the appropriate trie bucket based on their frequency
        for (var entry : count.entrySet()) {
            int freq = entry.getValue();
            if (bucket[freq] == null) {
                bucket[freq] = new TrieNode(); // Create a new TrieNode if not present
            }
            addWord(bucket[freq], entry.getKey()); // Add the word to the TrieNode
        }

        // Traverse buckets from highest to lowest frequency to get top K words
        for (int i = n; i > 0; i--) {
            if (bucket[i] != null) {
                getWords(bucket[i], ""); // Get words from the TrieNode
            }
            if (this.k == 0) {
                break; // Stop if we have found enough words
            }
        }
        return result;
    }

    // Method to add a word to the trie
    private void addWord(TrieNode root, String word) {
        TrieNode current = root; // Start from the root of the Trie
        for (char c : word.toCharArray()) {
            // Traverse the Trie, create nodes if necessary
            if (current.children[c - 'a'] == null) {
                current.children[c - 'a'] = new TrieNode();
            }
            current = current.children[c - 'a']; // Move to the next node
        }
        current.isEnd = true; // Mark the end of the word
    }

    // Method to retrieve words from the trie in lexicographical order
    private void getWords(TrieNode root, String prefix) {
        if (k == 0) {
            return; // Stop if we have found enough words
        }
        if (root.isEnd) {
            k--;
            result.add(prefix); // Add the word to the result list
        }
        for (int i = 0; i < 26; i++) {
            // Recursively get words from child nodes
            if (root.children[i] != null) {
                getWords(root.children[i], prefix + (char) (i + 'a'));
            }
        }
    }

    // Main method for testing the solution
    public static void main(String[] args) {
        TopKFrequentWords solution = new TopKFrequentWords();
        System.out.println(solution.topKFrequent(new String[]{"apple", "banana", "apple", "orange", "banana", "apple"}, 2)); // ["apple", "banana"]
        System.out.println(solution.topKFrequent(new String[]{"dog", "cat", "mouse", "cat", "dog", "dog"}, 1)); // ["dog"]
        System.out.println(solution.topKFrequent(new String[]{"hello", "world", "hello", "coding", "hello", "world"}, 3)); // ["hello", "world", "coding"]
    }
     class TrieNode {
     TrieNode[] children; // Children nodes for each letter
     boolean isEnd; // Flag to mark the end of a word

     public TrieNode() {
         children = new TrieNode[26]; // 26 letters in the alphabet
         isEnd = false; // Initialize as not the end of a word
     }
 }
}
