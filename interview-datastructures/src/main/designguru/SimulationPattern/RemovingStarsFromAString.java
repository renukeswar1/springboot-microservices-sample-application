package designguru.SimulationPattern;

public class RemovingStarsFromAString {

    public String removeStars(String s) {
        // Use StringBuilder as a stack
        StringBuilder stack = new StringBuilder();

        // Iterate through each character in the string
        for (char c : s.toCharArray()) {
            if (c != '*') {
                // If character is not '*', push it onto the stack
                stack.append(c);
            } else {
                // If character is '*', pop the top character from the stack
                stack.deleteCharAt(stack.length() - 1);
            }
        }

        // Convert stack to string
        return stack.toString();
    }

    public static void main(String[] args) {
        RemovingStarsFromAString solution = new RemovingStarsFromAString();
        System.out.println(solution.removeStars("ab*cd*ef*")); // Output: "ace"
        System.out.println(solution.removeStars("a*bc*def**g")); // Output: "bdg"
        System.out.println(solution.removeStars("xy*z*ww*")); // Output: "xw"
    }
}
