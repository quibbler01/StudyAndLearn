package cn.quibbler.codetop;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class WidthOfBinaryTree {

    public int widthOfBinaryTree(TreeNode root) {
        if (root == null) return 0;

        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        LinkedList<Integer> list = new LinkedList<>();
        list.add(1);

        int ans = 1;
        while (!q.isEmpty()) {
            int size = q.size();
            for (int i = 0; i < size; ++i) {
                TreeNode node = q.poll();
                int currentIndex = list.removeFirst();

                if (node.left != null) {
                    q.offer(node.left);
                    list.offer(currentIndex * 2);
                }
                if (node.right != null) {
                    q.offer(node.right);
                    list.offer(currentIndex * 2 + 1);
                }
            }
            if (list.size() >= 2) {
                ans = Math.max(ans, list.getLast() - list.getFirst() + 1);
            }
        }

        return ans;
    }

}
