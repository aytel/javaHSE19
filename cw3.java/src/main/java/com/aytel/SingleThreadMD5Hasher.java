package com.aytel;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** Hasher with only one thread to work in. */
public class SingleThreadMD5Hasher implements MD5Hasher {
    @Override
    public MessageDigest hash(@NotNull File file) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (file.isDirectory()) {
                md.update(file.getName().getBytes());
                for (var child: file.listFiles()) {
                    md.update(hash(child).digest());
                }
            } else {
                MessageDigestWithFileUpdater.update(md, file);
            }
            return md;
        } catch (NoSuchAlgorithmException ignored) {
            throw new RuntimeException();
        }
    }
}
