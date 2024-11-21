package designguru.serializeAndDeserialize;

public class VerifyPreorderSerializationBinaryTreeMedium {
    public boolean isValidSerialization(String preorder) {
        // Initialize the number of available slots for nodes in the binary tree.
        int availableSlots = 1;

        // Get the length of the input string.
        int length = preorder.length();

        // Traverse through each character in the input string.
        for (int i = 0; i < length; ++i) {
            // If a comma is encountered, a new node has been processed.
            if (preorder.charAt(i) == ',') {
                // Decrease the available slots by 1 as the node consumes one slot.
                --availableSlots;

                // If no slots are available, the serialization is invalid.
                if (availableSlots < 0) return false;

                // If the previous character is not '#', it represents a non-null node,
                // which creates two additional slots for its children.
                if (preorder.charAt(i - 1) != '#') availableSlots += 2;
            }
        }

        // Process the last node in the input string.
        availableSlots = (preorder.charAt(length - 1) == '#') ? availableSlots - 1 : availableSlots + 1;

        // The serialization is valid if all slots are exactly filled.
        return availableSlots == 0;
    }

    public static void main(String[] args) {
        VerifyPreorderSerializationBinaryTreeMedium solution = new VerifyPreorderSerializationBinaryTreeMedium();
        System.out.println(solution.isValidSerialization("5,2,#,#,3,1,#,#,#")); // true
        System.out.println(solution.isValidSerialization("7,3,1,#,#,4,#,#,8,#,2,#,#")); // true
        System.out.println(solution.isValidSerialization("1,#,#,2")); // false
    }
}
