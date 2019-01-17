package HashTable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyListPairStringStringTest {

    @Test
    void contains() {
        MyListPairStringString myListPairStringString = new MyListPairStringString();
        myListPairStringString.put("a", "b");
        assertTrue(myListPairStringString.contains("a"));
        assertFalse(myListPairStringString.contains("b"));
    }

    @Test
    void get() {
        MyListPairStringString myListPairStringString = new MyListPairStringString();
        myListPairStringString.put("a", "b");
        assertEquals("b", myListPairStringString.get("a"));
        assertNull(myListPairStringString.get("b"));
    }

    @Test
    void put() {
        MyListPairStringString myListPairStringString = new MyListPairStringString();
        myListPairStringString.put("a", "b");
        assertNotNull(myListPairStringString.head);
    }

    @Test
    void remove() {
        MyListPairStringString myListPairStringString = new MyListPairStringString();
        myListPairStringString.put("a", "a");
        myListPairStringString.remove("a");
        assertNull(myListPairStringString.get("a"));
    }

    @Test
    void clear() {
        MyListPairStringString myListPairStringString = new MyListPairStringString();
        myListPairStringString.put("a", "a");
        myListPairStringString.clear();
        assertNull(myListPairStringString.get("a"));
        assertNull(myListPairStringString.head);
    }
}