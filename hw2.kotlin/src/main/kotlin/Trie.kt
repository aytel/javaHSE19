package com.aytel

import java.io.InputStream
import java.io.OutputStream
import java.math.BigInteger
import java.util.*

/**
 * A trie of strings.
 * Allows to add and remove elements.
 * Also is serializable with corresponding methods.
 *
 * @constructor Creates empty trie.
 */
class Trie {
    private class Node() {
        internal var size: Int = 0
        internal var end: Boolean = false
        private val edges = HashMap<Char, Node>()

        internal constructor(ins: InputStream) : this() {
            val isEnd: Int = ins.read()
            val arraySize: Int = ins.read()
            val edgesCountArray = ByteArray(arraySize)
            ins.read(edgesCountArray)
            val edgesCount: Int = (BigInteger(edgesCountArray)).toInt()

            this.end = isEnd != 0
            this.size = isEnd

            for (i in 0 until edgesCount) {
                val edge: Char = ins.read().toChar()
                val to = Node(ins)
                edges[edge] = to
                this.size += to.size
            }

        }

        internal fun getNext(c: Char): Node? {
            return edges[c]
        }

        internal fun setNext(c: Char, to: Node?) {
            if (to is Node) {
                edges[c] = to
            } else {
                edges.remove(c)
            }
        }

        internal fun serialize(ots: OutputStream) {
            val isEnd = (if (this.end) 1 else 0)
            val edgesCount = BigInteger.valueOf(edges.size.toLong()).toByteArray()
            val arraySize = edgesCount.size
            ots.write(isEnd)
            ots.write(arraySize)
            ots.write(edgesCount)

            for ((key, value) in edges) {
                ots.write(key.toInt())
                value.serialize(ots)
            }
        }
    }

    private var root = Node()

    /** Returns number of strings in tie. */
    val size: Int
        get() = root.size

    /**
     * Adds value to the trie.
     * @return true in case there was such value in trie and false otherwise.
     */
    fun add(value: String): Boolean {
        var current: Node = root
        current.size++

        value.forEach { c: Char ->
            var next: Node? = current.getNext(c)

            if (next !is Node) {
                next = Node()
                current.setNext(c, next)
            }
            next.size++
            current = next
        }

        return if (current.end) {
            true
        } else {
            current.end = true
            false
        }
    }

    /**
     * Removes value from the trie if such exists.
     * @return true in case there was such value in trie and false otherwise.
     */
    fun remove(value: String): Boolean {
        val path = Stack<Node>()
        path.push(root)

        value.forEach { c: Char -> path.push(path.peek().getNext(c) ?: return false) }

        if (!path.peek().end) {
            return false
        }

        path.peek().end = false
        path.peek().size--
        var prev: Node = path.pop()!!

        while (!path.empty()) {
            val current: Node = path.pop()!!
            if (prev.size == 0) {
                current.setNext(value[path.size], null)
            }
            current.size--
            prev = current
        }

        return true
    }

    /** Returns true in case there is such value in trie and false otherwise. */
    fun contains(value: String): Boolean {
        var current: Node = root

        value.forEach { c: Char -> current = current.getNext(c) ?: return false }

        return current.end
    }

    /** Returns the number of strings in trie which start with the given prefix. */
    fun howManyStartWithPrefix(prefix: String): Int {
        var current: Node = root

        prefix.forEach { c: Char -> current = current.getNext(c) ?: return 0 }

        return current.size
    }

    /** Encodes trie into byte sequence. */
    fun serialize(ots: OutputStream) {
        root.serialize(ots)
    }

    /** Decodes trie from byte sequence. */
    fun deserialize(ins: InputStream) {
        root = Node(ins)
    }
}