package com.aytel;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.collect.Lists;

class JTreapTest {
    private JTreap<Integer> treap;

    @BeforeEach
    void init() {
        treap = new JTreap<>();
    }

    @Test
    void size() {
        assertEquals(0, treap.size());
        treap.add(1);
        assertEquals(1, treap.size());
        treap.add(1);
        assertEquals(1, treap.size());
        treap.remove(1);
        assertEquals(0, treap.size());
    }

    @Test
    void iterator() {
        Iterator<Integer> iter1 = treap.iterator();
        assertFalse(iter1.hasNext());

        treap.add(1);
        Iterator<Integer> iter2 = treap.iterator();
        assertTrue(iter2.hasNext());
        assertEquals(Integer.valueOf(1), iter2.next());
        assertFalse(iter2.hasNext());

        IntStream.range(0, 10).forEach(i -> treap.add(i));
        List<Integer> list = Lists.newArrayList(treap.iterator());
        assertEquals(IntStream.range(0, 10).boxed().collect(Collectors.toList()), list);
    }

    @Test
    void descendingIterator() {
        IntStream.range(0, 10).forEach(i -> treap.add(i));
        List<Integer> list = Lists.newArrayList(treap.descendingIterator());
        assertEquals(Lists.reverse(IntStream.range(0, 10).boxed().collect(Collectors.toList())), list);
    }

    @Test
    void descendingSet() {
        IntStream.range(0, 10).forEach(i -> treap.add(i));
        List<Integer> list = Lists.newArrayList(treap.descendingSet().iterator());
        assertEquals(Lists.reverse(IntStream.range(0, 10).boxed().collect(Collectors.toList())), list);
    }

    @Test
    void contains() {
        assertFalse(treap.contains(0));
        treap.add(0);
        assertTrue(treap.contains(0));
        treap.remove(0);
        assertFalse(treap.contains(0));
    }

    @Test
    void add() {
        assertTrue(treap.add(1));
        assertFalse(treap.add(1));
        assertFalse(treap.add(1));
    }

    @Test
    void remove() {
        assertFalse(treap.remove(1));
        treap.add(1);
        assertTrue(treap.remove(1));
    }

    @Test
    void comparator() {
        JTreap<Object> jTreapWithDefaultComparator = new JTreap<>();
        jTreapWithDefaultComparator.add(0);
        jTreapWithDefaultComparator.add(1);
        assertThrows(ClassCastException.class, () -> jTreapWithDefaultComparator.add(""));
        jTreapWithDefaultComparator.add(10);
        assertEquals(10, jTreapWithDefaultComparator.higher(5));
        assertEquals(0, jTreapWithDefaultComparator.descendingSet().last());

        JTreap<String> jTreapWithCustomComparator = new JTreap<>(Comparator.comparingInt(String::length));
        jTreapWithCustomComparator.add("0");
        jTreapWithCustomComparator.add("1");
        assertEquals(1, jTreapWithCustomComparator.size());
        assertEquals("0", jTreapWithCustomComparator.ceiling(""));

    }
}