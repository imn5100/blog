//package com.shaw.aop;
//
//import com.shaw.constants.CacheKey;
//import com.shaw.service.impl.RedisClient;
//import com.shaw.util.ThreadPoolManager;
//import org.apache.commons.lang.StringUtils;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * @author imn5100
// */
//@Aspect
//@Component
//public class CacheAop {
//    @Autowired
//    private RedisClient redisClient;
//
//    /**
//     * 对搜索业务进行aop处理，避免频繁搜索
//     */
//    @Around("execution(public * com.shaw.lucene.BlogIndex.searchBlog(..))")
//    public Object around(ProceedingJoinPoint pjp) throws Throwable {
//        Object value = null;
//        String targetName = pjp.getTarget().getClass().getName();
//        // 拦截的方法名称
//        String methodName = pjp.getSignature().getName();
//        Object[] arguments = pjp.getArgs();
//        String key = getCacheKey(targetName, methodName, arguments);
//        try {
//            // 判断是否有缓存
//            if (exists(key)) {
//                return getCache(key);
//            }
//            // 写入缓存
//            value = pjp.proceed();
//            if (value != null) {
//                final String tempKey = key;
//                final Object tempValue = value;
//                ThreadPoolManager.INSTANCE.execute(() -> setCache(tempKey, tempValue, CacheKey.SEARCH_EXPIRETIME));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            if (value == null) {
//                return pjp.proceed();
//            }
//        }
//        return value;
//    }
//
//    private String getCacheKey(String targetName, String methodName, Object[] arguments) {
//        //searchCache 下为搜索缓存
//        StringBuilder sbu = new StringBuilder("SearchCache:");
//        sbu.append(targetName).append("_").append(methodName);
//        if ((arguments != null) && (arguments.length != 0)) {
//            for (int i = 0; i < arguments.length; i++) {
//                sbu.append("_").append(arguments[i]);
//            }
//        }
//        return sbu.toString();
//    }
//
//    public boolean exists(final String key) {
//        return redisClient.exists(key);
//    }
//
//    private Object getCache(final String key) {
//        Object result;
//        result = redisClient.get(key);
//        return result;
//    }
//
//    private boolean setCache(final String key, Object value, Long expireTime) {
//        if (StringUtils.isBlank(key) || value == null) {
//            return false;
//        }
//        boolean result = false;
//        try {
//            redisClient.set(key, value);
//            redisClient.expire(key, expireTime);
//            result = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//}
