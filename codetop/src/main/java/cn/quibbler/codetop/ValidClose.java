package cn.quibbler.codetop;

import java.util.Stack;

public class ValidClose {

    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        char[] cs = s.toCharArray();
        for (char c : cs) {
            if (stack.empty()) {
                stack.push(c);
            } else {
                char left = stack.peek();
                if (isClose(left, c)) {
                    stack.pop();
                } else {
                    stack.push(c);
                }
            }
        }

        return stack.empty();
    }

    private boolean isClose(char left, char right) {
        return left == '(' && right == ')' || left == '[' && right == ']' ||left == '{' && right == '}';
    }

}
