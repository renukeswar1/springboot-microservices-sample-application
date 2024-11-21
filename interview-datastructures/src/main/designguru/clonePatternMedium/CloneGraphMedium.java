package designguru.clonePatternMedium;

import java.util.*;

public class CloneGraphMedium {
    private Map<GraphNode, GraphNode> visited = new HashMap<>();

    public GraphNode cloneGraph(GraphNode node) {
        // Base condition if node is null
        if (node == null) return null;

        // Return the clone from the map if it's already visited
        if (visited.containsKey(node)) return visited.get(node);

        // Create a new node for the given value
        GraphNode cloneNode = new GraphNode(node.val, new ArrayList<>());
        visited.put(node, cloneNode);

        // Process all the neighbors for the node
        for (GraphNode neighbor : node.neighbors) {
            cloneNode.neighbors.add(cloneGraph(neighbor));
        }

        return cloneNode;
    }

    // Utility function to print the structure of the graph
    public static void printGraph(GraphNode node) {
        Set<GraphNode> printed = new HashSet<>();
        Queue<GraphNode> queue = new LinkedList<>();
        queue.add(node);

        while(!queue.isEmpty()) {
            GraphNode curr = queue.poll();
            if(!printed.contains(curr)) {
                System.out.print(curr.val + "-->");
                for(GraphNode n : curr.neighbors) {
                    queue.add(n);
                    System.out.print(n.val + " ");
                }
                System.out.println();
                printed.add(curr);
            }
        }
    }

    public static void main(String[] args) {
        CloneGraphMedium sol = new CloneGraphMedium();

        // Example 1: Create a simple two-node graph and clone it
        GraphNode node1 = new GraphNode(1);
        GraphNode node2 = new GraphNode(2);
        node1.neighbors = Arrays.asList(node2);
        node2.neighbors = Arrays.asList(node1);
        printGraph(sol.cloneGraph(node1));  // Expecting: 1-->2, 2-->1
    }

    public static class GraphNode {
        public int val;
        public List<GraphNode> neighbors;

        public GraphNode() {
            val = 0;
            neighbors = new ArrayList<GraphNode>();
        }

        public GraphNode(int _val) {
            val = _val;
            neighbors = new ArrayList<GraphNode>();
        }

        public GraphNode(int _val, ArrayList<GraphNode> _neighbors) {
            val = _val;
            neighbors = _neighbors;
        }
    }
}

