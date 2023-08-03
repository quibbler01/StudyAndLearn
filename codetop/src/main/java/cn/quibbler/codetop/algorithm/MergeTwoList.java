package cn.quibbler.codetop.algorithm;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import cn.quibbler.codetop.ListNode;

public class MergeTwoList {

    public static void main(String[] args) {

    }

    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1 == null) {
            return l2;
        } else if (l2 == null) {
            return l1;
        } else {
            ListNode result = new ListNode();
            ListNode cur = result;

            while (l1 != null && l2 != null) {
                if (l1.val <= l2.val) {
                    cur.next = l1;
                    cur = cur.next;
                    l1 = l1.next;
                } else {
                    cur.next = l2;
                    cur = cur.next;
                    l2 = l2.next;
                }
            }

            if (l1 != null) {
                cur.next = l1;
            } else if (l2 != null) {
                cur.next = l2;
            }

            return result.next;
        }
    }

}
