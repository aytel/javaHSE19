package com.aytel;

import com.aytel.testClasses.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;


class InjectorTest {

    @Test
    void injectorShouldInitializeClassWithoutDependencies()
            throws Exception {
        Object object = Injector.initialize("com.aytel.testClasses.ClassWithoutDependencies", Collections.emptyList());
        assertTrue(object instanceof ClassWithoutDependencies);
    }

    @Test
    void injectorShouldInitializeClassWithOneClassDependency()
            throws Exception {
        Object object = Injector.initialize(
                "com.aytel.testClasses.ClassWithOneClassDependency",
                Collections.singletonList(com.aytel.testClasses.ClassWithoutDependencies.class)
        );
        assertTrue(object instanceof ClassWithOneClassDependency);
        ClassWithOneClassDependency instance = (ClassWithOneClassDependency) object;
        assertNotNull(instance.dependency);
    }

    @Test
    void injectorShouldInitializeClassWithOneInterfaceDependency()
            throws Exception {
        Object object = Injector.initialize(
                "com.aytel.testClasses.ClassWithOneInterfaceDependency",
                Collections.singletonList(com.aytel.testClasses.InterfaceImpl.class)
        );
        assertTrue(object instanceof ClassWithOneInterfaceDependency);
        ClassWithOneInterfaceDependency instance = (ClassWithOneInterfaceDependency) object;
        assertTrue(instance.dependency instanceof InterfaceImpl);
    }

    @Test
    void injectorShouldThrowImplementationNotFoundException() {
        assertThrows(ImplementationNotFoundException.class,
                () -> Injector.initialize("com.aytel.testClasses.ClassWithOneInterfaceDependency",
                        Collections.emptyList()));
    }

    @Test
    void injectorShouldThrowAmbiguousImplementationException() {
        assertThrows(AmbiguousImplementationException.class,
                () -> Injector.initialize("com.aytel.testClasses.ClassWithOneInterfaceDependency",
                        Arrays.asList(Interface.class, InterfaceImpl.class)));
    }

    @Test
    void injectorShouldThrowInjectionCycleException() {
        assertThrows(InjectionCycleException.class,
                () -> Injector.initialize("com.aytel.testClasses.ClassAWithBDependency",
                        Collections.singletonList(ClassBWithADependency.class)));
    }

    @Test
    void injectorShouldCreateOnlyOneInstance() throws Exception {
        Injector.initialize("com.aytel.testClasses.ClassWithTwoDependecies",
                Arrays.asList(FirstClassExtendsCounter.class,
                        SecondClassExtendsCounter.class,
                        Counter.class));
        assertEquals(1, Counter.counter);
    }
}
