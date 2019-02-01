package com.aytel

class ListNode<T>(internal val element: T) {
    internal var next: ListNode<T>? = null
    internal var prev: ListNode<T>? = null

    internal companion object {
        fun <T>setConnection(first: ListNode<T>?, second: ListNode<T>?) {
            if ((first == null) or (second == null)) {
                return
            } else {
                first!!.next = second
                second!!.prev = first
            }
        }
    }
}