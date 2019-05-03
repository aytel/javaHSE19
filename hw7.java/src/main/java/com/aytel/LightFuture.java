package com.aytel;

import java.util.function.Function;

/** Simple interface which represents task that you must wait to be calculated in other thread. */
public interface LightFuture<T> {
    /** Returns true if task is ready and false otherwise. */
    boolean isReady();

    /** Waits for calculating and returns the result. */
    T get() throws LightExecutionException, InterruptedException;

    /** Returns new LightFuture which will return function applied to result of this LightFuture. */
    <K> LightFuture<K> thenApply(Function<? super T, K> function);
}
