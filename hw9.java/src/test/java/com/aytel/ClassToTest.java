package com.aytel;

import com.aytel.annotations.BeforeEach;
import com.aytel.annotations.Test;

public class ClassToTest {
    @Test
    static void test() {
        System.out.println("simple test");
    }

    @Test(expected = Throwable.class)
    static void testWithThrow() throws Throwable {
        throw new Throwable();
    }

    @BeforeEach
    static void beforeEach() {
        System.out.println("BeforeEach");
    }
}
