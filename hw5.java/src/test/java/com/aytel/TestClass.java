package com.aytel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TestClass<T extends Integer, K, G extends String & List> extends SimpleTestClass<Integer> implements Comparable<K>, FirstTestInterface<List<? super K>> {
    List<T> list;
    List<Integer> ilist;
    T t;
    int x;
    Integer[] vals;
    String[][] s;
    <Q> int kek(Q[] q, List<? super List<? extends K>> v) {

        return 0;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return 0;
    }

    int a;
    protected T[] aaa;

    class meme<E extends K, F> {
        int a;
        List<T> b;
    }
    private <T, K> TestClass(int i){}
    <Q> TestClass(T x, Integer y){}

    private void superOf(T ... oh) {
        return;
    }
    static Integer sus(List<?>... oh) { return 1; }
}
