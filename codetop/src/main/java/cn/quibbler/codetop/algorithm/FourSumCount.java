package cn.quibbler.codetop.algorithm;

import java.util.HashMap;
import java.util.Map;

/**
 * <a href="https://leetcode.cn/problems/4sum-ii/description/">...</a>
 */
public class FourSumCount {

    public static void main(String[] args) {

    }

    public int fourSumCount(int[] nums1, int[] nums2, int[] nums3, int[] nums4) {
        int cnt = 0;
        Map<Integer, Integer> map = new HashMap<>();
        for (int k : nums1) {
            for (int i : nums2) {
                int sum = k + i;
                map.put(sum, map.getOrDefault(sum, 0) + 1);
            }
        }
        for (int p : nums3) {
            for (int q : nums4) {
                int sum = -(p + q);
                if (map.containsKey(sum)) {
                    cnt += map.get(sum);
                }
            }
        }
        return cnt;
    }

}
