package com.aytel;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.sql.Ref;
import java.util.AbstractList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void dumbDiff() {
        Reflector.diffClasses(Integer.class, Integer.class);
    }

    @Test
    void dumbCompile() throws IOException, ClassNotFoundException {
        Reflector.printStructure(Dumb.class);
        Reflector.diffClasses(Dumb.class, Class.forName("com.aytel.outputs.Dumb"));
    }

}