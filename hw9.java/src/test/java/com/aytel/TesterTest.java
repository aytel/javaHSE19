package com.aytel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TesterTest {

    @Test
    void test() {
        assertDoesNotThrow(() -> Tester.test("com.aytel.ClassToTest"));
    }
}