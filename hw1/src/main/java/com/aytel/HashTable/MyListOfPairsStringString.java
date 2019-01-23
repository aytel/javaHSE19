package com.aytel.HashTable;

import java.util.AbstractMap.SimpleEntry;

class MyListOfPairsStringString {
    private class Node {
        private Node next;
        private Node prev;
        private String key;
        private String val;

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public Node getPrev() {
            return prev;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }

        public String getKey() {
            return key;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }

        Node(String key, String val) {
            this.key = key;
            this.val = val;
            this.next = null;
            this.prev = null;
        }
    }

    private Node head = null;

    MyListOfPairsStringString() {}

    private Node find(Node node, String key) {
        if (node == null) {
            return null;
        }

        if (node.getKey().equals(key)) {
            return node;
        }

        return find(node.getNext(), key);
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

            newHead.setNext(head);

            if (head != null) {
                head.setPrev(newHead);
            }
            head = newHead;

            return null;
        } else {
            String ret = node.getVal();
            node.setVal(val);
            return ret;
        }
    }

    protected String remove(String key) {
        Node node = find(head, key);

        if (node == null) {
            return null;
        } else {
            String ret = node.getVal();

            if (node == head) {
                head = node.getNext();
            } else {
                node.getPrev().setNext(node.getNext());
                if (node.getNext() != null) {
                    node.getNext().setPrev(node.prev);
                }
            }

            return ret;
        }
    }

    protected void clear() {
        head = null;
    }

    protected SimpleEntry<String, String> pop() {
        if (head == null) {
            return null;
        }
        Node oldHead = head;
        head = head.getNext();
        return new SimpleEntry<>(oldHead.getKey(), oldHead.getVal());
    }
}
