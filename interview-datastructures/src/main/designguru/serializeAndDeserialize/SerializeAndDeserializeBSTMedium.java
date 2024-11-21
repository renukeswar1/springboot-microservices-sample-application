package designguru.serializeAndDeserialize;

import java.util.*;

public class SerializeAndDeserializeBSTMedium {
    // Function to serialize the BST
    public String serialize(TreeNode root) {
        StringBuilder sb = new StringBuilder(); // StringBuilder to store the serialized result
        serializeHelper(root, sb);
        return sb.toString();
    }

    // Helper function for pre-order traversal
    private void serializeHelper(TreeNode root, StringBuilder sb) {
        if (root == null) {
            sb.append("#,"); // Use "#" to denote null nodes
            return;
        }
        sb.append(root.val).append(","); // Append the node value followed by a comma
        serializeHelper(root.left, sb); // Recur for the left subtree
        serializeHelper(root.right, sb); // Recur for the right subtree
    }

    // Function to deserialize the serialized string back to BST
    public TreeNode deserialize(String data) {
        Queue<String> nodes = new LinkedList<>(Arrays.asList(data.split(","))); // Split data into a queue
        return deserializeHelper(nodes);
    }

    // Helper function to rebuild the tree
    private TreeNode deserializeHelper(Queue<String> nodes) {
        String value = nodes.poll(); // Retrieve the next node value
        if (value.equals("#")) return null;
        TreeNode root = new TreeNode(Integer.parseInt(value));
        root.left = deserializeHelper(nodes); // Recur for the left subtree
        root.right = deserializeHelper(nodes); // Recur for the right subtree
        return root;
    }

    // Function to convert BST to array format using level-order traversal
    public List<String> treeToArray(TreeNode root) {
        List<String> result = new ArrayList<>(); // List to store the tree in array format
        if (root == null) return result; // Return empty if root is null

        Queue<TreeNode> queue = new LinkedList<>(); // Queue for level-order traversal
        queue.offer(root); // Start with the root

        while (!queue.isEmpty()) {
            TreeNode node = queue.poll(); // Get the current node from the queue
            if (node == null) {
                result.add("null"); // Add "null" for missing nodes
                continue;
            }
            result.add(String.valueOf(node.val)); // Add the current node's value
            queue.offer(node.left); // Add left child to the queue
            queue.offer(node.right); // Add right child to the queue
        }

        // Remove trailing "null" values
        while (result.get(result.size() - 1).equals("null")) {
            result.remove(result.size() - 1);
        }

        return result; // Return the array representation of the tree
    }

    // Main method to test the solution
    public static void main(String[] args) {
        SerializeAndDeserializeBSTMedium sol = new SerializeAndDeserializeBSTMedium();
        TreeNode root = new TreeNode(4); // Creating example tree
        root.left = new TreeNode(2);
        root.right = new TreeNode(6);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(3);
        root.right.right = new TreeNode(7);

        String serialized = sol.serialize(root); // Serialize the BST
        System.out.println("Serialized: " + serialized);

        TreeNode deserialized = sol.deserialize(serialized); // Deserialize the string

        List<String> arrayRepresentation = sol.treeToArray(deserialized); // Convert tree to array format
        System.out.println("Deserialized as Array: " + arrayRepresentation); // Print the array representation
    }
}
  class TreeNode {
     int val;
     TreeNode left;
     TreeNode right;
     TreeNode(int x) {
         val = x;
     }
 }
