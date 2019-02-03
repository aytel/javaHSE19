package com.aytel

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import java.util.*

internal class TreeTest {

    lateinit var tree: Tree<Int>

    @BeforeEach
    fun init() {
        tree = Tree({ a, b -> a - b})
    }

    @Test
    fun `getSize$aytel_hw_main`() {
        assertEquals(0, tree.size)
        tree.add(1)
        assertEquals(1, tree.size)
        tree.add(1)
        assertEquals(1, tree.size)
        tree.remove(1)
        assertEquals(0, tree.size)

        (0 until 10).forEach {
            tree.add(it)
        }
        assertEquals(10, tree.size)
    }

    @Test
    fun `contains$aytel_hw_main`() {
        assertFalse(tree.contains(1))
        tree.add(1)
        assertTrue(tree.contains(1))
        tree.remove(1)
        assertFalse(tree.contains(1))
    }

    @Test
    fun `iterator$aytel_hw_main`() {
        val iter1 = tree.iterator()
        assertThrows(IllegalStateException::class.java) {iter1.remove()}
        assertThrows(NoSuchElementException::class.java) {iter1.next()}
        assertFalse(iter1.hasNext())

        tree.add(1)
        val iter2 = tree.iterator()
        assertTrue(iter2.hasNext())
        assertEquals(1, iter2.next())
        assertFalse(iter2.hasNext())

        (0 until 10).forEach {
            tree.add(it)
        }

        val list = tree.iterator().asSequence().toList()
        assertEquals((0 until 10).iterator().asSequence().toList(), list)

        tree.clear()

        (0 until 3).forEach { i ->
            tree.add(i)
        }

        val iter3 = tree.iterator()
        iter3.next()
        iter3.remove()
        assertThrows(IllegalStateException::class.java) {iter3.remove()}
        assertEquals((1 until 3).iterator().asSequence().toList(), tree.iterator().asSequence().toList())
    }

    @Test
    fun `descendingIterator$aytel_hw_main`() {
        (0 until 10).forEach { tree.add(it) }

        val list = tree.descendingIterator().asSequence().toList()
        assertEquals((0 until 10).sortedDescending(), list)
    }

    @Test
    fun `clear$aytel_hw_main`() {
        (0 until 10).forEach { tree.add(it) }
        tree.clear()
        assertEquals(emptyList<Int>(), tree.iterator().asSequence().toList())
    }

    @Test
    fun `add$aytel_hw_main`() {
        assertFalse(tree.add(1))
        assertTrue(tree.add(1))
        assertTrue(tree.add(1))
    }

    @Test
    fun `remove$aytel_hw_main`() {
        assertFalse(tree.remove(1))
        tree.add(1)
        assertTrue(tree.remove(1))
    }

    @Test
    fun `first$aytel_hw_main`() {
        (0 until 10).forEach {
            tree.add(it)
        }
        assertEquals(0, tree.first())
        tree.remove(0)
        assertEquals(1, tree.first())
    }

    @Test
    fun `last$aytel_hw_main`() {
        (0 until 10).forEach {
            tree.add(it)
        }
        assertEquals(9, tree.last())
        tree.remove(9)
        assertEquals(8, tree.last())
    }

    @Test
    fun `lower$aytel_hw_main`() {
        (0 until 10).forEach {
            tree.add(it)
        }
        assertEquals(0, tree.lower(1))
        assertEquals(null, tree.lower(0))
    }

    @Test
    fun `higher$aytel_hw_main`() {
        (0 until 10).forEach {
            tree.add(it)
        }
        assertEquals(9, tree.higher(8))
        assertEquals(null, tree.higher(9))
    }

    @Test
    fun `floor$aytel_hw_main`() {
        (0 until 10).forEach {
            tree.add(it)
        }
        assertEquals(9, tree.floor(9))
        assertEquals(null, tree.floor(100))
    }

    @Test
    fun `ceiling$aytel_hw_main`() {
        (0 until 10).forEach {
            tree.add(it)
        }
        assertEquals(1, tree.ceiling(1))
        assertEquals(null, tree.ceiling(-100))
    }
}