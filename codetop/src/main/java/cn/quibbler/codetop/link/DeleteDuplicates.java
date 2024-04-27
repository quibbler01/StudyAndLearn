package cn.quibbler.codetop.link;

import cn.quibbler.codetop.ListNode;

public class DeleteDuplicates {

    public static void main(String[] args) {
        ListNode test = ListNode.createListNodeByArray(5, 5,5,5,5,5);

        deleteDuplicates(test).print();
    }

    public static ListNode deleteDuplicates(ListNode head) {
        ListNode res = new ListNode(Integer.MIN_VALUE);
        res.next = head;

        ListNode pre = res;
        head = res;
        while (head != null) {
            //head.print();
            if (head.val != pre.val) {
                pre.next = head;
                pre = head;
            }
            head = head.next;
        }
        pre.next = null;

        return res.next;
    }

}
