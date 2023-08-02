package cn.quibbler.codetop.algorithm;

import cn.quibbler.codetop.ListNode;

public class deleteNode {

    public static void main(String[] args) {

    }

    public ListNode deleteNode(ListNode head, int val) {
        ListNode ans = new ListNode();
        ans.next = head;

        ListNode pre = ans;
        while (head != null) {
            if (head.val == val) {
                pre.next = head.next;
                break;
            }
            pre = head;
            head = head.next;
        }

        return ans.next;
    }

}
