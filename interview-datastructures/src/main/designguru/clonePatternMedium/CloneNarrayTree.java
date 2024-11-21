package designguru.clonePatternMedium;

import java.util.ArrayList;
import java.util.List;

class NAryNode {
     public int val; // Value of the current node
     public List<NAryNode> children; // List of children for the current node

     // Constructor to initialize a node with a value
     public NAryNode(int val) {
         this.val = val;
         this.children = new ArrayList<>();
     }
 }

public class CloneNarrayTree {
    // Method to clone the N-ary tree
    public NAryNode cloneTree(NAryNode root) {
        // Base case: if the current node is null, return null
        if (root == null) return null;

        // Create a new node with the same value as the root node
        NAryNode newNode = new NAryNode(root.val);

        // Recursively clone all the children of the current node
        for (NAryNode child : root.children) {
            newNode.children.add(cloneTree(child));
        }

        // Return the new node, which represents the root of the cloned subtree
        return newNode;
    }

    // Helper method to print the N-ary tree in level order
    public void printTree(NAryNode root) {
        // Base case: if the tree is empty, return
        if (root == null) return;

        // Initialize a list to store nodes at the current level
        List<NAryNode> currentLevel = new ArrayList<>();
        currentLevel.add(root);

        // Process all levels of the tree
        while (!currentLevel.isEmpty()) {
            List<NAryNode> nextLevel = new ArrayList<>();

            // Print all nodes at the current level
            for (NAryNode node : currentLevel) {
                System.out.print(node.val + " "); // Print the value of the current node

                // Add children of the current node to the next level list
                for (NAryNode child : node.children) {
                    nextLevel.add(child);
                }
            }

            System.out.println(); // Move to the next line for the next level

            // Move to the next level
            currentLevel = nextLevel;
        }
    }

    public static void main(String[] args) {
        // Create the original N-ary tree for Example 1
        NAryNode root1 = new NAryNode(1);
        root1.children.add(new NAryNode(2));
        root1.children.add(new NAryNode(3));
        root1.children.add(new NAryNode(4));
        root1.children.get(1).children.add(new NAryNode(5));
        root1.children.get(1).children.add(new NAryNode(6));

        CloneNarrayTree solution = new CloneNarrayTree();

        // Clone the tree using the cloneTree method
        NAryNode clonedRoot1 = solution.cloneTree(root1);

        // Print the original tree
        System.out.println("Original Tree:");
        solution.printTree(root1);

        // Print the cloned tree
        System.out.println("\nCloned Tree:");
        solution.printTree(clonedRoot1);
    }
}
