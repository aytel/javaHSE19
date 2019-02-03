package com.aytel

import java.lang.ClassCastException
import java.util.*
import kotlin.Comparator
import kotlin.NoSuchElementException

/**
 * Implements [kotlin.collections.MutableSet] interface with internal [Tree], which is simple treap.
 * Compares all elements using natural order or with comparator, given in the constructor.
 * All methods can throw [kotlin.ClassCastException] in case types are not comparable.
 *
 * @constructor Creates MyTreeSet with given comparator and given internal [Tree].
 */
class MyTreeSet<T> private constructor(private val comparator: Comparator<T>, private val tree: Tree<T>) : MutableSet<T> {

    /** Creates empty MyTreeSet with given comparator or with default in case it was called without arguments. */
    constructor(comparator: Comparator<T> = Comparator { a: T, b: T ->
        @Suppress("UNCHECKED_CAST")
        (a as? Comparable<T>) ?: throw ClassCastException()
        a.compareTo(b)
    }) : this(comparator, Tree({ a: T, b: T -> comparator.compare(a, b)}))

    /** [kotlin.collections.MutableSet.size] */
    override val size: Int
        get() = tree.size

    /** [kotlin.collections.MutableSet.contains] */
    override fun contains(element: T): Boolean = tree.contains(element)

    /** [kotlin.collections.MutableSet.containsAll] */
    override fun containsAll(elements: Collection<T>): Boolean = elements.fold(true) { prev, element ->
        prev.and(contains(element))
    }

    /** [kotlin.collections.MutableSet.isEmpty] */
    override fun isEmpty(): Boolean = size == 0

    /** [kotlin.collections.MutableSet.iterator] */
    override fun iterator(): MutableIterator<T> = tree.iterator()

    /** [java.util.TreeSet.descendingIterator] */
    fun descendingIterator(): MutableIterator<T> = tree.descendingIterator()

    /** [java.util.TreeSet.descendingSet] */
    fun descendingSet(): MyTreeSet<T> = MyTreeSet(comparator, Tree(tree, !tree.inverted))

    /** [kotlin.collections.MutableSet.add] */
    override fun add(element: T): Boolean = tree.add(element)

    /** [kotlin.collections.MutableSet.addAll] */
    override fun addAll(elements: Collection<T>): Boolean = elements.fold(false) { prev, element ->
        prev or add(element)
    }

    /** [kotlin.collections.MutableSet.clear] */
    override fun clear() {
        tree.clear()
    }

    /** [kotlin.collections.MutableSet.remove] */
    override fun remove(element: T): Boolean = tree.remove(element)

    /** [kotlin.collections.MutableSet.removeAll] */
    override fun removeAll(elements: Collection<T>): Boolean = elements.fold(false) { prev, element ->
        prev or remove(element)
    }

    /** [kotlin.collections.MutableSet.retainAll] */
    override fun retainAll(elements: Collection<T>): Boolean {
        val newTreeSet: MyTreeSet<T> = MyTreeSet(comparator)
        newTreeSet.addAll(this)
        newTreeSet.removeAll(elements)

        return removeAll(newTreeSet)
    }

    /** [kotlin.collections.MutableSet.first] */
    fun first(): T? = tree.first()

    /** [kotlin.collections.MutableSet.last] */
    fun last(): T? = tree.last()

    /** [java.util.TreeSet.lower] */
    fun lower(element: T): T? = tree.lower(element)

    /** [java.util.TreeSet.higher] */
    fun higher(element: T): T? = tree.higher(element)

    /** [java.util.TreeSet.floor] */
    fun floor(element: T): T? = tree.floor(element)

    /** [java.util.TreeSet.ceiling] */
    fun ceiling(element: T): T? = tree.ceiling(element)

}