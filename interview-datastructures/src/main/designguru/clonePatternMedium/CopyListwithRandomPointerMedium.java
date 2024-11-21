package designguru.clonePatternMedium;

public class CopyListwithRandomPointerMedium {
    // Method to create a deep copy of a linked list with random pointers
    public ListNode copyRandomList(ListNode head) {
        if (head == null) return null;  // If the original list is empty, return null

        // Step 1: Create the copied nodes and insert them next to their originals
        ListNode current = head;
        while (current != null) {
            ListNode copy = new ListNode(current.val);  // Create a new node (deep copy)
            // original linked list: A->B->C.
            // Updated linked list A->A'->B->B'->C->C.
            copy.next = current.next;           // Set the next of the new node
            current.next = copy;                // Link the new node after the original node
            current = copy.next;                // Move to the next original node
        }

        // Step 2: Assign random pointers for the copied nodes
        current = head;
        while (current != null) {
            if (current.random != null) {
                current.next.random = current.random.next;  // Set the random pointer for the copied node
            }
            current = current.next.next;  // Move to the next original node
        }

        // Step 3: Separate the original list from the copied list
        // i.e. A->A'->B->B'->C->C' would be broken to A->B->C and A'->B'->C'
        current = head;
        ListNode copiedHead = head.next;  // Head of the copied linked list
        ListNode copy = copiedHead;
        while (current != null) {
            current.next = current.next.next;  // Restore the original list
            if (copy.next != null) {
                copy.next = copy.next.next;    // Set the next for the copied list
            }
            current = current.next;  // Move to the next original node
            copy = copy.next;        // Move to the next copied node
        }

        return copiedHead;  // Return the head of the copied linked list
    }

    // Helper method to print the list for testing
    public void printList(ListNode head) {
        ListNode current = head;
        while (current != null) {
            int randomVal = (current.random != null) ? current.random.val : -1;  // Get the random value or -1 if null
            System.out.print("ListNode value: " + current.val + ", Random points to: " + randomVal + " | ");
            current = current.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        CopyListwithRandomPointerMedium solution = new CopyListwithRandomPointerMedium();
        // Example 1: Constructing the list [[3, 1], [4, null], [5, 0], [7, 2]]
        ListNode node1 = new ListNode(3);
        ListNode node2 = new ListNode(4);
        ListNode node3 = new ListNode(5);
        ListNode node4 = new ListNode(7);

        node1.next = node2;
        node2.next = node3;
        node3.next = node4;

        node1.random = node2;  // ListNode with value 3 has a random pointer to node with value 4
        node2.random = null;   // ListNode with value 4 has a random pointer to null
        node3.random = node1;  // ListNode with value 5 has a random pointer to node with value 3
        node4.random = node3;

        // Print the original list
        System.out.println("Original List:");
        solution.printList(node1);

        // Create a deep copy of the list
        ListNode copiedHead = solution.copyRandomList(node1);

        // Print the copied list
        System.out.println("Copied List:");
        solution.printList(copiedHead);
    }
}
 class ListNode {
     int val;
     ListNode next;
     ListNode random;

     public ListNode(int val) {
         this.val = val;
         this.next = null;
         this.random = null;
     }
 }
