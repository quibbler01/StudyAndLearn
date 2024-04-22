package cn.quibbler.codetop.link;

import cn.quibbler.codetop.ListNode;

public class RotateRight {

    public static void main(String[] args) {
        ListNode listNode = ListNode.createListNodeByArray(1, 2, 3, 4, 5).print();

        rotateRight(listNode, 10).print();
    }

    public static ListNode rotateRight(ListNode head, int k) {
        if (k == 0 || head == null || head.next == null) return head;

        ListNode newHead = null, oldHead = head, oldTail = null;

        int length = 0;
        while (head != null) {
            length++;
            if (head.next == null) {
                oldTail = head;
            }
            head = head.next;
        }

        k = k % length;
        if (k == 0) return oldHead;

        int front = length - k;

        head = oldHead;
        while (front - 1 > 0) {
            front--;
            head = head.next;
        }

        newHead = head.next;
        head.next = null;
        oldTail.next = oldHead;

        return newHead;
    }

}
