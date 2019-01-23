package com.aytel.Trie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrieTest {

    Trie trie;

    @BeforeEach
    void init() {
        trie = new Trie();
    }

    @Test
    void size() {
        assertEquals(0, trie.size());
        trie.add("");
        assertEquals(1, trie.size());
    }

    @Test
    void add() {
        assertFalse(trie.add("aba"));
        assertTrue(trie.add("aba"));
        assertFalse(trie.add(""));
        assertThrows(IllegalArgumentException.class, () -> trie.add(null));
    }

    @Test
    void remove() {
        trie.add("");
        assertTrue(trie.remove(""));
        assertFalse(trie.remove("abc"));
        trie.add("abc");
        assertFalse(trie.remove("ab"));
        assertEquals(1, trie.size());
        assertThrows(IllegalArgumentException.class, () -> trie.remove(null));
    }

    @Test
    void contains() {
        trie.add("abc");
        assertTrue(trie.contains("abc"));
        assertFalse(trie.contains("ab"));
    }

    @Test
    void howManyStartWithPrefix() {
        trie.add("");
        trie.add("ab");
        assertEquals(2, trie.howManyStartWithPrefix(""));
        assertEquals(1, trie.howManyStartWithPrefix("a"));
        trie.remove("ab");
        assertEquals(0, trie.howManyStartWithPrefix("a"));
    }
}