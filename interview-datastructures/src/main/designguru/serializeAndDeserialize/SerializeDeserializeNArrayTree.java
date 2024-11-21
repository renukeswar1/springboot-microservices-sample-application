package designguru.serializeAndDeserialize;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SerializeDeserializeNArrayTree {
    public String serialize(NAryNode root) {
        if (root == null) return ""; // If the tree is empty, return an empty string
        StringBuilder sb = new StringBuilder();
        Queue<NAryNode> queue = new LinkedList<>();
        queue.offer(root); // Add the root node to the queue

        while (!queue.isEmpty()) {
            NAryNode node = queue.poll(); // Remove the front node from the queue
            if (node == null) {
                sb.append("null,"); // Add "null" to the string to indicate the end of a level
                continue;
            }
            sb.append(node.val).append(","); // Append the value of the current node to the string
            for (NAryNode child : node.children) {
                queue.offer(child); // Add all children of the current node to the queue
            }
            queue.offer(null);
        }
        return sb.toString();
    }


    public NAryNode deserialize(String data) {
        if (data.isEmpty()) return null; // If the input string is empty, return null
        String[] nodes = data.split(","); // Split the string by commas to get node values
        NAryNode root = new NAryNode(Integer.parseInt(nodes[0]), new ArrayList<>());
        Queue<NAryNode> queue = new LinkedList<>();
        queue.offer(root); // Add the root node to the queue

        for (int i = 1; i < nodes.length; i++) {
            NAryNode parent = queue.poll(); // Get the next parent node from the queue
            while (!nodes[i].equals("null")) {
                NAryNode child = new NAryNode(Integer.parseInt(nodes[i]), new ArrayList<>());

                parent.children.add(child); // Add the child node to the parent
                queue.offer(child); // Add the child to the queue to process its children
                i++;
            }
        }
        return root;
    }

    public static void main(String[] args) {
        SerializeDeserializeNArrayTree solution = new SerializeDeserializeNArrayTree();

        // Create nodes for the tree
        NAryNode root = new NAryNode(1); // Root node with value 1
        NAryNode node2 = new NAryNode(2); // Node with value 2
        NAryNode node3 = new NAryNode(3); // Node with value 3
        NAryNode node4 = new NAryNode(4); // Node with value 4
        NAryNode node5 = new NAryNode(5); // Node with value 5
        NAryNode node6 = new NAryNode(6); // Node with value 6

        // Build the tree structure
        root.children.add(node2); // Adding node 2 as a child of root
        root.children.add(node3); // Adding node 3 as a child of root
        root.children.add(node4); // Adding node 4 as a child of root

        node2.children.add(node5); // Adding node 5 as a child of node 2
        node2.children.add(node6); // Adding node 6 as a child of node 2

        // Serialize the tree
        String serializedData = solution.serialize(root);
        System.out.println("Serialized Output: " + serializedData); // Output should be: [1,null,2,3,4,null,5,6]

        // Deserialize the tree back from the serialized data
        NAryNode deserializedRoot = solution.deserialize(serializedData);
        System.out.println("Deserialized Output: " + solution.serialize(deserializedRoot)); // Should match the serialized output
    }

}
 class NAryNode {
     public int val;
     public List<NAryNode> children;

     public NAryNode() {}

     public NAryNode(int _val) {
         val = _val;
     }

     public NAryNode(int _val, List<NAryNode> _children) {
         val = _val;
         children = _children;
     }
 }