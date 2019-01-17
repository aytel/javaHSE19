package HashTable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class MyHashTableTest {

    @Test
    void size() {
        MyHashTable myHashTable = new MyHashTable();
        myHashTable.put("a", "b");
        assertEquals(1, myHashTable.size());
        myHashTable.put("a", "c");
        assertEquals(1, myHashTable.size());
        myHashTable.put("b", "a");
        assertEquals(2, myHashTable.size());
    }

    @Test
    void contains() {
        MyHashTable myHashTable = new MyHashTable();
        myHashTable.put("a", "b");
        assertTrue(myHashTable.contains("a"));
        assertFalse(myHashTable.contains("b"));
    }

    @Test
    void get() {
        MyHashTable myHashTable = new MyHashTable();
        myHashTable.put("a", "b");
        assertEquals("b", myHashTable.get("a"));
    }

    @Test
    void put() {
        MyHashTable myHashTable = new MyHashTable();
        myHashTable.put("a", "a");
        assertEquals(1, myHashTable.size());
        myHashTable.put("a", "b");
        assertEquals(1, myHashTable.size());
    }

    @Test
    void remove() {
        MyHashTable myHashTable = new MyHashTable();
        myHashTable.put("a", "a");
        myHashTable.remove("a");
        assertNull(myHashTable.get("a"));
    }

    @Test
    void clear() {
        MyHashTable myHashTable = new MyHashTable();
        myHashTable.put("a", "a");
        myHashTable.clear();
        assertNull(myHashTable.get("a"));
        assertEquals(0, myHashTable.size());
    }
}