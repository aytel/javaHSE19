package com.aytel.outputs;

public class TestClass<T extends java.lang.Integer, K extends java.lang.Object, G extends java.lang.String & java.util.List> extends com.aytel.SimpleTestClass<java.lang.Integer> implements java.lang.Comparable<K>, com.aytel.FirstTestInterface<java.util.List<? super K>> {
    private <T extends java.lang.Object, K extends java.lang.Object>TestClass(int arg0){}
    <Q extends java.lang.Object>TestClass(T arg0, java.lang.Integer arg1){}
    java.util.List<T> list;
    java.util.List<java.lang.Integer> ilist;
    T t;
    int x;
    java.lang.Integer[] vals;
    java.lang.String[][] s;
    int a;
    protected T[] aaa;
    <Q extends java.lang.Object>int kek (Q[] arg0, java.util.List<? super java.util.List<? extends K>> arg1){ throw new UnsupportedOperationException(); }
    static java.lang.Integer sus (java.util.List<?>... arg0){ throw new UnsupportedOperationException(); }
    private void superOf (T... arg0){ throw new UnsupportedOperationException(); }
    public int compareTo (java.lang.Object arg0){ throw new UnsupportedOperationException(); }
    class meme<E extends K, F extends java.lang.Object> {
        meme(com.aytel.TestClass arg0){}
        int a;
        java.util.List<T> b;
    }
}
