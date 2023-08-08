package cn.quibbler.codetop.algorithm;

import java.util.ArrayList;
import java.util.List;

public class GenerateParenthesis {

    public List<String> generateParenthesis(int n) {
        List<String> ans = new ArrayList<>();
        backTrace(ans, n, 0, 0, new StringBuilder());
        return ans;
    }

    private void backTrace(List<String> ans, int n, int left, int right, StringBuilder sb) {
        if (left > n || right > n || left < right) return;
        if (left == n && right == n) {
            ans.add(sb.toString());
            return;
        }

        sb.append(')');
        backTrace(ans, n, left, right + 1, sb);
        sb.deleteCharAt(sb.length() - 1);

        sb.append("(");
        backTrace(ans, n, left + 1, right, sb);
        sb.deleteCharAt(sb.length() - 1);
    }

}
