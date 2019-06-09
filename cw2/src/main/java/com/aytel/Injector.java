package com.aytel;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.*;

public class Injector {

    private static Map<String,Object> used = new HashMap<>();

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */
    public static Object initialize(String rootClassName, List<Class<?>> implementationClasses) throws Exception {
        if (used.containsKey(rootClassName)) {
            if (used.get(rootClassName) != null) {
                return used.get(rootClassName);
            } else {
                throw new InjectionCycleException();
            }
        }
        Class<?> clazz = Class.forName(rootClassName);
        if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
            clazz = findImplementation(clazz, implementationClasses);
        }
        used.put(rootClassName, null);

        Constructor constructor = clazz.getDeclaredConstructors()[0];
        List<Class<?>> dependencies = Arrays.asList(constructor.getParameterTypes());
        List<Object> parameters = new ArrayList<>();
        for (Class<?> dependency: dependencies) {
            parameters.add(initialize(dependency.getName(), implementationClasses));
        }
        used.put(rootClassName, constructor.newInstance(parameters.toArray()));
        return used.get(rootClassName);
    }

    private static Class<?> findImplementation(Class<?> clazz, List<Class<?>> implementationClasses) throws AmbiguousImplementationException, ImplementationNotFoundException {
        Class<?> returnClass = null;
        for (Class<?> candidate: implementationClasses) {
            if (clazz.isAssignableFrom(candidate)) {
                if (returnClass != null) {
                    throw new AmbiguousImplementationException();
                } else {
                    returnClass = candidate;
                }
            }
        }
        if (returnClass == null) {
            throw new ImplementationNotFoundException();
        }
        return returnClass;
    }
}
