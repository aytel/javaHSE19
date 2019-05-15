package com.aytel;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        File file = new File(args[0]);
        System.out.println("SingleThread:");
        printHashAndTime(new SingleThreadMD5Hasher(), file);
        System.out.println("ForkJoin:");
        printHashAndTime(new ForkJoinMD5Hasher(), file);
    }

    private static void printHashAndTime(MD5Hasher hasher, File file) {
        long startTime = System.currentTimeMillis();

        try {
            System.out.println(MessageDigestToStringConverter.convert(hasher.hash(file)));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        long stopTime = System.currentTimeMillis();

        System.out.println(stopTime - startTime);
    }
}
