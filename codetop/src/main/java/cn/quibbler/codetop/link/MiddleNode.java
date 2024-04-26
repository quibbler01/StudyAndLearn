package cn.quibbler.codetop.link;

import cn.quibbler.codetop.ListNode;

public class MiddleNode {

    public static void main(String[] args) {
        ListNode head = ListNode.createListNodeByArray(1, 2, 3, 4);

        middleNode(head).print();
    }

    public static ListNode middleNode(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode first = head, second = head;
        while (second != null && second.next != null) {
            first = first.next;
            second = second.next.next;
        }
        return first;
    }

}
