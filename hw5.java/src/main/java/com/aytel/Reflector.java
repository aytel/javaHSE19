package com.aytel;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Reflector {
    private static FileWriter writer;
    private static int tabs = 0;
    private static final int tabsOffset = 4;

    public static void printStructure(@NotNull Class<?> someClass) throws IOException {
        writer = new FileWriter("outputs/" + someClass.getName() + ".java", false);
        printClass(someClass);
        writer.close();
    }

    private static void printClass(Class<?> someClass) throws IOException {
        printTabs();
        printModifiers(someClass.getModifiers());
        writer.write("class " + someClass.getSimpleName());
        printGenericType(someClass.getTypeParameters());
        printSuperclasses(someClass);
        writer.write(" {\n");
        tabs += tabsOffset;
        printConstructors(someClass);
        printFields(someClass);
        printMethods(someClass);
        printClasses(someClass);
        tabs -= tabsOffset;
        printTabs();
        writer.write("}\n");
    }

    private static void printSuperclasses(Class<?> someClass) throws IOException {
        Type superclass = someClass.getGenericSuperclass();
        if (superclass != Object.class) {
            writer.write(" extends " + superclass.getTypeName());
        }
        Type[] interfaces = someClass.getGenericInterfaces();
        if (interfaces.length != 0) {
            writer.write(" implements " + Arrays
                    .stream(interfaces)
                    .map(Type::getTypeName)
                    .collect(Collectors.joining(", ")));
        }
    }

    private static void printConstructors(Class<?> someClass) throws IOException {
        for (Constructor constructor: someClass.getDeclaredConstructors()) {
            if (constructor.isSynthetic()) {
                continue;
            }
            printTabs();
            printModifiers(constructor.getModifiers());
            printGenericType(constructor.getTypeParameters());
            writer.write(constructor.getName());
            writer.write("(" + Arrays.
                    stream(constructor.getGenericParameterTypes()).
                    map(Type::getTypeName).
                    collect(Collectors.joining(", ")) + "){}\n");
        }
    }

    private static void printClasses(Class<?> someClass) throws IOException {
        for (Class inner: someClass.getDeclaredClasses()) {
            printClass(inner);
        }
    }

    private static void printMethods(Class<?> someClass) throws IOException {
        for (Method method: someClass.getDeclaredMethods()) {
            if (method.isSynthetic()) {
                continue;
            }
            printTabs();
            printModifiers(method.getModifiers());
            printGenericType(method.getTypeParameters());
            writer.write(method.getGenericReturnType() + " " + method.getName());
            writer.write("(" + Arrays
                    .stream(method.getGenericParameterTypes())
                    .map(Type::getTypeName)
                    .collect(Collectors.joining(", ")) + ")");
            if (method.getReturnType().isPrimitive()) {
                writer.write("{ return " + getInstanceOfPrimitive(method.getReturnType()).toString() + "; }\n");
            } else {
                writer.write("{ return null; }\n");
            }
        }
    }

    private static Object getInstanceOfPrimitive(Class<?> returnType) {
        if (returnType == Boolean.TYPE) {
            return Boolean.valueOf("false");
        } else {
            return Integer.valueOf("0");
        }
    }

    private static void printFields(Class<?> someClass) throws IOException {
        for (Field field: someClass.getDeclaredFields()) {
            if (field.isSynthetic()) {
                continue;
            }
            printTabs();
            printModifiers(field.getModifiers());
            writer.write(field.getGenericType().getTypeName() + " " + field.getName() + ";\n");
        }
    }

    private static String boundedGenericTypeToString(TypeVariable type) {
        StringBuilder builder = new StringBuilder();
        builder.append(type.getName()).append(" extends ");
        builder.append(Arrays
                .stream(type.getBounds())
                .map(Type::getTypeName)
                .collect(Collectors.joining(" & ")));
        return builder.toString();
    }

    private static void printGenericType(TypeVariable[] parameters) throws IOException {
        if (parameters.length != 0) {
            writer.write("<");
            writer.write(Arrays
                    .stream(parameters)
                    .map(Reflector::boundedGenericTypeToString)
                    .collect(Collectors.joining(", ")));
            writer.write(">");
        }
    }

    private static void printModifiers(int modifiers) throws IOException {
        if (Modifier.isStatic(modifiers)) {
            writer.write("static ");
        }
        if (Modifier.isPrivate(modifiers)) {
            writer.write("private ");
        }
        if (Modifier.isProtected(modifiers)) {
            writer.write("protected ");
        }
        if (Modifier.isPublic(modifiers)) {
            writer.write("public ");
        }
    }

    private static void printTabs() throws IOException {
        for (int i = 0; i < tabs; i++) {
            writer.write(" ");
        }
    }


}
