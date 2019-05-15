package com.aytel;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;

import static org.junit.jupiter.api.Assertions.*;

class HasherTest {
    private MD5Hasher singleThreadHasher = new SingleThreadMD5Hasher();
    private MD5Hasher forkJoinHasher = new ForkJoinMD5Hasher();

    void testEmptyFolder(MD5Hasher hasher) {
        try {
            MessageDigest md = hasher.hash(new File("data/test1"));
            assertEquals("5a105e8b9d40e1329780d62ea2265d8a", MessageDigestToStringConverter.convert(md));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testEmptyFolderSingleThread() {
        testEmptyFolder(singleThreadHasher);
    }

    @Test
    void testEmptyFolderForkJoin() {
        testEmptyFolder(forkJoinHasher);
    }

    void testFolderWithOneFile(MD5Hasher hasher) {
        try {
            MessageDigest md = hasher.hash(new File("data/test2"));
            assertEquals("f43a1738add8fc8710386be74c49c9f7", MessageDigestToStringConverter.convert(md));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testFolderWithOneFileSingleThread() {
        testFolderWithOneFile(singleThreadHasher);
    }

    @Test
    void testFolderWithOneFileForkJoin() {
        testFolderWithOneFile(forkJoinHasher);
    }

    void testFolderWithOneFolder(MD5Hasher hasher) {
        try {
            MessageDigest md = hasher.hash(new File("data/test3"));
            assertEquals("d42041be376fb0bad063557c5e388e7f", MessageDigestToStringConverter.convert(md));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testFolderWithOneFolderSingleThread() {
        testFolderWithOneFolder(singleThreadHasher);
    }

    @Test
    void testFolderWithOneFolderForkJoin() {
        testFolderWithOneFolder(forkJoinHasher);
    }
}