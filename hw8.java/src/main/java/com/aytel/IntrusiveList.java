package com.aytel;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/** Double-linked intrusive list created to keep sequence of objects, iterate over them and erase them. */
class IntrusiveList<T extends IntrusiveList.IntrusiveContainer> implements Iterable<T> {
    private IntrusiveNode<T> root = null;

    void add(T container) {
        var node = new IntrusiveNode<>(container);
        container.node = node;
        container.list = this;

        node.next = root;
        if (root != null) {
            root.prev = node;
        }

        root = node;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private IntrusiveNode<T> current = root;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                T result = current.content;
                current = current.next;
                return result;
            }
        };
    }

    static class IntrusiveNode<T> {
        IntrusiveNode<T> prev = null, next = null;
        final T content;

        IntrusiveNode(T content) {
            this.content = content;
        }
    }

    static abstract class IntrusiveContainer<K extends IntrusiveContainer<K>> {
        IntrusiveNode<K> node;
        IntrusiveList<K> list;

        void removeFromList() {
            IntrusiveNode<K> prev = node.prev, next = node.next;

            if (prev != null) {
                prev.next = next;
            } else {
                list.root = next;
            }

            if (next != null) {
                next.prev = prev;
            }
        }
    }
}
