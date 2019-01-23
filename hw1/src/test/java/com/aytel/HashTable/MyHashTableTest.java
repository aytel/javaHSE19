package com.aytel.HashTable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyHashTableTest {

    private MyHashTable myHashTable;

    @BeforeEach
    void initialize() {
        myHashTable = new MyHashTable();
    }

    @Test
    void size() {
        myHashTable.put("a", "b");
        assertEquals(1, myHashTable.size());
        myHashTable.put("a", "c");
        assertEquals(1, myHashTable.size());
        myHashTable.put("b", "a");
        assertEquals(2, myHashTable.size());
    }

    @Test
    void contains() {
        myHashTable.put("a", "b");
        assertTrue(myHashTable.contains("a"));
        assertFalse(myHashTable.contains("b"));
    }

    @Test
    void get() {
        myHashTable.put("a", "b");
        assertEquals("b", myHashTable.get("a"));
    }

    @Test
    void put() {
        myHashTable.put("a", "a");
        assertEquals(1, myHashTable.size());
        assertEquals("a", myHashTable.put("a", "b"));
        assertEquals(1, myHashTable.size());
        assertThrows(IllegalArgumentException.class, () -> myHashTable.put("a", null));
        assertThrows(IllegalArgumentException.class, () -> myHashTable.put(null, "a"));
    }

    @Test
    void remove() {
        myHashTable.put("a", "a");
        assertEquals("a", myHashTable.remove("a"));
        assertNull(myHashTable.remove("b"));
        assertNull(myHashTable.get("a"));
    }

    @Test
    void clear() {
        myHashTable.put("a", "a");
        myHashTable.clear();
        assertNull(myHashTable.get("a"));
        assertEquals(0, myHashTable.size());
    }
}