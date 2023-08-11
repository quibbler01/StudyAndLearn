package cn.quibbler.codetop.algorithm;

import java.util.HashMap;
import java.util.Map;

public class LFUCache {

    public static void main(String[] args) {
        LFUCache lfuCache = new LFUCache(3);

        lfuCache.put(2,2);
        lfuCache.put(1,1);

        System.out.println("lfuCache.get(2) = " + lfuCache.get(2));
        System.out.println("lfuCache.get(1) = " + lfuCache.get(1));
        System.out.println("lfuCache.get(2) = " + lfuCache.get(2));

        lfuCache.put(3,3);
        lfuCache.put(4,4);

        System.out.println();

        System.out.println("lfuCache.get(3) = " + lfuCache.get(3));
        System.out.println("lfuCache.get(2) = " + lfuCache.get(2));
        System.out.println("lfuCache.get(1) = " + lfuCache.get(1));
        System.out.println("lfuCache.get(4) = " + lfuCache.get(4));
    }

    private static class Node {
        int key;
        int val;

        public Node() {
        }

        public Node(int key, int val) {
            this.key = key;
            this.val = val;
        }

        Node pre;
        Node next;
    }

    private int capacity;
    private int size;

    private Node dummyHead;
    private Node dummyTail;

    private Map<Integer, Node> cache;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.size = 0;

        cache = new HashMap<>();

        dummyHead = new Node();
        dummyTail = new Node();
        dummyHead.next = dummyTail;
        dummyTail.pre = dummyHead;
    }

    public int get(int key) {
        if (!cache.containsKey(key)) {
            return -1;
        }
        Node node = cache.get(key);
        unlink(node);
        addHead(node);
        return node.val;
    }

    public void put(int key, int val) {
        Node node = new Node(key, val);
        if (cache.containsKey(key)) {
            Node tmp = cache.get(key);
            unlink(tmp);
            addHead(node);
        } else {
            if (size == capacity) {
                removeTail();
                addHead(node);
            } else {
                addHead(node);
            }
        }
    }

    private void unlink(Node node) {
        node.pre.next = node.next;
        node.next.pre = node.pre;
        cache.remove(node.key);
        --size;
    }

    private void addHead(Node node) {
        Node tmp = dummyHead.next;

        dummyHead.next = node;
        node.next = tmp;
        node.pre = dummyHead;
        tmp.pre = node;

        cache.put(node.key, node);
        ++size;
    }

    private void removeTail() {
        Node tail = dummyTail.pre;
        unlink(tail);
    }

}
