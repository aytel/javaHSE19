package com.aytel;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;

/** MD5Hasher which recursively hashes directory. */
public interface MD5Hasher {

    /** Returns MessageDigest which represents MD5 hash of file. Call ::digest to get byte array of it. */
    MessageDigest hash(@NotNull File file) throws IOException;
}
