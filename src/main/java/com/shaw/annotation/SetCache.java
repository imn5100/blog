package com.shaw.annotation;

import java.lang.annotation.*;

@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SetCache {
    String key() default "";

    long expire() default 60;

    boolean onlyUseKey() default false;
}