package com.aytel

/** Node of doubly linked list. */
internal class ListNode<T>(internal val element: T) {
    internal var next: ListNode<T>? = null
    internal var previous: ListNode<T>? = null

    internal companion object {
        fun<T> setConnection(first: ListNode<T>?, second: ListNode<T>?) {
            first?.next = second
            second?.previous = first
        }
    }
}