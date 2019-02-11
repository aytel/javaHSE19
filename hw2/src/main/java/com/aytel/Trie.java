package com.aytel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * A trie of strings.
 * Allows to add and remove elements.
 * Also is serializable with correspondins methods.
 */
public class Trie {

    private Node root = new Node();

    /** Returns number of string in trie. */
    public int size() {
        return root.size();
    }

    /**
     * Adds value to the trie.
     * @return true in case there was such value in trie and false otherwise.
     * @throws IllegalArgumentException if value is null.
     */
    public boolean add(String value) throws IllegalArgumentException {
        if (value == null) {
            throw new IllegalArgumentException("You can't put null String to trie.");
        }

        if (contains(value)) {
            return true;
        }

        Node current = root;
        current.increaseSize();

        for (final Character c : value.toCharArray()) {
            Node next = current.getNext(c);
            if (next == null) {
                next = new Node();
                current.setNext(c, next);
            }
            next.increaseSize();
            current = next;
        }

        current.end = true;
        return false;
    }

    /**
     * Removes value from the trie if such exists.
     * @return true in case there was such value in trie and false otherwise.
     * @throws IllegalArgumentException if value is null.
     */
    public boolean remove(String value) throws IllegalArgumentException {
        if (value == null) {
            throw new IllegalArgumentException("You can't remove null String from trie.");
        }

        var path = new Stack<Node>();
        path.push(root);

        for (Character c : value.toCharArray()) {
            Node next = path.peek().getNext(c);
            if (next == null) {
                return false;
            }
            path.push(next);
        }

        if (!path.peek().end) {
            return false;
        }

        path.peek().end = false;
        path.peek().decreaseSize();
        Node prev = path.pop();

        while (!path.empty()) {
            Node current = path.pop();
            if (prev.size() == 0) {
                current.setNext(value.charAt(path.size()), null);
            }
            current.decreaseSize();
            prev = current;
        }

        return true;
    }

    /** Returns true in case there is such value in trie and false otherwise.
     * @throws IllegalArgumentException if value is null.
     */
    public boolean contains(String value) throws IllegalArgumentException {
        if (value == null) {
            throw new IllegalArgumentException("You can't search for null String in trie.");
        }

        Node current = root;

        for (Character c : value.toCharArray()) {
            if (current.getNext(c) == null) {
                return false;
            } else {
                current = current.getNext(c);
            }
        }

        return current.end;
    }

    /** Returns the number of strings in trie which start with the given prefix.
     * @throws IllegalArgumentException if prefix is null.
     */
    public int howManyStartWithPrefix(String prefix) throws IllegalArgumentException {
        if (prefix == null) {
            throw new IllegalArgumentException("Prefix to search must be not null.");
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

    /** Encodes trie into byte sequence. */
    public void serialize(OutputStream out) throws IOException {
        root.serialize(out);
    }

    /** Decodes trie from byte sequence. */
    public void deserialize(InputStream in) throws IOException {
        root = new Node(in);
    }

    private class Node {
        private int size = 0;
        private boolean end = false;
        private HashMap<Character, Node> edges = new HashMap<>();

        private Node(){}

        private Node(InputStream in) throws IOException {
            int endToInt = in.read();
            int arraySize = in.read();
            var edgesCountArray = new byte[arraySize];
            in.read(edgesCountArray);
            var edgesCount = (new BigInteger(edgesCountArray)).intValue();

            this.end = (endToInt != 0);
            this.size = endToInt;

            for (int i = 0; i < edgesCount; i++) {
                var edge = (char)in.read();
                Node to = new Node(in);
                edges.put(edge, to);
                this.size += to.size();
            }
        }

        private int size() {
            return this.size;
        }

        private void increaseSize() {
            size++;
        }

        private void decreaseSize() {
            size--;
        }

        private Node getNext(Character c) {
            return edges.get(c);
        }

        private void setNext(Character c, Node to) {
            edges.put(c, to);
        }

        private void serialize(OutputStream out) throws IOException {
            int endToInt = this.end ? 1 : 0;
            var edgesCount = BigInteger.valueOf(edges.size()).toByteArray();
            int arraySize = edgesCount.length;
            out.write(endToInt);
            out.write(arraySize);
            out.write(edgesCount);

            for (Map.Entry<Character, Node> entry : edges.entrySet()) {
                out.write((int)entry.getKey());
                entry.getValue().serialize(out);
            }
        }
    }
}
