package com.aytel;

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;

/** Converts byte array from MessageDigest to hex string. */
public class MessageDigestToStringConverter {
    public static String convert(@NotNull MessageDigest md) {
        StringBuilder sb = new StringBuilder();
        byte[] bytes = md.digest();
        for (byte bt: bytes) {
            sb.append(Integer.toHexString(0xFF & bt));
        }
        return sb.toString();
    }
}
