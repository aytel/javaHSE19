package com.aytel;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ReflectorTest {
    private static final String lineBreak = System.lineSeparator();

    private static final String noDiff = "Only in first:" + lineBreak +
            "Methods:" + lineBreak +
            lineBreak +
            "Fields:" + lineBreak +
            lineBreak +
            "Only in second:" + lineBreak +
            "Methods:" + lineBreak +
            lineBreak +
            "Fields:" + lineBreak +
            lineBreak;

    void testCompile(Class<?> clazz) throws ClassNotFoundException, IOException {
        Reflector.printStructure(clazz);
        var os = new ByteArrayOutputStream();
        Reflector.diffClassesToCustomOutput(clazz,
                Class.forName("com.aytel.outputs." + clazz.getSimpleName()),
                new PrintStream(os));
        assertEquals(noDiff, os.toString());
    }

    @Test
    void testCompileTestClass() {
        assertDoesNotThrow(() -> testCompile(TestClass.class));
    }

    @Test
    void testCompileReflector() {
        assertDoesNotThrow(() -> testCompile(Reflector.class));
    }

}