package com.aytel;

import org.jetbrains.annotations.NotNull;

class JListNode<T> {
    final T element;
    JListNode<T> next = null, previous = null;

    JListNode(@NotNull T element) {
        this.element = element;
    }

    static <T>void setConnection(JListNode<T> first, JListNode<T> second) {
        if (first != null) {
            first.next = second;
        }
        if (second != null) {
            second.previous = first;
        }
    }

}