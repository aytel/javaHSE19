package com.aytel

import java.io.InputStream
import java.io.OutputStream
import java.math.BigInteger

/**
 * A trie of strings.
 * Allows to add and remove elements.
 * Also is serializable with correspondins methods.
 */
public class Trie() {
    private class Node(){
        internal var size: Int = 0
        internal var end: Boolean = false
        private val edges = HashMap<Char, Node>()

        internal constructor(ins : InputStream) : this() {
            val isEnd: Int = ins.read()
            val arraySize: Int = ins.read()
            val edgesCountArray = ByteArray(arraySize)
            ins.read(edgesCountArray)
            val edgesCount: Int = (BigInteger(edgesCountArray)).toInt()

            this.end = isEnd != 0
            this.size = isEnd

            for (i in 0 until edgesCount) {
                val edge: Char = ins.read().toChar()
                val to: Node = Node(ins)
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

    public fun size(): Int {
        return root.size
    }

    public fun add(value: String): Boolean {
        var current: Node = root
        current.size++

        for (c: Char in value) {
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
}