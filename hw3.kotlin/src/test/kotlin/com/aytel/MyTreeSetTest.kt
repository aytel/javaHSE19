package com.aytel

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import java.util.*

internal class MyTreeSetTest {

    lateinit var myTreeSet: MyTreeSet<Int>

    @BeforeEach
    fun init() {
        myTreeSet = MyTreeSet(Comparator { a, b -> a - b})
    }

    @Test
    fun comparator() {
        val cur = MyTreeSet<MyTreeSet<Int>>()

        cur.add(myTreeSet)
        assertThrows(ClassCastException::class.java) {cur.add(myTreeSet)}
    }

    @Test
    fun containsAll() {
        (0 until 10).forEach {
            myTreeSet.add(it)
        }
        assertTrue(myTreeSet.containsAll((0 until 10).asSequence().toList()))
        assertFalse(myTreeSet.containsAll((0 until 11).asSequence().toMutableList()))
    }

    @Test
    fun descendingSet() {
        (0 until 10).forEach {
            myTreeSet.add(it)
        }
        assertEquals((9 downTo 0).toList(), myTreeSet.descendingSet().toList())
    }

    @Test
    fun addAll() {
        myTreeSet.addAll((0 until 10))
        assertEquals((0 until 10).toList(), myTreeSet.toList())
    }

    @Test
    fun removeAll() {
        myTreeSet.addAll((0 until 10))
        myTreeSet.removeAll((-10 until 5))
        assertEquals((5 until 10).toList(), myTreeSet.toList())
    }

    @Test
    fun retainAll() {
        myTreeSet.addAll((0 until 10))
        myTreeSet.retainAll((-10 until 5))
        assertEquals((0 until 5).toList(), myTreeSet.toList())
    }

}