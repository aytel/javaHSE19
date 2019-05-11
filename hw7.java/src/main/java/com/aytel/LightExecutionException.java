package com.aytel;

/** This exception is thrown by threadpool. */
public class LightExecutionException extends Exception {
    public LightExecutionException(Exception e) {
        super(e);
    }
}
