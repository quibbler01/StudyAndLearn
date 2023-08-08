package cn.quibbler.codetop.algorithm;

import cn.quibbler.codetop.ListNode;

public class PrintlListNodeTailToHead {

    public static void main(String[] args) {
        ListNode head = ListNode.mock();

        ListNode.traverseListNode(head);

        printListNodeFromTailToHead(head);
    }

    public static void printListNodeFromTailToHead(ListNode head){
        if(head == null) return;
        printListNodeFromTailToHead(head.next);
        System.out.print(head.val+ " ");
    }

}
