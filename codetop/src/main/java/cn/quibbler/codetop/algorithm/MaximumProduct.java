package cn.quibbler.codetop.algorithm;

import java.util.Arrays;

/**
 * https://leetcode.cn/problems/maximum-product-of-three-numbers/description/
 */
public class MaximumProduct {

    public int maximumProduct(int[] nums) {
        Arrays.sort(nums);
        return Math.max(nums[0] * nums[1] * nums[nums.length - 1], nums[nums.length - 3] * nums[nums.length - 2] * nums[nums.length - 1]);
    }

}
