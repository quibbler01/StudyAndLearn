package cn.quibbler.codetop.link;

import cn.quibbler.codetop.ListNode;

public class RemoveNthFromEnd {

    public static void main(String[] args) {
        ListNode listNode = ListNode.createListNodeByArray(1);

        removeNthFromEnd(listNode, 1).print();
    }

    /**
     * Time : O(n+m)
     * Space : O(1)
     *
     * @param head
     * @param n
     * @return
     */
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode tmp = new ListNode(-1);
        tmp.next = head;

        int len = listLength(tmp);
        int p = len - n;

        head = tmp;
        while (--p > 0) {
            head = head.next;
        }
        head.next = head.next == null ? null : head.next.next;

        return tmp.next;
    }

    private static int listLength(ListNode head) {
        int cnt = 0;
        while (head != null) {
            head = head.next;
            ++cnt;
        }
        return cnt;
    }

}
