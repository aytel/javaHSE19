package com.aytel

/** Simple treap with added iterators. */
internal class Treap<T>(val compare: (T, T) -> Int,
                        private val inverted: Boolean = false,
                        descendingTree: Treap<T>? = null) {

    internal val descendingTree: Treap<T>

    private var lastModification: Int = 0
        set(value) {
            field = value
            if (descendingTree.lastModification != value) {
                descendingTree.lastModification = value
            }
        }

    private var root: Node? = null
        set(value) {
            field = value
            if (descendingTree.root != value) {
                descendingTree.root = value
            }
        }


    init {
        if (descendingTree == null) {
            this.descendingTree = Treap(compare, !inverted, this)
        } else {
            this.descendingTree = descendingTree
        }
    }

    private operator fun T.compareTo(other: T) = compare(this, other)

    private fun chooseTop(first: Node, second: Node): Boolean =
        (0..(first.size + second.size)).random() < first.size

    private fun merge(first: Node?, second: Node?): Node? {
        if (first == null) {
            return second
        }
        if (second == null) {
            return first
        }
        return if (chooseTop(first, second)) {
            first.right = merge(first.right, second)
            first
        } else {
            second.left = merge(first, second.left)
            second
        }
    }

    private fun split(node: Node?, value: T): Pair<Node?, Node?> {
        if (node == null) {
            return Pair(null, null)
        }
        return if (node.element >= value) {
            val temp = split(node.left, value)
            node.left = temp.second
            temp.copy(second = node)
        } else {
            val temp = split(node.right, value)
            node.right = temp.first
            temp.copy(first = node)
        }
    }

    internal val size: Int
        get() = root?.size ?: 0

    internal fun contains(element: T): Boolean = floor(element)?.compareTo(element) == 0

    internal operator fun iterator(): MutableIterator<T> = (if (inverted) root?.descendingIterator() else root?.iterator())
        ?: invalidIterator

    internal fun descendingIterator(): MutableIterator<T> = (if (inverted) root?.iterator() else root?.descendingIterator())
        ?: invalidIterator

    internal fun clear() {
        root = null
    }

    private fun splitToThree(element: T): NodeTriple<T> {
        val (lower, equalOrHigher) = split(root, element)

        return if (equalOrHigher == null) {
            NodeTriple(lower, null, null)
        } else {
            if (equalOrHigher.first().element.compareTo(element) == 0) {
                val next: T? = equalOrHigher.first().listNode.next?.element?.element
                if (next != null) {
                    val (equal, higher) = split(equalOrHigher, next)
                    NodeTriple(lower, equal, higher)
                } else {
                    NodeTriple(lower, equalOrHigher, null)
                }
            } else {
                NodeTriple(lower, null, equalOrHigher)
            }
        }

    }

    internal fun add(element: T): Boolean {
        val (lower, equal, higher) = splitToThree(element)

        val current = Node(element)

        ListNode.setConnection(lower?.last()?.listNode, current.listNode)
        ListNode.setConnection(current.listNode, higher?.first()?.listNode)

        root = merge(lower, merge(current, higher))!!

        return if (equal != null) {
            lastModification++
            true
        } else {
            false
        }
    }

    internal fun remove(element: T): Boolean {
        val (lower, equal, higher) = splitToThree(element)

        ListNode.setConnection(lower?.last()?.listNode, higher?.first()?.listNode)

        root = merge(lower, higher)

        return if (equal != null) {
            lastModification++
            true
        } else {
            false
        }
    }

    internal fun first(): T? = if (!inverted) root?.first()?.element else root?.last()?.element

    internal fun last(): T? = if (inverted) root?.first()?.element else root?.last()?.element

    internal fun lower(element: T): T? {
        val (lower, equal, higher) = splitToThree(element)
        val returnValue = if (!inverted) {
            lower?.last()?.element
        } else {
            higher?.first()?.element
        }
        root = merge(lower, merge(equal, higher))
        return returnValue
    }

    internal fun higher(element: T): T? {
        val (lower, eq, higher) = splitToThree(element)
        val returnValue = if (!inverted) {
            higher?.first()?.element
        } else {
            lower?.last()?.element
        }
        root = merge(lower, merge(eq, higher))
        return returnValue
    }

    internal fun floor(element: T): T? {
        val (lower, eq, higher) = splitToThree(element)
        val returnValue = if (!inverted) {
            eq?.element ?: higher?.first()?.element
        } else {
            eq?.element ?: lower?.last()?.element
        }
        root = merge(lower, merge(eq, higher))
        return returnValue
    }

    internal fun ceiling(element: T): T? {
        val (lower, eq, higher) = splitToThree(element)
        val returnValue = if (!inverted) {
            eq?.element ?: lower?.last()?.element
        } else {
            eq?.element ?: higher?.first()?.element
        }
        root = merge(lower, merge(eq, higher))
        return returnValue
    }

    private inner class Node(val element: T) {
        internal var left: Node? = null
            set(value) {
                field = value
                update()
            }

        internal var right: Node? = null
            set(value) {
                field = value
                update()
            }

        internal var size: Int = 1
            private set
        internal val listNode: ListNode<Node> = ListNode(this)

        internal fun update() {
            size = 1 + (left?.size ?: 0) + (right?.size ?: 0)
        }

        private fun baseIterator(direction: Boolean) = object : MutableIterator<T> {
            private var modificationIndex = lastModification
            private var current: Treap<T>.Node? = if (!direction) this@Node.first() else this@Node.last()
            private var previous: Treap<T>.Node? = null

            override fun next(): T {
                if (lastModification != modificationIndex) {
                    throw ConcurrentModificationException()
                }

                if (current == null) {
                    throw NoSuchElementException()
                } else {
                    val ret = current!!.element
                    previous = current
                    current = if (!direction) current!!.listNode.next?.element else current!!.listNode.previous?.element
                    return ret
                }
            }

            override fun hasNext(): Boolean {
                if (lastModification != modificationIndex) {
                    throw ConcurrentModificationException()
                }
                return current != null
            }

            override fun remove() {
                if (lastModification != modificationIndex) {
                    throw ConcurrentModificationException()
                }

                if (previous != null) {
                    this@Treap.remove(previous!!.element)
                    previous = null
                    modificationIndex = lastModification
                } else {
                    throw IllegalStateException()
                }
            }
        }

        internal fun iterator(): MutableIterator<T> = baseIterator(false)

        internal fun descendingIterator(): MutableIterator<T> = baseIterator(true)

        internal fun first(): Node = this.left?.first() ?: this

        internal fun last(): Node = this.right?.last() ?: this
    }

    private data class NodeTriple<T>(val lower: Treap<T>.Node?, val equal: Treap<T>.Node?, val higher: Treap<T>.Node?)

    private val invalidIterator = object : MutableIterator<T> {
        override fun hasNext(): Boolean = false
        override fun next(): T = throw NoSuchElementException()
        override fun remove() = throw IllegalStateException()
    }
}