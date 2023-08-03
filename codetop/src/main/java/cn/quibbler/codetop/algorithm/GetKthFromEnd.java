package cn.quibbler.codetop.algorithm;

import cn.quibbler.codetop.ListNode;

public class GetKthFromEnd {

    public static void main(String[] args) {
        ListNode head = ListNode.mock();

    }

    public ListNode getKthFromEnd(ListNode head, int k) {
        if (head == null || k < 0) return null;

        ListNode fromt = head, latter = head;
        for (int i = 0; i < k; ++i) {
            if (fromt == null) {
                return null;
            }
            fromt = fromt.next;
        }

        while (fromt != null) {
            fromt = fromt.next;
            latter = latter.next;
        }
        return latter;
    }

}
