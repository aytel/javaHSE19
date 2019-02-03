package com.aytel

internal class Tree<T>(val compare: (T, T) -> Int, val inverted: Boolean = false) {

    internal constructor(tree: Tree<T>, inverted: Boolean) : this(tree.compare, inverted) {
        this.root = tree.root
        this.lastModification = tree.lastModification
    }

    private operator fun T.compareTo(other: T) = compare(this, other)

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

        internal fun iterator(): MutableIterator<T> = object : MutableIterator<T> {
            private var modificationIndex = lastModification
            private var cur: Tree<T>.Node? = this@Node.first()
            private var prev: Tree<T>.Node? = null

            override fun next(): T {
                if (lastModification != modificationIndex) {
                    throw ConcurrentModificationException()
                }

                if (cur == null) {
                    throw NoSuchElementException()
                } else {
                    val ret = cur!!.element
                    prev = cur
                    cur = cur!!.listNode.next?.element
                    return ret
                }
            }

            override fun hasNext(): Boolean {
                if (lastModification != modificationIndex) {
                    throw ConcurrentModificationException()
                }
                return cur != null
            }

            override fun remove() {
                if (lastModification != modificationIndex) {
                    throw ConcurrentModificationException()
                }

                if (prev != null) {
                    this@Tree.remove(prev!!.element)
                    prev = null
                    modificationIndex = lastModification
                } else {
                    throw IllegalStateException()
                }
            }
        }

        internal fun descendingIterator(): MutableIterator<T> = object : MutableIterator<T> {
            private var modificationIndex = lastModification
            private var cur: Tree<T>.Node? = this@Node.last()
            private var prev: Tree<T>.Node? = null

            override fun next(): T {
                if (lastModification != modificationIndex) {
                    throw ConcurrentModificationException()
                }

                if (cur == null) {
                    throw NoSuchElementException()
                } else {
                    val ret = cur!!.element
                    prev = cur
                    cur = cur!!.listNode.prev?.element
                    return ret
                }
            }

            override fun hasNext(): Boolean {
                if (lastModification != modificationIndex) {
                    throw ConcurrentModificationException()
                }
                return cur != null
            }

            override fun remove() {
                if (lastModification != modificationIndex) {
                    throw ConcurrentModificationException()
                }

                if (prev != null) {
                    this@Tree.remove(prev!!.element)
                    prev = null
                    modificationIndex = lastModification
                } else {
                    throw IllegalStateException()
                }
            }
        }

        internal fun first(): Node = this.left?.first() ?: this

        internal fun last(): Node = this.right?.last() ?: this
    }

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

    private var lastModification: Int = 0
    private var root: Node? = null
        set(value) {
            field = value
            lastModification++
        }

    internal val size: Int
        get() = root?.size ?: 0

    internal fun contains(element: T): Boolean = floor(element)?.compareTo(element) == 0

    internal operator fun iterator(): MutableIterator<T> = (if (inverted) root?.descendingIterator() else root?.iterator()) ?: object: MutableIterator<T> {
        override fun hasNext(): Boolean = false
        override fun next(): T = throw NoSuchElementException()
        override fun remove() = throw IllegalStateException()
    }

    internal fun descendingIterator(): MutableIterator<T> = (if (inverted) root?.iterator() else root?.descendingIterator()) ?: object: MutableIterator<T> {
        override fun hasNext(): Boolean = false
        override fun next(): T = throw NoSuchElementException()
        override fun remove() = throw IllegalStateException()
    }

    internal fun clear() {
        root = null
    }

    private data class NodeTriple<T>(val lower: Tree<T>.Node?, val eq: Tree<T>.Node?, val higher: Tree<T>.Node?)

    private fun splitToThree(element: T): NodeTriple<T> {
        val (lower, eqOrHigher) = split(root, element)

        return if (eqOrHigher == null) {
            NodeTriple(lower, null, null)
        } else {
            if (eqOrHigher.first().element.compareTo(element) == 0) {
                val next: T? = eqOrHigher.first().listNode.next?.element?.element
                if (next != null) {
                    val (eq, higher) = split(eqOrHigher, next)
                    NodeTriple(lower, eq, higher)
                } else {
                    NodeTriple(lower, eqOrHigher, null)
                }
            } else {
                NodeTriple(lower, null, eqOrHigher)
            }
        }

    }

    internal fun add(element: T): Boolean {
        val (lower, eq, higher) = splitToThree(element)

        val cur = Node(element)

        ListNode.setConnection(lower?.last()?.listNode, cur.listNode)
        ListNode.setConnection(cur.listNode, higher?.first()?.listNode)

        root = merge(lower, merge(cur, higher))!!

        return eq?.element?.equals(element) == true
    }

    internal fun remove(element: T): Boolean {
        val (lower, eq, higher) = splitToThree(element)

        ListNode.setConnection(lower?.last()?.listNode, higher?.first()?.listNode)

        root = merge(lower, higher)

        return eq?.element?.equals(element) == true
    }

    internal fun first(): T? = if (!inverted) root?.first()?.element else root?.last()?.element

    internal fun last(): T? = if (inverted) root?.first()?.element else root?.last()?.element

    internal fun lower(element: T): T? {
        val (lower, _, higher) = splitToThree(element)
        return if (!inverted) {
            lower?.last()?.element
        } else {
            higher?.first()?.element
        }
    }

    internal fun higher(element: T): T? {
        val (lower, _, higher) = splitToThree(element)
        return if (!inverted) {
            higher?.first()?.element
        } else {
            lower?.last()?.element
        }
    }

    internal fun floor(element: T): T? {
        val (lower, eq, higher) = splitToThree(element)
        return if (!inverted) {
            eq?.element ?: higher?.first()?.element
        } else {
            eq?.element ?: lower?.last()?.element
        }
    }

    internal fun ceiling(element: T): T? {
        val (lower, eq, higher) = splitToThree(element)
        return if (!inverted) {
            eq?.element ?: lower?.last()?.element
        } else {
            eq?.element ?: higher?.first()?.element
        }
    }
}