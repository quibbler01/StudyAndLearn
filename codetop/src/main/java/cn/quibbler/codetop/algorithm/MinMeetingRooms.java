package cn.quibbler.codetop.algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * https://leetcode.cn/problems/meeting-rooms-ii/solutions/179963/sao-miao-xian-zuo-fa-huan-shi-fei-chang-jian-dan-d/
 */
public class MinMeetingRooms {

    public int minMeetingRooms(int[][] intervals) {
        List<int[]> list = new ArrayList<>();
        for (int[] interval : intervals) {
            list.add(new int[]{interval[0], 0});
            list.add(new int[]{interval[1], 1});
        }
        list.sort(new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[0] == o2[0] ? o2[1] - o1[1] : o1[0] - o2[0];
            }
        });

        int rooms = 0,ans = 0;
        for (int[] i : list) {
            if (i[1] == 0) {
                ++rooms;
                ans = Math.max(ans, rooms);
            }else{
                --rooms;
            }
        }

        return ans;
    }

}
