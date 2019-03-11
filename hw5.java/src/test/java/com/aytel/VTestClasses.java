package com.aytel;

import org.junit.jupiter.api.Test;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.*;

class TestPrimitives {
    int a1;
    char a2;
    boolean a3;
    double a4;
    long a5;
    float a6;
    short a7;
    byte a8;
}

class TestFinalFields {
    final int a1 = 0;
    final char a2 = 0;
    final boolean a3 = false;
    final double a4 = 0;
    final long a5 = 0;
    final float a6 = 0;
    final short a7 = 0;
    final byte a8 = 0;
    final String s = null;
    final Object[] o = null;
}

class TestArgsInVoidMethods {
    void f1() {}
    void f2(int x) {}
    void f3(String s, boolean y) {}
    void f4(TestArgsInVoidMethods a) {}
    void f4(TestArgsInVoidMethods[] arr) {}
    void f5(boolean[] arr) {}
}

class TestArgsInConstructor {
    TestArgsInConstructor() {}
    TestArgsInConstructor(int x) {}
    TestArgsInConstructor(String s, boolean y) {}
    TestArgsInConstructor(String[] arr) {}
    TestArgsInConstructor(boolean[] arr) {}
}

class TestReturnTypes {
    TestReturnTypes() {}
    void f1() {}
    int f2() { return 0; }
    boolean f3() { return false; }
    int[] f4() { return null; }
    TestReturnTypes[] f5() { return null; }
    TestReturnTypes f6() { return null; }
}

class TestModifiers {
    public TestModifiers() {}
    protected TestModifiers(int x) {}
    private TestModifiers(int x1, int x2) {}

    public int a1;
    private int a2;
    int a3;
    protected int a4;
    public static int a5;
    private static int a6;
    static int a7;
    protected static int a8;

    public void m1() {}
    private void m2() {}
    protected void m3() {}
    public static void m4() {}
    private static void m5() {}
    protected static void m6() {}
}

class TestNestedClasses {
    private static class Nested1 {
        private Nested1() {}
        private int x;
        protected void m() {}
    }

    private static class Nested2 implements Runnable {
        public void run() {}
    }

    private static class Nested3 {
        private Nested4 obj;
        private static class Nested4 {}
    }
}

class TestExceptions {
    TestExceptions() throws IOException {}
    void m() throws IOException, ClassNotFoundException {}
}

class TestExtends extends ClassLoader {}
class TestExtendsObject extends Object {}
class TestImplements implements Runnable, Comparable {
    public int compareTo(Object o) { return 0; }
    public void run() {}
}
class TestExtendsAndImplements extends ClassLoader implements Runnable {
    public void run() {}
}

class TestGenericFields<T, S> {
    T t;
    final T finalT = null;
    T[] arr;
    Class<T> tClass;
}

class TestGenericMethodsArgs<T> {
    TestGenericMethodsArgs(T t) {}
    void f1(T t) {}
    void f2(T t, T t2) {}
    void f3(T[] t) {}
    void f4(Class<T> t) {}
    void f5(BiPredicate<T, T> pred) {}
    <U> void f6(U u, T t, Class<U> uClass) {}
    <U extends Comparable<U> & Runnable> void f7(Collection<U> pred) {}
}

class TestGenericReturnTypes<T, S> {
    T f1() { return null; }
    T[] f2() { return null; }
    Class<T> f3() { return null; }
    <U> U f4() { return null; }
    <U extends T> U f5() { return null; }
    <U extends T, S> U f6() { return null; }
    <U extends ClassLoader & Runnable> U f7() { return null; }
}

class TestWildcardInFields<T> {
    Class<?> f1;
    Class<? extends Runnable> f2;
    Class<? extends T> f3;
    Class<? super Runnable> f4;
    Class<? super T> f5;
}

class TestWildcardInReturnTypes<T> {
    Class<?> f1() { return null; }
    Class<? extends Runnable> f2() { return null; }
    Class<? extends T> f3() { return null; }
    Class<? super Runnable> f4() { return null; }
    Class<? super T> f5() { return null; }
    <U extends Comparable<? super U>> U f6() { return null; }
}

class TestWildcardInArgs<T> {
    void f1(Class<?> c) {}
    void f2(Class<? extends Runnable> c) {}
    void f3(Class<? extends T> c) {}
    void f4(Class<? super Runnable> c) {}
    void f5(Class<? super T> c) {}
    <U extends Comparable<? super U>> void f6(Collection<? super U> c) {}
}

class VTestClasses {
    @Test
    void testPrimitiveFields() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestPrimitives.class);
    }

    @Test
    void testFinalFields() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestFinalFields.class);
    }

    @Test
    void testArgsInVoidMethods() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestArgsInVoidMethods.class);
    }

    @Test
    void testArgsInConstructor() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestArgsInConstructor.class);
    }

    @Test
    void testReturnTypes() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestReturnTypes.class);
    }

    @Test
    void testModifiers() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestModifiers.class);
    }

    @Test
    void testExceptions() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestExceptions.class);
    }

    @Test
    void testExtends() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestExtends.class);
    }

    @Test
    void testExtendsObject() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestExtendsObject.class);
    }

    @Test
    void testImplements() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestImplements.class);
    }

    @Test
    void testExtendsAndImplements() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestExtendsAndImplements.class);
    }

    @Test
    void testInnerClasses() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(VTestInnerClasses.class);
    }

    @Test
    void testNestedClasses() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestNestedClasses.class);
    }

    @Test
    void testGenericFields() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestGenericFields.class);
    }

    @Test
    void testGenericMethodsArgs() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestGenericMethodsArgs.class);
    }

    @Test
    void testGenericReturnTypes() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestGenericReturnTypes.class);
    }

    @Test
    void testWildcardInFields() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestWildcardInFields.class);
    }

    @Test
    void testWildcardInReturnTypes() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestWildcardInReturnTypes.class);
    }

    @Test
    void testWildcardInArgs() throws IOException, ClassNotFoundException {
        testCompiledSourceEqual(TestWildcardInArgs.class);
    }

    private void testCompiledSourceEqual(Class<?> targetClass) throws IOException,
            ClassNotFoundException {
        Reflector.printStructure(targetClass);

        var sourceFile = new File("outputs/com.aytel." + targetClass.getSimpleName() + ".java");
        var currentDir = new File("outputs/");

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, sourceFile.getPath());

        var classLoader = URLClassLoader.newInstance(new URL[] { currentDir.toURI().toURL() });
        var loadedClass = Class.forName(targetClass.getName(), true, classLoader);

        try (var outStream = new ByteArrayOutputStream();
             var out = new PrintStream(outStream)) {
            //Reflector.diffClasses(targetClass, loadedClass);
            //assertEquals(0, outStream.size());
        }
    }
}