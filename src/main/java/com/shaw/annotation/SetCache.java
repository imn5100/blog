package com.shaw.annotation;

import com.shaw.aop.CacheKeyType;

import java.lang.annotation.*;

@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SetCache {
    /**
     * key
     */
    String key() default "";

    /**
     * 缓存失效时间
     */
    int expire() default 60;

    /**
     * 缓存key生成契约：
     * 1.keyType=CacheKeyType.DEFAULT : 自动生成 默认前缀+类名+方法名+参数.toString()   ClinicCache_className_methodName_arg0.toString()_arg1.toString()....
     * 2.keyType=CacheKeyType.PREFIX  : key作为前缀+参数.toString()                   key_arg0.toString()_arg1.toString()....
     * 3.keyType=CacheKeyType.SpEl    : 使用SpEl表达式生成key  参数使用方式：arg0.toString()  arg1.getXXX() ...；注意：1.key必须是一个合法的SpEL表达式 2.不支持通过参数名访问
     * 4.keyType=CacheKeyType.STR     : 直接使用key
     */
    CacheKeyType keyType() default CacheKeyType.DEFAULT;
}
