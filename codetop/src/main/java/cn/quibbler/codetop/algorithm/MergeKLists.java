package cn.quibbler.codetop.algorithm;

import cn.quibbler.codetop.ListNode;

public class MergeKLists {

    public ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;
        ListNode dummy = new ListNode(Integer.MIN_VALUE);
        for (ListNode listNode : lists) {
            dummy = mergeTwoList(dummy, listNode);
        }
        return dummy.next;
    }

    private ListNode mergeTwoList(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(-1);
        ListNode temp = dummy;
        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                temp.next = l1;
                l1 = l1.next;
            } else {
                temp.next = l2;
                l2 = l2.next;
            }
            temp = temp.next;
        }

        temp.next = l1 != null ? l1 : l2;

        return dummy.next;
    }

}
