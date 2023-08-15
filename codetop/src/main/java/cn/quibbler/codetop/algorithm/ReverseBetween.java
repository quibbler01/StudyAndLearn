package cn.quibbler.codetop.algorithm;

import cn.quibbler.codetop.ListNode;

public class ReverseBetween {

    public static void main(String[] args) {
        ListNode head = mock();
        ListNode.traverseListNode(head);
        System.out.println();

        ListNode ans = reverseBetween(head, 2, 4);
        ListNode.traverseListNode(ans);
    }

    /**
     * 输入：head = [1,2,3,4,5], left = 2, right = 4
     * <p>
     * 输出：[1,4,3,2,5]
     *
     * @param head
     * @param left
     * @param right
     * @return
     */
    public static ListNode reverseBetween(ListNode head, int left, int right) {
        if (head == null || left >= right) return head;
        ListNode dummyHead = new ListNode();
        dummyHead.next = head;

        ListNode pre = dummyHead;
        while (left > 1) {
            pre = pre.next;
            --left;
        }
        ListNode start = pre.next;

        ListNode end = dummyHead;
        while (right > 0) {
            end = end.next;
            --right;
        }
        ListNode tail = end.next;
        end.next = null;

        reverseListNode(start);

        pre.next = end;
        start.next = tail;

        return dummyHead.next;
    }

    private static void reverseListNode(ListNode head) {
        ListNode pre = null;
        ListNode cur = head;

        while (cur != null) {
            ListNode tmp = cur.next;
            cur.next = pre;
            pre = cur;
            cur = tmp;
        }
    }

    public static ListNode mock() {
        ListNode head = new ListNode(1);
        ListNode head2 = new ListNode(2);
        ListNode head3 = new ListNode(3);
        ListNode head4 = new ListNode(4);
        ListNode head5 = new ListNode(5);
        head.next = head2;
        head2.next = head3;
        head3.next = head4;
        head4.next = head5;
        return head;
    }

}
