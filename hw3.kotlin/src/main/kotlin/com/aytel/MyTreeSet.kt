package com.aytel

import java.lang.ClassCastException
import kotlin.Comparator
import kotlin.NoSuchElementException

class MyTreeSet<T> private constructor(private val comparator: Comparator<T>, private val tree: Tree<T>) : MutableSet<T> {

    constructor(comparator: Comparator<T> = Comparator { a: T, b: T ->
        (a as? Comparable<T>) ?: throw ClassCastException()
        a.compareTo(b)
    }) : this(comparator, Tree({ a: T, b: T -> comparator.compare(a, b)}))

    override val size: Int
        get() = tree.size

    override fun contains(element: T): Boolean = tree.contains(element)

    override fun containsAll(elements: Collection<T>): Boolean = elements.fold(true) { prev, element ->
        prev and contains(element)
    }

    override fun isEmpty(): Boolean = size == 0

    override fun iterator(): MutableIterator<T> = tree.iterator()

    fun descendingIterator(): MutableIterator<T> = tree.descendingIterator()

    fun descendingSet(): MyTreeSet<T> = MyTreeSet(comparator, Tree<T>({a, b -> comparator.compare(a, b)}, true))

    override fun add(element: T): Boolean = tree.add(element)

    override fun addAll(elements: Collection<T>): Boolean = elements.fold(false) { prev, element ->
        prev or add(element)
    }

    override fun clear() {
        tree.clear()
    }

    override fun remove(element: T): Boolean = tree.remove(element)

    override fun removeAll(elements: Collection<T>): Boolean = elements.fold(false) { prev, element ->
        prev or remove(element)
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        val newTreeSet: MyTreeSet<T> = MyTreeSet(comparator)
        newTreeSet.addAll(this)
        newTreeSet.removeAll(elements)
        return removeAll(newTreeSet)
    }

    fun first(): T? = tree.first()

    fun last(): T? = tree.last()

    fun lower(element: T): T? = tree.lower(element)

    fun higher(element: T): T? = tree.higher(element)

    fun floor(element: T): T? = tree.floor(element)

    fun ceiling(element: T): T? = tree.ceiling(element)

}