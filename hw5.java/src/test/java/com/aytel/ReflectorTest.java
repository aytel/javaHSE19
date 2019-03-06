package com.aytel;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.AbstractList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DDumb<T>{
    int k;
}

interface DumbI<T> {}
interface DDumbI<T> {}

class Dumb<T extends Integer, K, G extends String & List> extends DDumb<Integer> implements Comparable<K>, DumbI<List<? super K>> {
    List<T> list;
    List<Integer> ilist;
    T t;
    int x;
    Integer[] vals;
    String[][] s;
    <Q> int kek(Q q, List<? super List<? extends K>> v) {

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
    private <T, K>Dumb(int i){}
    <Q>Dumb(T x, Integer y){}

    private void superOf(T ... oh) {
        return;
    }
    static Integer sus(List<?>... oh) { return 1; }
}


class ReflectorTest {

    @Test
    void dumb() throws IOException {
        Class<?> someClass = Dumb.class;

        Reflector.printStructure(Reflector.class);

        Reflector.printStructure(someClass);

        TypeVariable[] types = someClass.getTypeParameters();
        for (TypeVariable type : types) {
            System.out.println(type.getTypeName());
            Type[] bounds = type.getBounds();
            for (Type bound: bounds) {
                System.out.println(bound.getTypeName());
            }
        }
        for (Field field: someClass.getDeclaredFields()) {
            if (field.isSynthetic())
                continue;
            System.out.println(field.getGenericType().getTypeName() + " " + field.getName());
            /*for (Type type : types) {
                System.out.println(type.getTypeName());
            }*/
        }
    }

}