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
        writer = new FileWriter("src/test/java/"
                + someClass.getPackageName().replace('.', '/')
                + "/outputs/"
                + someClass.getSimpleName()
                + ".java", false);
        writer.write("package " + someClass.getPackageName() + ".outputs" + ";\n\n");
        printClass(someClass);
        writer.close();
    }

    static void diffClassesToCustomOutput(Class<?> firstClass, Class<?> secondClass, PrintStream printStream) {
        printStream.println("Only in first: \n");
        printStream.println("Methods: \n");
        printMethodsOnlyInFirst(firstClass, secondClass);
        printStream.println();
        printStream.println("Fields: \n");
        printFieldsOnlyInFirst(firstClass, secondClass);
        printStream.println();

        printStream.println("Only in second: \n");
        printStream.println("Methods: \n");
        printMethodsOnlyInFirst(secondClass, firstClass);
        printStream.println();
        printStream.println("Fields: \n");
        printFieldsOnlyInFirst(secondClass, firstClass);
        printStream.println();
    }

    public static void diffClasses(@NotNull Class<?> firstClass, @NotNull Class<?> secondClass) {
        diffClassesToCustomOutput(firstClass, secondClass, System.out);
    }

    private static void printClass(Class<?> someClass) throws IOException {
        printTabs();
        writer.write(modifiersToString(someClass.getModifiers()));
        writer.write("class " + someClass.getSimpleName());
        writer.write(genericTypeToString(someClass.getTypeParameters()));
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
            writer.write(constructorToString(constructor, someClass));
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
            writer.write(methodToString(method));
            writer.write("{ throw new UnsupportedOperationException(); }\n");
        }
    }

    private static void printFields(Class<?> someClass) throws IOException {
        for (Field field: someClass.getDeclaredFields()) {
            if (field.isSynthetic()) {
                continue;
            }
            printTabs();
            writer.write(fieldToString(field) + ";\n");
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

    private static String genericTypeToString(TypeVariable[] parameters) {
        StringBuilder stringBuilder = new StringBuilder();
        if (parameters.length != 0) {
            stringBuilder.append("<");
            stringBuilder.append(Arrays
                    .stream(parameters)
                    .map(Reflector::boundedGenericTypeToString)
                    .collect(Collectors.joining(", ")));
            stringBuilder.append(">");
        }
        return stringBuilder.toString();
    }

    private static String modifiersToString(int modifiers)  {
        StringBuilder stringBuilder = new StringBuilder();
        if (Modifier.isPrivate(modifiers)) {
            stringBuilder.append("private ");
        }
        if (Modifier.isProtected(modifiers)) {
            stringBuilder.append("protected ");
        }
        if (Modifier.isPublic(modifiers)) {
            stringBuilder.append("public ");
        }
        if (Modifier.isStatic(modifiers)) {
            stringBuilder.append("static ");
        }
        return stringBuilder.toString();
    }

    private static void printTabs() throws IOException {
        for (int i = 0; i < tabs; i++) {
            writer.write(" ");
        }
    }

    private static String methodToString(Method method) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(modifiersToString(method.getModifiers()));
        stringBuilder.append(genericTypeToString(method.getTypeParameters()));
        stringBuilder.append(method.getGenericReturnType().getTypeName()).append(" ").append(method.getName()).append(" ");
        stringBuilder.append("(").append(Arrays
                .stream(method.getParameters())
                .map((Parameter parameter) -> {
                    if (parameter.isVarArgs()) {
                        String typeName = parameter.getParameterizedType().getTypeName();
                        typeName = typeName.substring(0, typeName.length() - 2) + "...";
                        return typeName + " " + parameter.getName();
                    } else {
                        return parameter.getParameterizedType().getTypeName() + " " + parameter.getName();
                    }
                })
                .collect(Collectors.joining(", "))).append(")");
        return stringBuilder.toString();
    }

    private static String constructorToString(Constructor constructor, Class<?> someClass) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(modifiersToString(constructor.getModifiers()));
        stringBuilder.append(genericTypeToString(constructor.getTypeParameters()));
        stringBuilder.append(someClass.getSimpleName());
        stringBuilder.append("(").append(Arrays
                .stream(constructor.getParameters())
                .map((Parameter parameter) -> {
                    if (parameter.isVarArgs()) {
                        String typeName = parameter.getParameterizedType().getTypeName();
                        typeName = typeName.substring(0, typeName.length() - 2) + "...";
                        return typeName + " " + parameter.getName();
                    } else {
                        return parameter.getParameterizedType().getTypeName() + " " + parameter.getName();
                    }
                })
                .collect(Collectors.joining(", "))).append("){}\n");
        return stringBuilder.toString();
    }

    private static String fieldToString(Field field) {
        return modifiersToString(field.getModifiers()) + field.getGenericType().getTypeName() + " " + field.getName();
    }

    private static boolean methodsEqual(Method firstMethod, Method secondMethod) {
        return methodToString(firstMethod).equals(methodToString(secondMethod));
    }

    private static void printMethodsOnlyInFirst(Class<?> firstClass, Class<?> secondClass) {
        for (Method firstMethod: firstClass.getDeclaredMethods()) {
            if (Arrays
                    .stream(secondClass.getDeclaredMethods())
                    .noneMatch((Method secondMethod) -> methodsEqual(firstMethod, secondMethod))) {
                System.out.println(methodToString(firstMethod));
            }
        }
    }

    private static boolean fieldsEqual(Field firstField, Field secondField) {
        return fieldToString(firstField).equals(fieldToString(secondField));
    }

    private static void printFieldsOnlyInFirst(Class<?> secondClass, Class<?> firstClass) {
        for (Field firstField: firstClass.getDeclaredFields()) {
            if (Arrays
                    .stream(secondClass.getDeclaredFields())
                    .noneMatch((Field secondField) -> fieldsEqual(firstField, secondField))) {
                System.out.println(fieldToString(firstField));
            }
        }
    }
}
