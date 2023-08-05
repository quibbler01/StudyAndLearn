package cn.quibbler.codetop.algorithm;

import cn.quibbler.codetop.ListNode;

public class ReverseListNode {

    public static void main(String[] args) {
        ListNode head = ListNode.mock();
        ListNode.traverseListNode(head);

        ListNode ans = reverstList(head);

        ListNode.traverseListNode(ans);
    }

    public static ListNode reverstList(ListNode head){
        ListNode pre = new ListNode();
        while(head != null){
            ListNode tmp = head.next;
            head.next = pre.next;
            pre.next = head;

            head = tmp;
        }
        return pre.next;
    }

}
