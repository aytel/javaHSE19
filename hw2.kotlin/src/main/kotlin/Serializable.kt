package com.aytel

import java.io.InputStream
import java.io.OutputStream

/** If class implements this interface it can be encoded to bytes or decoded from them. */
interface Serializable {

    /** Encodes trie into byte sequence. */
    fun serialize(ots: OutputStream)

    /** Decodes trie from byte sequence. */
    fun deserialize(ins: InputStream)
}