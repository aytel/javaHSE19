package com.aytel.HashTable;

import java.util.AbstractMap.SimpleEntry;

class MyListOfPairsStringString {

    private Node head = null;

    MyListOfPairsStringString() {}

    private Node find(Node node, String key) {
        if (node == null) {
            return null;
        }

        if (node.key.equals(key)) {
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
            return node.value;
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
            String returnValue = node.value;
            node.value = val;
            return returnValue;
        }
    }

    protected String remove(String key) {
        Node node = find(head, key);

        if (node == null) {
            return null;
        } else {
            String returnValue = node.value;

            if (node == head) {
                head = node.next;
            } else {
                node.prev.next = node.next;
                if (node.next != null) {
                    node.next.prev = node.prev;
                }
            }

            return returnValue;
        }
    }

    protected SimpleEntry<String, String> pop() {
        if (head == null) {
            return null;
        }
        Node oldHead = head;
        head = head.next;
        return new SimpleEntry<>(oldHead.key, oldHead.value);
    }

    private class Node {
        Node next;
        Node prev;
        String key;
        String value;

        Node(String key, String value) {
            this.key = key;
            this.value = value;
            this.next = null;
            this.prev = null;
        }
    }
}
