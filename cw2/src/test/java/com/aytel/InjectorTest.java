package com.aytel;

import org.junit.jupiter.api.Test;
import com.aytel.testClasses.ClassWithOneClassDependency;
import com.aytel.testClasses.ClassWithOneInterfaceDependency;
import com.aytel.testClasses.ClassWithoutDependencies;
import com.aytel.testClasses.InterfaceImpl;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class InjectorTest {

    @Test
    public void injectorShouldInitializeClassWithoutDependencies()
            throws Exception {
        Object object = Injector.initialize("com.aytel.testClasses.ClassWithoutDependencies", Collections.emptyList());
        assertTrue(object instanceof ClassWithoutDependencies);
    }

    @Test
    public void injectorShouldInitializeClassWithOneClassDependency()
            throws Exception {
        Object object = Injector.initialize(
                "com.aytel.testClasses.ClassWithOneClassDependency",
                Collections.singletonList(com.aytel.testClasses.ClassWithoutDependencies.class)
        );
        assertTrue(object instanceof ClassWithOneClassDependency);
        ClassWithOneClassDependency instance = (ClassWithOneClassDependency) object;
        assertTrue(instance.dependency != null);
    }

    @Test
    public void injectorShouldInitializeClassWithOneInterfaceDependency()
            throws Exception {
        Object object = Injector.initialize(
                "com.aytel.testClasses.ClassWithOneInterfaceDependency",
                Collections.singletonList(com.aytel.testClasses.InterfaceImpl.class)
        );
        assertTrue(object instanceof ClassWithOneInterfaceDependency);
        ClassWithOneInterfaceDependency instance = (ClassWithOneInterfaceDependency) object;
        assertTrue(instance.dependency instanceof InterfaceImpl);
    }
}
