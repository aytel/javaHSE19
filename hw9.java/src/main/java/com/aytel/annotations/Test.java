package com.aytel.annotations;

import com.aytel.None;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/** Annotation of my JUnit. Annotate method with this and it will be checked. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
    String ignore() default "";
    Class<? extends Throwable> expected() default None.class;
}
