package com.aytel.Trie;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Trie {
    private class Node {
        private int size = 0;
        private boolean end = false;
        private HashMap<Character, Node> edges = new HashMap<>();

        Node(){}

        Node(InputStream in) throws IOException {
            int isEnd = in.read();
            int arraySize = in.read();
            var edgesCountArray = new byte[arraySize];
            in.read(edgesCountArray);
            int edgesCount = (new BigInteger(edgesCountArray)).intValue();

            this.end = (isEnd != 0);
            this.size = isEnd;

            for (int i = 0; i < edgesCount; i++) {
                char edge = (char)in.read();
                Node to = new Node(in);
                edges.put(edge, to);
                this.size += to.size();
            }
        }

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

        void serialize(OutputStream out) throws IOException {
            byte isEnd = (byte)(this.end ? 1 : 0);
            byte[] edgesCount = BigInteger.valueOf(edges.size()).toByteArray();
            byte arraySize = (byte)edgesCount.length;
            out.write(isEnd);
            out.write(arraySize);
            out.write(edgesCount);

            for (Map.Entry<Character, Node> entry : edges.entrySet()) {
                out.write((byte)entry.getKey().charValue());
                entry.getValue().serialize(out);
            }
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
            Node next = path.peek().getNext(c);
            if (next == null) {
                return false;
            }
            path.push(next);
        }

        if (!path.peek().getEnd()) {
            return false;
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

    public void serialize(OutputStream out) throws IOException {
        root.serialize(out);
    }

    public void deserialize(InputStream in) throws IOException {
        root = new Node(in);
    }
}
