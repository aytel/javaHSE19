package com.aytel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TrieTest {

    private Trie trie;

    @BeforeEach
    void init() {
        trie = new Trie();
    }

    @Test
    void illegalArgumentExceptions() {
        assertThrows(IllegalArgumentException.class, () -> trie.add(null));
        assertThrows(IllegalArgumentException.class, () -> trie.remove(null));
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
    }

    @Test
    void remove() {
        trie.add("");
        assertTrue(trie.remove(""));
        assertFalse(trie.remove("abc"));
        trie.add("abc");
        assertFalse(trie.remove("ab"));
        assertEquals(1, trie.size());
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

    @Test
    void serialize() throws IOException {
        trie.add("a");
        trie.add("b");
        trie.add("");

        var baos = new ByteArrayOutputStream(1000);
        trie.serialize(baos);

        byte[] check = {1, 1, 2, 97, 1, 1, 0, 98, 1, 1, 0};
        byte[] result = baos.toByteArray();
        assertArrayEquals(check, result);
    }

    @Test
    void deserialize() throws IOException {
        byte[] input = {1, 1, 1, 99, 1, 1, 0};

        trie.deserialize(new ByteArrayInputStream(input));

        assertTrue(trie.contains(""));
        assertTrue(trie.contains("c"));
        assertEquals(2, trie.size());
    }

    @Test
    void serializationSynchronization() throws IOException {
        trie.add("a");
        trie.add("b");
        trie.add("abc");
        trie.add("qq!'~");
        trie.add("");

        var baos = new ByteArrayOutputStream(1000);
        trie.serialize(baos);

        var gotTrie = new Trie();
        gotTrie.deserialize(new ByteArrayInputStream(baos.toByteArray()));

        assertTrue(gotTrie.contains("a"));
        assertTrue(gotTrie.contains("abc"));
        assertTrue(gotTrie.contains(""));
        assertEquals(5, gotTrie.size());
        assertEquals(1, gotTrie.howManyStartWithPrefix("ab"));
        assertEquals(2, gotTrie.howManyStartWithPrefix("a"));
        assertEquals(1, gotTrie.howManyStartWithPrefix("qq!'~"));
    }
}