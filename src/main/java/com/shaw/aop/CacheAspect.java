package com.shaw.aop;

import com.shaw.annotation.DeleteCache;
import com.shaw.annotation.SetCache;
import com.shaw.service.impl.RedisClient;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @author imn5100
 */
@Aspect
@Component
public class CacheAspect {
    /**
     * 所有包含了@SetCache注解的地方作为AOP织入点
     */
    @Pointcut("@annotation(com.shaw.annotation.SetCache)")
    public void setCachePointcut() {
    }

    @Pointcut("@annotation(com.shaw.annotation.DeleteCache)")
    public void deleteCachePointcut() {
    }

    @Autowired
    private RedisClient redisClient;


    /***
     * 切点Around方法实现
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("setCachePointcut()")
    public Object doAround(final ProceedingJoinPoint pjp) throws Throwable {
        final MethodSignature ms = (MethodSignature) pjp.getSignature();
        final Method method = ms.getMethod();
        final SetCache cacheAnnotation = method.getAnnotation(SetCache.class);
        if (cacheAnnotation != null) {
            final String cacheKey = cacheAnnotation.onlyUseKey() ? cacheAnnotation.key() : generateCacheKey(cacheAnnotation.key(), pjp);
            final long expire = cacheAnnotation.expire();
            final Object cacheValue = redisClient.get(cacheKey);
            if (cacheValue != null) {
                return cacheValue;
            }
            final Object proceedValue = pjp.proceed();
            if (proceedValue != null) {
                //非序列化结果不保存
                if (!(proceedValue instanceof Serializable)) {
                    return proceedValue;
                }
                //cache不保存空的集合
                if (proceedValue instanceof Collection && ((Collection<?>) proceedValue).isEmpty()) {
                    return proceedValue;
                }
                Serializable returnValue = (Serializable) proceedValue;
                redisClient.set(cacheKey, returnValue);
                redisClient.expire(cacheKey, expire);
            }
            return proceedValue;
        } else {
            return pjp.proceed();
        }
    }

    @AfterReturning("deleteCachePointcut()")
    public void doAfter(final JoinPoint pjp) throws Throwable {
        final MethodSignature ms = (MethodSignature) pjp.getSignature();
        final Method method = ms.getMethod();
        final DeleteCache deleteCacheAnnotation = method.getAnnotation(DeleteCache.class);
        if (deleteCacheAnnotation != null) {
            redisClient.del(generateCacheKey(deleteCacheAnnotation.key(), pjp));
        }
    }


    private static String generateCacheKey(final String configKey, final JoinPoint pjp) {
        final Object[] methodArgs = pjp.getArgs();
        if (configKey != null && configKey.trim().length() > 0) {
            return generateCacheKey(configKey, methodArgs);
        }
        StringBuilder sb = new StringBuilder();
        String className = pjp.getTarget().getClass().getSimpleName();
        String methodName = pjp.getSignature().getName();
        sb.append("blog_cache");
        sb.append(className);
        sb.append("_");
        sb.append(methodName);
        for (Object arg : methodArgs) {
            if (arg != null) {
                sb.append("_").append(arg.toString());
            }
        }
        return sb.toString();
    }

    private static String generateCacheKey(String configKey, Object[] methodArgs) {
        if (methodArgs == null || methodArgs.length == 0) {
            return configKey;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(configKey);
        for (Object arg : methodArgs) {
            if (arg != null) {
                sb.append("_").append(arg.toString());
            }
        }
        return sb.toString();
    }


}
