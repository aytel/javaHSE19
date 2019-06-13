package com.aytel;

import com.aytel.annotations.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class Tester {
    public static void test(String name) {
        try {
            var clazz = Class.forName(name);

            var beforeClass = new LinkedList<Method>();
            var afterClass = new LinkedList<Method>();
            var beforeEach = new LinkedList<Method>();
            var afterEach = new LinkedList<Method>();
            var tests = new LinkedList<Method>();

            for (var method: clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Test.class)) {
                    tests.add(method);
                }
                if (method.isAnnotationPresent(BeforeClass.class)) {
                    beforeClass.add(method);
                }
                if (method.isAnnotationPresent(AfterClass.class)) {
                    afterClass.add(method);
                }
                if (method.isAnnotationPresent(BeforeEach.class)) {
                    beforeEach.add(method);
                }
                if (method.isAnnotationPresent(AfterEach.class)) {
                    afterEach.add(method);
                }
            }

            runAllMethods(beforeClass);

            for (var method: tests) {
                var test = method.getAnnotation(Test.class);
                if (!test.ignore().isEmpty()) {
                    System.out.printf("Method %s is not tested, reason is: %s\n", method.getName(), test.ignore());
                } else {
                    testMethod(method, beforeEach, afterEach);
                }
            }

            runAllMethods(afterClass);

        } catch (ClassNotFoundException e) {
            System.out.println("Class not found.");
        }
    }

    private static void testMethod(Method method, List<Method> beforeEach, List<Method> afterEach) {
        runAllMethods(beforeEach);

        long start = System.currentTimeMillis();

        Class<? extends Throwable> exception = None.class;

        try {
            method.invoke(null);
        } catch (IllegalAccessException e) {
            System.out.printf("Method %s is not accessible\n", method.getName());
        } catch (InvocationTargetException e) {
            var cause = e.getCause();
            exception = cause.getClass();
        }

        long finish = System.currentTimeMillis();

        if (!exception.isAssignableFrom(method.getAnnotation(Test.class).expected())) {
            System.out.printf("Test %s failed, worked %d millis", method.getName(), finish - start);
        } else {
            System.out.printf("Test %s invoked correctly, worked %d millis", method.getName(), finish - start);
        }

        runAllMethods(afterEach);
    }

    private static void runAllMethods(List<Method> afterClass) {
        for (var method: afterClass) {
            try {
                method.invoke(null);
            } catch (IllegalAccessException e) {
                System.out.printf("Method %s is not accessible\n", method.getName());
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        test(args[0]);
    }
}
