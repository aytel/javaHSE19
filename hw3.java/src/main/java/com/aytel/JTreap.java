package com.aytel;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class JTreap<T> extends AbstractSet<T> implements JMyTreeSet<T>  {

    private @NotNull Comparator<T> comparator;
    private @NotNull InfoStorage infoStorage;
    private @NotNull JTreap<T> descendingTreap;
    private @NotNull EmptyIterator<T> emptyIterator = new EmptyIterator<>();
    private boolean inverted;

    private JTreap(@NotNull Comparator<T> comparator,
                   @NotNull InfoStorage infoStorage,
                   @NotNull JTreap<T> descendingTreap) {
        this.comparator = comparator;
        this.infoStorage = infoStorage;
        this.descendingTreap = descendingTreap;
        this.inverted = true;
    }

    public JTreap(@NotNull Comparator<T> comparator) {
        this.comparator = comparator;
        this.infoStorage = new InfoStorage();
        this.inverted = false;
        this.descendingTreap = new JTreap<>(comparator, infoStorage, this);
    }

    public JTreap() {
        comparator = (T a, T b) -> ((Comparable<T>)a).compareTo(b);
        this.infoStorage = new InfoStorage();
        this.inverted = false;
        this.descendingTreap = new JTreap<>(comparator, infoStorage, this);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        if (infoStorage.root == null) {
            return emptyIterator;
        } else {
            return (!inverted ? infoStorage.root.iterator() : infoStorage.root.descendingIterator());
        }
    }

    @NotNull
    @Override
    public Iterator<T> descendingIterator() {
        return descendingTreap.iterator();
    }

    @NotNull
    @Override
    public JMyTreeSet<T> descendingSet() {
        return descendingTreap;
    }

    @Override
    public T first() {
        if (infoStorage.root == null) {
            return null;
        } else {
            return (inverted ? infoStorage.root.first().value : infoStorage.root.last().value);
        }
    }

    @Override
    public T last() {
        return descendingTreap.first();
    }

    @Override
    public T lower(@NotNull T element) {
        NodeTriple nodeTriple = splitToThree(element);
        if (!inverted) {
            return (nodeTriple.lower != null ? nodeTriple.lower.last().value : null);
        } else {
            return (nodeTriple.higher != null ? nodeTriple.higher.first().value : null);
        }
    }

    @Override
    public T floor(@NotNull T element) {
        if (contains(element)) {
            return element;
        } else {
            return higher(element);
        }
    }

    @Override
    public T ceiling(@NotNull T element) {
        return descendingTreap.floor(element);
    }

    @Override
    public T higher(@NotNull T element) {
        return descendingTreap.lower(element);
    }

    @Override
    public int size() {
        if (infoStorage.root == null) {
            return 0;
        } else {
            return infoStorage.root.size;
        }
    }

    @Override
    public boolean contains(@NotNull Object element) {
        if (infoStorage.root == null) {
            return false;
        } else {
            return infoStorage.root.contains((T)element);
        }
    }

    @Override
    public boolean add(@NotNull T element) {
        if (contains(element)) {
            return true;
        }

        NodeTriple nodeTriple = splitToThree(element);
        nodeTriple.equal = new Node(element);

        JListNode.setConnection((nodeTriple.lower != null ? nodeTriple.lower.last().jListNode : null),
                nodeTriple.equal.jListNode);
        JListNode.setConnection(nodeTriple.equal.jListNode,
                (nodeTriple.higher != null ? nodeTriple.higher.first().jListNode : null));

        infoStorage.setRoot(nodeTriple.merge());
        return false;
    }

    @Override
    public boolean remove(@NotNull Object element) {
        if (!contains(element)) {
            return false;
        }

        NodeTriple nodeTriple = splitToThree((T)element);
        nodeTriple.equal = null;

        JListNode.setConnection((nodeTriple.lower != null ? nodeTriple.lower.last().jListNode : null),
                (nodeTriple.higher != null ? nodeTriple.higher.first().jListNode : null));

        infoStorage.setRoot(nodeTriple.merge());
        return true;
    }

    private NodePair split(Node node, @NotNull T value) {
        if (node == null) {
            return new NodePair(null, null);
        }
        if (comparator.compare(node.value, value) >= 0) {
            NodePair nodePair = split(node.left, value);
            node.setLeft(nodePair.equalOrHigher);
            nodePair.equalOrHigher = node;
            return nodePair;
        } else {
            NodePair nodePair = split(node.right, value);
            node.setRight(nodePair.lower);
            nodePair.lower = node;
            return nodePair;
        }
    }

    private boolean chooseTop(@NotNull Node first, @NotNull Node second) {
        int randomValue = (new Random()).nextInt();
        randomValue %= first.size + second.size;
        return randomValue < first.size;
    }

    @Contract("!null, _ -> !null; _, !null -> !null")
    private Node merge(Node first, Node second) {
        if (first == null) {
            return second;
        }
        if (second == null) {
            return first;
        }
        if (chooseTop(first, second)) {
            first.setRight(merge(first.right, second));
            return first;
        } else {
            second.setLeft(merge(first, second.left));
            return second;
        }
    }

    @NotNull
    private NodeTriple splitToThree(@NotNull T element) {
        NodePair nodePair = split(infoStorage.root, element);

        if (nodePair.equalOrHigher == null) {
            return new NodeTriple(nodePair.lower, null, null);
        } else {
            if (comparator.compare(nodePair.equalOrHigher.first().value, element) == 0) {
                T next;
                if (nodePair.equalOrHigher.first().jListNode.next == null) {
                    next = null;
                    return new NodeTriple(nodePair.lower, nodePair.equalOrHigher, null);
                } else {
                    next = nodePair.equalOrHigher.first().jListNode.next.element.value;
                    NodePair equalOrHigher = split(nodePair.equalOrHigher, next);
                    return new NodeTriple(nodePair.lower, equalOrHigher.lower, equalOrHigher.equalOrHigher);
                }
            } else {
                return new NodeTriple(nodePair.lower, null, nodePair.equalOrHigher);
            }
        }
    }

    private class InfoStorage {
        int lastModification = 0;
        Node root = null;

        void setRoot(Node value) {
            root = value;
            lastModification++;
        }
    }

    private class Node {
        @NotNull final T value;
        Node left = null, right = null;
        int size = 1;
        @NotNull final JListNode<Node> jListNode = new JListNode<>(this);

        Node(@NotNull T value) {
            this.value = value;
        }

        void setLeft(Node left) {
            this.left = left;
            update();
        }

        void setRight(Node right) {
            this.right = right;
            update();
        }

        void update() {
            this.size = 1 + (left != null ? left.size : 0) + (right != null ? right.size : 0);
        }

        boolean contains(@NotNull T element) {
            if (comparator.compare(value, element) == 0) {
                return true;
            }
            if (comparator.compare(value, element) < 0) {
                return (right != null && right.contains(element));
            }
            return (left != null && left.contains(element));
        }

        @NotNull
        Node first() {
            return (this.left != null ? this.left.first() : this);
        }

        @NotNull
        Node last() {
            return (this.right != null ? this.right.last() : this);
        }

        @NotNull
        Iterator<T> iterator() {
            return new baseIterator(false);
        }

        @NotNull
        Iterator<T> descendingIterator() {
            return new baseIterator(true);
        }

        class baseIterator implements Iterator<T> {
            int modificationIndex = infoStorage.lastModification;
            Node current, previous = null;
            boolean direction;

            baseIterator(boolean direction) {
                this.direction = direction;
                current = (!direction ? first() : last());
            }

            @NotNull
            @Override
            public T next() {
                if (infoStorage.lastModification != modificationIndex) {
                    throw new ConcurrentModificationException();
                }

                if (current == null) {
                    throw new NoSuchElementException();
                } else {
                    T returnValue = current.value;
                    previous = current;
                    JListNode<Node> nextJListNode;
                    if (!direction) {
                        nextJListNode = current.jListNode.next;
                    } else {
                        nextJListNode = current.jListNode.previous;
                    }
                    if (nextJListNode != null) {
                        current = nextJListNode.element;
                    } else {
                        current = null;
                    }
                    return returnValue;
                }
            }

            @Override
            public boolean hasNext() {
                if (infoStorage.lastModification != modificationIndex) {
                    throw new ConcurrentModificationException();
                }
                return current != null;
            }

            @Override
            public void remove() {
                if (infoStorage.lastModification != modificationIndex) {
                    throw new ConcurrentModificationException();
                }

                if (previous != null) {
                    JTreap.this.remove(current.value);
                    previous = null;
                    modificationIndex = infoStorage.lastModification;
                } else {
                    throw new IllegalStateException();
                }
            }
        }

    }

    private class NodePair {
        Node lower, equalOrHigher;

        NodePair(Node lower, Node equalOrHigher) {
            this.lower = lower;
            this.equalOrHigher = equalOrHigher;
        }
    }

    private class NodeTriple {
        Node lower, equal, higher;

        NodeTriple(Node lower, Node equal, Node higher) {
            this.lower = lower;
            this.equal = equal;
            this.higher = higher;
        }

        Node merge() {
            return JTreap.this.merge(lower, JTreap.this.merge(equal, higher));
        }
    }

    private static class EmptyIterator<T> implements Iterator<T> {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public T next() {
            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            throw new IllegalStateException();
        }
    }
}