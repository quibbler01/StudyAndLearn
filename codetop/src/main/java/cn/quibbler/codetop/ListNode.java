package cn.quibbler.codetop;

public class ListNode {

    public int val;

    public ListNode next;

    public ListNode(int val) {
        this.val = val;
    }

    public ListNode() {
    }

    public static ListNode mock() {
        ListNode head = new ListNode(1);
        ListNode head2 = new ListNode(2);
        ListNode head3 = new ListNode(3);
        ListNode head4 = new ListNode(4);
        head.next = head2;
        head2.next = head3;
        head3.next = head4;

        return head;
    }

    public static void traverseListNode(ListNode head) {
        while (head != null) {
            System.out.print(head.val + " ");
            head = head.next;
        }
        System.out.println();
    }

    public ListNode print() {
        traverseListNode(this);
        return this;
    }

    public static ListNode createListNodeByArray(int... array) {
        ListNode head = new ListNode(-1);

        ListNode cur = head;

        for (int i : array) {
            cur.next = new ListNode(i);
            cur = cur.next;
        }

        return head.next;
    }

}
