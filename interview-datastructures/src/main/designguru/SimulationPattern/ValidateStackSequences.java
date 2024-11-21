package designguru.SimulationPattern;

import java.util.Stack;

public class ValidateStackSequences {
    public boolean validateStackSequences(int[] pushed, int[] popped) {
        Stack<Integer> stack = new Stack<>();
        int i = 0;

        for (int num : pushed) {
            stack.push(num); // push onto stack
            // When stack is empty and top elements matches the popped[i]
            while (!stack.isEmpty() && stack.peek() == popped[i]) {
                stack.pop(); // pop from stack
                i++;
            }
        }

        return stack.isEmpty();
    }

    public static void main(String[] args) {
        ValidateStackSequences sol = new ValidateStackSequences();
        int[] pushed1 = {1, 2, 3, 4, 6, 7};
        int[] popped1 = {4, 6, 3, 2, 7, 1};
        System.out.println(sol.validateStackSequences(pushed1, popped1)); // true

        int[] pushed2 = {1, 2, 3, 4};
        int[] popped2 = {4, 3, 2, 1};
        System.out.println(sol.validateStackSequences(pushed2, popped2)); // true

        int[] pushed3 = {1, 2, 3, 4, 5};
        int[] popped3 = {4, 5, 3, 1, 2};
        System.out.println(sol.validateStackSequences(pushed3, popped3)); // false
    }
}
