package cn.quibbler.codetop.link;

import cn.quibbler.codetop.ListNode;

public class SwapPairs {

    public static void main(String[] args) {
        swapPairs(ListNode.createListNodeByArray(1, 2, 3, 4, 54).print()).print();
    }

    /**
     * 输入：head = [1,2,3,4]
     * 输出：[2,1,4,3]
     *
     * @param head
     * @return
     */
    public static ListNode swapPairs(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode res = new ListNode(-1);
        res.next = head;

        ListNode preHead = res;
        ListNode cur = head;
        while (cur != null && cur.next != null) {
            preHead.next = cur.next;

            ListNode tmp = cur.next.next;
            cur.next.next = cur;
            cur.next = tmp;

            preHead = cur;
            cur = tmp;
        }

        return res.next;
    }

}
