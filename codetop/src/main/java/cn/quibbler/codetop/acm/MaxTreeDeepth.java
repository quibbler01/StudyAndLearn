package cn.quibbler.codetop.acm;

import java.util.Scanner;

import java.util.Arrays;

class Node {
    int data;
    Node left;
    Node right;

    public Node(int data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }
}

public class MaxTreeDeepth {
    static int lchild[] = new int[25];
    static int rchild[] = new int[25];
    static int height;

    public static Node BuildTree(int data) {
        if (data == 0) return null;
        Node root = new Node(data);
        root.left = BuildTree(lchild[data]);
        root.right = BuildTree(rchild[data]);
        return root;
    }

    public static void dfs(Node root, int x) {
        if (root != null) {
            if (x > height) height = x;
            dfs(root.left, x + 1);
            dfs(root.right, x + 1);
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            int n = in.nextInt();
            height = 0;
            for (int i = 1; i <= n; i++) {
                lchild[i] = in.nextInt();
                rchild[i] = in.nextInt();
            }
            Node root = BuildTree(1);
            dfs(root, 1);
            System.out.println(height);
        }
    }
}