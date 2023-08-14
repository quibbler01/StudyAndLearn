package cn.quibbler.codetop.algorithm;

import java.util.ArrayList;
import java.util.List;

public class Subsets {

    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        backtrace(ans, nums, 0, new ArrayList<Integer>());
        return ans;
    }

    private void backtrace(List<List<Integer>> ans, int[] nums, int i, List<Integer> cur) {
        if (i >= nums.length) {
            ans.add(new ArrayList<Integer>(cur));
            return;
        }

        backtrace(ans, nums, i + 1, cur);

        cur.add(nums[i]);
        backtrace(ans, nums, i + 1, cur);
        cur.remove(cur.size() - 1);
    }

}
