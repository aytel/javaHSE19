package com.aytel.Trie;

import java.util.HashMap;
import java.util.Stack;

public class Trie {
    private class Node {
        private int size = 0;
        private boolean end = false;
        private HashMap<Character, Node> edges = new HashMap<>();

        Node(){}

        int size() {
            return this.size;
        }

        void incSize() {
            size++;
        }

        void decSize() {
            size--;
        }

        Node getNext(Character c) {
            return edges.get(c);
        }

        void setNext(Character c, Node to) {
            edges.put(c, to);
        }

        boolean getEnd() {
            return end;
        }

        void setEnd(boolean isEnd) {
            end = isEnd;
        }
    }

    private Node root = new Node();

    public Trie(){}

    public int size() {
        return root.size();
    }

    public boolean add(String val) throws IllegalArgumentException {
        if (val == null) {
            throw new IllegalArgumentException("value must be not null");
        }

        Node current = root;
        current.incSize();

        for (final Character c : val.toCharArray()) {
            Node next = current.getNext(c);
            if (next == null) {
                next = new Node();
                current.setNext(c, next);
            }
            next.incSize();
            current = next;
        }

        if (current.getEnd()) {
            return true;
        } else {
            current.setEnd(true);
            return false;
        }
    }

    public boolean remove(String val) throws IllegalArgumentException {
        if (val == null) {
            throw new IllegalArgumentException("value must be not null");
        }

        Stack<Node> path = new Stack<>();
        path.push(root);

        for (Character c : val.toCharArray()) {
            Node next = path.peek();
            if (next == null) {
                return false;
            }
            path.push(next);
        }

        path.peek().setEnd(false);
        path.peek().decSize();
        Node prev = path.pop();

        while (!path.empty()) {
            Node current = path.pop();
            if (prev.size() == 0) {
                current.setNext(val.charAt(path.size()), null);
            }
            current.decSize();
            prev = current;
        }

        return true;
    }

    public boolean contains(String val) {
        if (val == null) {
            throw new IllegalArgumentException("value must be not null");
        }

        Node current = root;

        for (Character c : val.toCharArray()) {
            if (current.getNext(c) == null) {
                return false;
            } else {
                current = current.getNext(c);
            }
        }

        return current.getEnd();
    }

    public int howManyStartWithPrefix(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("value must be not null");
        }

        Node current = root;

        for (Character c : prefix.toCharArray()) {
            if (current.getNext(c) == null) {
                return 0;
            } else {
                current = current.getNext(c);
            }
        }

        return current.size();
    }


}
