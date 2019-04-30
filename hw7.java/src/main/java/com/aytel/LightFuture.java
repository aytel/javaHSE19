package com.aytel;

import java.util.function.Function;

public interface LightFuture<T> {
    boolean isReady();
    T get() throws LightExecutionException, InterruptedException;
    <K> LightFuture<K> thenApply(Function<? super T, K> function);
}
