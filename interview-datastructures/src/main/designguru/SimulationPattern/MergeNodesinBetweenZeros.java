package designguru.SimulationPattern;

 class ListNode {
     int val;
     ListNode next;
     ListNode(int val) { this.val = val; }
 }

public class MergeNodesinBetweenZeros {
    public ListNode mergeNodes(ListNode head) {
        // Create a placeholder node to assist in merging
        ListNode helperNode = head.next;
        ListNode sumNode = helperNode;

        while (sumNode != null) {
            int accumulatedSum = 0;
            // Accumulate sum of nodes between zeros
            while (sumNode.val != 0) {
                accumulatedSum += sumNode.val;
                sumNode = sumNode.next;
            }

            // Assign the accumulated sum to the current node's value
            helperNode.val = accumulatedSum;
            // Move sumNode to the first non-zero value of the next segment
            sumNode = sumNode.next;
            // Move helperNode also to this node
            helperNode.next = sumNode;
            helperNode = helperNode.next;
        }
        return head.next;
    }

    public static void main(String[] args) {
        MergeNodesinBetweenZeros sol = new MergeNodesinBetweenZeros();

        // Test Example 1
        ListNode head1 = new ListNode(0);
        head1.next = new ListNode(2);
        head1.next.next = new ListNode(3);
        head1.next.next.next = new ListNode(0);
        head1.next.next.next.next = new ListNode(4);
        head1.next.next.next.next.next = new ListNode(5);
        head1.next.next.next.next.next.next = new ListNode(0);
        head1.next.next.next.next.next.next.next = new ListNode(3);
        head1.next.next.next.next.next.next.next.next = new ListNode(0);
        head1.next.next.next.next.next.next.next.next.next = new ListNode(4);
        head1.next.next.next.next.next.next.next.next.next.next = new ListNode(0);
        ListNode result1 = sol.mergeNodes(head1);
        while (result1 != null) {
            System.out.print(result1.val + " ");
            result1 = result1.next;
        }
        System.out.println(" ");

        // Test Example 2
        ListNode head2 = new ListNode(0);
        head2.next = new ListNode(1);
        head2.next.next = new ListNode(1);
        head2.next.next.next = new ListNode(1);
        head2.next.next.next.next = new ListNode(0);
        head2.next.next.next.next.next = new ListNode(2);
        head2.next.next.next.next.next.next = new ListNode(2);
        head2.next.next.next.next.next.next.next = new ListNode(0);
        ListNode result2 = sol.mergeNodes(head2);
        while (result2 != null) {
            System.out.print(result2.val + " ");
            result2 = result2.next;
        }
        System.out.println(" ");

        // Test Example 3
        ListNode head3 = new ListNode(0);
        head3.next = new ListNode(5);
        head3.next.next = new ListNode(0);
        head3.next.next.next = new ListNode(10);
        head3.next.next.next.next = new ListNode(15);
        head3.next.next.next.next.next = new ListNode(0);
        ListNode result3 = sol.mergeNodes(head3);
        while (result3 != null) {
            System.out.print(result3.val + " ");
            result3 = result3.next;
        }
        System.out.println(" ");
    }
}
