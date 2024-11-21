package designguru.articulationPointsAndBridges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CriticalConnectionsInNetworkHard {
    private int time = 0; // Time counter used in DFS

    public List<List<Integer>> criticalConnections(int n, List<List<Integer>> connections) {
        // Initialize graph, discovery time and low time arrays
        List<Integer>[] graph = new ArrayList[n];
        int[] disc = new int[n], low = new int[n];
        Arrays.fill(disc, -1); // Mark all nodes as unvisited
        List<List<Integer>> res = new ArrayList<>();

        // Build the graph
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (List<Integer> conn : connections) {
            graph[conn.get(0)].add(conn.get(1));
            graph[conn.get(1)].add(conn.get(0));
        }

        // Perform DFS from the first node
        dfs(0, -1, disc, low, graph, res);

        return res;
    }

    private void dfs(int u, int parent, int[] disc, int[] low, List<Integer>[] graph, List<List<Integer>> res) {
        disc[u] = low[u] = ++time; // Initialize discovery and low times
        for (int v : graph[u]) {
            if (v == parent) continue; // Skip the parent node
            if (disc[v] == -1) { // If v is not visited
                dfs(v, u, disc, low, graph, res);
                low[u] = Math.min(low[u], low[v]);
                if (low[v] > disc[u]) { // Check for a critical connection
                    res.add(Arrays.asList(u, v));
                }
            } else {
                low[u] = Math.min(low[u], disc[v]); // Update low time of u
            }
        }
    }

    public static void main(String[] args) {
        CriticalConnectionsInNetworkHard sol = new CriticalConnectionsInNetworkHard();
        // Example 1
        int n1 = 5;
        List<List<Integer>> connections1 = Arrays.asList(Arrays.asList(0, 1), Arrays.asList(1, 2), Arrays.asList(2, 3), Arrays.asList(3, 4), Arrays.asList(2, 4));
        System.out.println(sol.criticalConnections(n1, connections1)); // [[2, 3], [3, 4]]

        // Example 2
        int n2 = 4;
        List<List<Integer>> connections2 = Arrays.asList(Arrays.asList(0, 1), Arrays.asList(0, 2), Arrays.asList(1, 2), Arrays.asList(2, 3));
        System.out.println(sol.criticalConnections(n2, connections2)); // [[2, 3]]

        // Example 3
        int n3 = 6;
        List<List<Integer>> connections3 = Arrays.asList(Arrays.asList(0, 1), Arrays.asList(1, 2), Arrays.asList(2, 0), Arrays.asList(1, 3), Arrays.asList(3, 4), Arrays.asList(4, 5), Arrays.asList(5, 3));
        System.out.println(sol.criticalConnections(n3, connections3)); // [[1, 3]]
    }
}
