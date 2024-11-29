package com.github.romanqed.devspark;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Return {
    Class<?> value();

    Class<?> sub() default void.class;
}
