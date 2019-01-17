package HashTable;

import java.util.Map;
import java.util.Objects;

public class MyListPairStringString {
    public class Node {
        public Node next, prev;
        public String key, val;

        Node(String key, String val) {
            this.key = key;
            this.val = val;
            this.next = null;
            this.prev = null;
        }
    }

    public Node head;

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

    public boolean contains(String key) {
        Node node = find(head, key);

        if (node == null) {
            return false;
        } else {
            return true;
        }
    }

    public String get(String key) {
        Node node = find(head, key);

        if (node == null) {
            return null;
        } else {
            return node.val;
        }
    }

    public String put(String key, String val) {
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

    public String remove(String key) {
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

    public void clear() {
        head = null;
    }
}
