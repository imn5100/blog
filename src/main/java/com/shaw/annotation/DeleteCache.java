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

    /**
     * 是否使用多个key(生成多个key，删除多个设置的缓存) 仅限SpEl类型的key
     */
    boolean multiKey() default false;
}