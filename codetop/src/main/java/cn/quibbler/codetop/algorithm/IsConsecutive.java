package cn.quibbler.codetop.algorithm;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

/*
https://leetcode.cn/problems/check-if-an-array-is-consecutive/
 */
public class IsConsecutive {

    public boolean isConsecutive(int[] nums) {
        if (nums == null) return false;
        Arrays.sort(nums);
        return nums[nums.length - 1] - nums[0] + 1 == nums.length && Arrays.stream(nums).boxed().collect(Collectors.toSet()).size() == nums.length;
    }

}
