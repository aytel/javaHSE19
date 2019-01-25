import com.aytel.Trie
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

internal class TrieTest {

    private lateinit var trie: Trie

    @BeforeEach
    fun init() {
        trie = Trie()
    }

    @Test
    fun size() {
        assertEquals(0, trie.size)
        trie.add("")
        assertEquals(1, trie.size)
    }

    @Test
    fun add() {
        assertFalse(trie.add("aba"))
        assertTrue(trie.add("aba"))
        assertFalse(trie.add("a"))
    }

    @Test
    fun remove() {
        trie.add("")
        assertTrue(trie.remove(""))
        assertFalse(trie.remove("abc"))
        trie.add("abc")
        assertFalse(trie.remove("ab"))
        assertEquals(1, trie.size)
    }

    @Test
    fun contains() {
        trie.add("abc")
        assertTrue(trie.contains("abc"))
        assertFalse(trie.contains("ab"))
    }

    @Test
    fun howManyStartWithPrefix() {
        trie.add("")
        trie.add("ab")
        assertEquals(2, trie.howManyStartWithPrefix(""))
        assertEquals(1, trie.howManyStartWithPrefix("a"))
        trie.remove("ab")
        assertEquals(0, trie.howManyStartWithPrefix("a"))
    }

    @Test
    fun serialization() {
        trie.add("a")
        trie.add("b")
        trie.add("abc")
        trie.add("")

        val baos = ByteArrayOutputStream(1000)
        trie.serialize(baos)

        val gotTrie = Trie()
        gotTrie.deserialize(ByteArrayInputStream(baos.toByteArray()))

        assertTrue(gotTrie.contains("a"))
        assertTrue(gotTrie.contains("abc"))
        assertTrue(gotTrie.contains(""))
        assertEquals(4, gotTrie.size)
        assertEquals(1, gotTrie.howManyStartWithPrefix("ab"))
        assertEquals(2, gotTrie.howManyStartWithPrefix("a"))
    }
}