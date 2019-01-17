package HashTable;

import java.util.Objects;

class MyListPairStringString {
    protected class Node {
        protected Node next, prev;
        protected String key, val;

        Node(String key, String val) {
            this.key = key;
            this.val = val;
            this.next = null;
            this.prev = null;
        }
    }

    protected Node head;

    MyListPairStringString() {
        this.head = null;
    }

    private Node find(Node node, String key) {
        if (node == null) {
            return null;
        }

        if (Objects.equals(node.key, key)) {
            return node;
        }

        return find(node.next, key);
    }

    protected boolean contains(String key) {
        Node node = find(head, key);

        return node != null;
    }

    protected String get(String key) {
        Node node = find(head, key);

        if (node == null) {
            return null;
        } else {
            return node.val;
        }
    }

    protected String put(String key, String val) {
        Node node = find(head, key);

        if (node == null) {
            Node newHead = new Node(key, val);

            newHead.next = head;

            if (head != null) {
                head.prev = newHead;
            }
            head = newHead;

            return null;
        } else {
            String ret = node.val;
            node.val = val;
            return ret;
        }
    }

    protected String remove(String key) {
        Node node = find(head, key);

        if (node == null) {
            return null;
        } else {
            String ret = node.val;

            if (node == head) {
                head = node.next;
            } else {
                node.prev.next = node.next;
                if (node.next != null) {
                    node.next.prev = node.prev;
                }
            }

            return ret;
        }
    }

    protected void clear() {
        head = null;
    }
}
