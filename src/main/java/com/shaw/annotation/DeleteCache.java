package com.shaw.annotation;

import com.shaw.aop.CacheKeyType;

import java.lang.annotation.*;

@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DeleteCache {
    String key() default "";

    CacheKeyType keyType() default CacheKeyType.DEFAULT;
}