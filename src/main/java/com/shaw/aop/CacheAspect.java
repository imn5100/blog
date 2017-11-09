package com.shaw.aop;

import com.shaw.annotation.DeleteCache;
import com.shaw.annotation.SetCache;
import com.shaw.service.impl.RedisClient;
import com.shaw.util.StringUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;

@Aspect
@Component
public class CacheAspect {

    @Autowired
    private RedisClient redisClient;
    @Autowired
    ParameterNameDiscoverer nameDiscoverer;


    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Pointcut("@annotation(com.shaw.annotation.SetCache)")
    public void setCachePointcut() {
    }

    @Pointcut("@annotation(com.shaw.annotation.DeleteCache)")
    public void deleteCachePointcut() {
    }


    /***
     * 切点Around方法实现
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("setCachePointcut()")
    public Object doAround(final ProceedingJoinPoint pjp) throws Throwable {
        final SetCache cacheAnnotation = ((MethodSignature) pjp.getSignature()).getMethod().getAnnotation(SetCache.class);
        if (cacheAnnotation != null) {
            final String cacheKey = generateSetCacheKey(pjp);
            if (StringUtil.isEmpty(cacheKey)) {
                return pjp.proceed();
            }
            final int expire = cacheAnnotation.expire();
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
                redisClient.set(cacheKey, returnValue, expire);
            }
            return proceedValue;
        } else {
            return pjp.proceed();
        }
    }

    @AfterReturning("deleteCachePointcut()")
    public void doAround(final JoinPoint pjp) throws Throwable {
        String[] cacheKey = generateDeleteCacheKey(pjp);
        if (cacheKey != null && cacheKey.length > 0) {
            for (String key : cacheKey) {
                if (StringUtil.isNotEmpty(key)) {
                    redisClient.del(key);
                }
            }
        }
    }

    private String generateSetCacheKey(final JoinPoint pjp) {
        final Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        final SetCache cacheAnnotation = method.getAnnotation(SetCache.class);
        if (cacheAnnotation != null) {
            final String cacheKey = generateCacheKey(cacheAnnotation.key(), cacheAnnotation.keyType(), pjp);
            if (StringUtil.isEmpty(cacheKey)) {
                logger.error("Error:setCache generate cache Key Error:Key:" + cacheAnnotation.key() + " KeyType:" + cacheAnnotation.keyType() + " Method:" + method.getName());
                return null;
            } else {
                return cacheKey;
            }
        }
        return null;
    }

    private String[] generateDeleteCacheKey(final JoinPoint pjp) {
        final Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        final DeleteCache cacheAnnotation = method.getAnnotation(DeleteCache.class);
        if (cacheAnnotation != null) {
            if (cacheAnnotation.multiKey()) {
                if (cacheAnnotation.keyType() == CacheKeyType.SpEl) {
                    String[] multiKeys = generateSpELKey(cacheAnnotation.key(), pjp, String[].class);
                    if (multiKeys != null && multiKeys.length > 0) {
                        return multiKeys;
                    } else {
                        logger.error("Error:DeleteCache generate cache Key Error:Key:" + cacheAnnotation.key() + " KeyType:" + cacheAnnotation.keyType() + " Method:" + method.getName() + " MultiKey:True");
                    }
                } else {
                    throw new RuntimeException("MultiKey must use for  CacheKeyType.SpEL");
                }
            } else {
                final String cacheKey = generateCacheKey(cacheAnnotation.key(), cacheAnnotation.keyType(), pjp);
                if (StringUtil.isEmpty(cacheKey)) {
                    logger.error("Error:DeleteCache generate cache Key Error:Key:" + cacheAnnotation.key() + " KeyType:" + cacheAnnotation.keyType() + " Method:" + method.getName());
                } else {
                    return new String[]{cacheKey};
                }
            }
        }
        return null;
    }

    private String generateCacheKey(String key, CacheKeyType cacheKeyType, final JoinPoint pjp) {
        switch (cacheKeyType) {
            case STR:
                return key;
            case SpEl:
                return generateSpELKey(key, pjp, String.class);
            case PREFIX:
                return generatePrefixCacheKey(key, pjp.getArgs());
            default:
                return generateDefaultCacheKey(pjp);
        }
    }

    private String generateDefaultCacheKey(final JoinPoint pjp) {
        final Object[] methodArgs = pjp.getArgs();
        StringBuilder sb = new StringBuilder();
        String className = pjp.getTarget().getClass().getSimpleName();
        String methodName = pjp.getSignature().getName();
        sb.append("BlogCache");
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


    private <T> T generateSpELKey(String configKey, final JoinPoint pjp, Class<T> tClass) {
        Object[] methodArgs = pjp.getArgs();
        if (methodArgs == null || methodArgs.length == 0) {
            throw new RuntimeException("Not support null args for SpEL");
        }
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext ctx = new StandardEvaluationContext();
        String[] argNames = nameDiscoverer.getParameterNames(((MethodSignature) pjp.getSignature()).getMethod());
        for (int i = 0; i < methodArgs.length; i++) {
            ctx.setVariable("arg" + i, methodArgs[i]);
            ctx.setVariable(argNames[i], methodArgs[i]);
        }
        ctx.setVariable("target", pjp.getTarget());
        ctx.setVariable("methodSignature", pjp.getSignature());
        Expression expr = parser.parseExpression(configKey);
        return expr.getValue(ctx, tClass);
    }

    private String generatePrefixCacheKey(String configKey, Object[] methodArgs) {
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
