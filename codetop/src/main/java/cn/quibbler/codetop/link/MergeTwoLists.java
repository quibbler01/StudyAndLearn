package cn.quibbler.codetop.link;

import cn.quibbler.codetop.ListNode;

public class MergeTwoLists {

    public static void main(String[] args) {
        ListNode list11 = new ListNode(1);
        ListNode list12 = new ListNode(2);
        ListNode list13 = new ListNode(4);
        list11.next = list12;
        list12.next = list13;

        ListNode list21 = new ListNode(1);
        ListNode list22 = new ListNode(3);
        ListNode list23 = new ListNode(4);
        list21.next = list22;
        list22.next = list23;

        mergeTwoLists(list11, list21).print();
    }

    public static ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode res = new ListNode();

        ListNode cur = res;
        while (list1 != null && list2 != null) {
            if (list1.val <= list2.val) {
                cur.next = list1;
                list1 = list1.next;
            }else{
                cur.next = list2;
                list2 = list2.next;
            }
            cur = cur.next;
        }

        ListNode least = list1 == null ? list2 : list1;
        while (least != null) {
            cur.next = least;
            least = least.next;
            cur = cur.next;
        }

        return res.next;
    }

}
