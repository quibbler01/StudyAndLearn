package cn.quibbler.codetop.algorithm;

import java.util.ArrayList;
import java.util.List;

public class Combine {

    public static void main(String[] args) {
        List<List<Integer>> ans = combine(4, 2);

        System.out.println("ans = " + ans);
    }

    public static List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> ans = new ArrayList<>();
        reverse(ans, n, 1, k, new ArrayList<Integer>());
        return ans;
    }

    public static void reverse(List<List<Integer>> ans, int n, int i, int k, List<Integer> cur) {
        if (cur.size() >= k || i > n) {
            if (cur.size() == k) {
                ans.add(new ArrayList<Integer>(cur));
            }
            return;
        }
        reverse(ans, n, i + 1, k, cur);

        cur.add(i);
        reverse(ans, n, i + 1, k, cur);
        cur.remove(cur.size() - 1);
    }

}
