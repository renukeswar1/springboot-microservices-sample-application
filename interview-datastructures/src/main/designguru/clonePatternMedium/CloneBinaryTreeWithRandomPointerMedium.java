package designguru.clonePatternMedium;

import java.util.HashMap;

public class CloneBinaryTreeWithRandomPointerMedium {

    // Method to deep copy the binary tree with random pointers
    public TreeNode copyRandomBinaryTree(TreeNode root) {
        // If the original tree is empty, return null
        if (root == null) return null;

        // HashMap to store the mapping of original nodes to their cloned counterparts
        HashMap<TreeNode, TreeNode> clonedMap = new HashMap<>();

        // Start the cloning process from the root
        return cloneNode(root, clonedMap);
    }

    // Helper method to clone a given node
    private TreeNode cloneNode(TreeNode node, HashMap<TreeNode, TreeNode> clonedMap) {
        // Base case: If the node is null, return null
        if (node == null) return null;

        // If the node has already been cloned, return the cloned node from the map
        if (clonedMap.containsKey(node)) return clonedMap.get(node);

        // Create a new node as a deep copy of the original node
        TreeNode newNode = new TreeNode(node.val);

        // Add the new cloned node to the map
        clonedMap.put(node, newNode);

        // Recursively clone the left and right children and assign them to the cloned node
        newNode.left = cloneNode(node.left, clonedMap);
        newNode.right = cloneNode(node.right, clonedMap);

        // Recursively clone the random pointer and assign it to the cloned node
        newNode.random = cloneNode(node.random, clonedMap);

        // Return the cloned node
        return newNode;
    }

    public static void main(String[] args) {
        CloneBinaryTreeWithRandomPointerMedium solution = new CloneBinaryTreeWithRandomPointerMedium();

        // Creating the original tree for Example 1
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);

        // Setting up random pointers
        root.random = root.left;
        root.left.random = null;
        root.right.random = root;
        root.right.left.random = root.right;
        root.right.right.random = null;

        // Cloning the binary tree
        TreeNode clonedRoot = solution.copyRandomBinaryTree(root);

        // Printing the original tree nodes
        System.out.println("Original Tree:");
        printNodeDetails(root, "root");
        printNodeDetails(root.left, "root.left");
        printNodeDetails(root.right, "root.right");
        printNodeDetails(root.right.left, "root.right.left");
        printNodeDetails(root.right.right, "root.right.right");

        // Printing the cloned tree nodes
        System.out.println("\nCloned Tree:");
        printNodeDetails(clonedRoot, "clonedRoot");
        printNodeDetails(clonedRoot.left, "clonedRoot.left");
        printNodeDetails(clonedRoot.right, "clonedRoot.right");
        printNodeDetails(clonedRoot.right.left, "clonedRoot.right.left");
        printNodeDetails(clonedRoot.right.right, "clonedRoot.right.right");
    }

    // Helper method to print detailed node information
    private static void printNodeDetails(TreeNode node, String nodeName) {
        if (node == null) return;
        System.out.println(nodeName + ".val = " + node.val + ", " +
                nodeName + ".random = " +
                (node.random != null ? node.random.val : "null"));
    }

}


 class TreeNode {
     public int val;
     public TreeNode left;
     public TreeNode right;
     public TreeNode random;

     // Constructor to initialize a TreeNode with a given value
     public TreeNode(int val) {
         this.val = val;
         this.left = null;
         this.right = null;
         this.random = null;
     }
 }