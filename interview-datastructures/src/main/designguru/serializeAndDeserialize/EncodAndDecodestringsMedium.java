package designguru.serializeAndDeserialize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EncodAndDecodestringsMedium {
    // Function to encode a list of strings
    public String encode(List<String> strs) {
        // StringBuilder to build the encoded string
        StringBuilder encoded_string = new StringBuilder();

        // Iterate through each string in the list
        for (String s : strs) {
            // Append the length of the string followed by a '#' separator
            encoded_string.append(s.length()).append("#").append(s);
            // The length helps in decoding the exact number of characters for each string
        }
        // Return the final encoded string containing all strings in the desired format
        return encoded_string.toString();
    }

    // Function to decode the single string back to a list of strings
    public List<String> decode(String s) {
        List<String> strs = new ArrayList<>();
        int i = 0;

        // Process the encoded string until all characters are decoded
        while (i < s.length()) {
            // Find the position of the next '#' separator
            int j = s.indexOf('#', i);
            // Extract the length of the next string using the substring before '#'
            int length = Integer.parseInt(s.substring(i, j));
            // Extract the actual string using the calculated length
            strs.add(s.substring(j + 1, j + 1 + length));
            // Move the pointer to the next encoded string part
            i = j + 1 + length;
        }
        // Return the decoded list of strings
        return strs;
    }

    public static void main(String[] args) {
        EncodAndDecodestringsMedium sol = new EncodAndDecodestringsMedium();
        List<String> strs1 = Arrays.asList("dog", "cat", "bird");
        List<String> strs2 = Arrays.asList("hello,world", "foo!bar", "");
        List<String> strs3 = Arrays.asList("", "", "empty");

        // Encode and then decode the strings to test the correctness
        String encoded1 = sol.encode(strs1);
        String encoded2 = sol.encode(strs2);
        String encoded3 = sol.encode(strs3);

        // Output the results to verify that the decoded strings match the original input
        System.out.println(sol.decode(encoded1)); // Output: ["dog", "cat", "bird"]
        System.out.println(sol.decode(encoded2)); // Output: ["hello,world", "foo!bar", ""]
        System.out.println(sol.decode(encoded3)); // Output: ["", "", "empty"]
    }
}
