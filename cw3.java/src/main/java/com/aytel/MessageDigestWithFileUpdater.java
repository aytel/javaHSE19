package com.aytel;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;

/** Adds content of the non-directory file to MessageDigest. */
public class MessageDigestWithFileUpdater {
    public static void update(@NotNull MessageDigest md, @NotNull File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        byte[] dataBytes = new byte[4096];

        int nread = 0;
        while ((nread = fis.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nread);
        }
        fis.close();
    }
}
