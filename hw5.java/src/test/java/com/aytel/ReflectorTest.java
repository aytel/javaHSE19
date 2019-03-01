package com.aytel;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReflectorTest {

    class DDumb<T>{
        int k;
    }

    interface DumbI<T> {}
    interface DDumbI<T> {}

    class Dumb<T extends Integer, K> extends DDumb<Integer> implements DDumbI<K>, DumbI<List<T>> {
        List<T> list;
        List<Integer> ilist;
        T t;
        int x;
        <Q> int kek(Q q, List<? extends T> v) {

            return 0;
        }
        class meme {
            int a;
            List<T> b;
        }
        private <T, K>Dumb(int i){}
        Dumb(T x, Integer y){}
    }

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