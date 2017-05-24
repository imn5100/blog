package com.shaw.handler;

import com.shaw.annotation.IpPassport;
import com.shaw.constants.CacheKey;
import com.shaw.constants.ResponseCode;
import com.shaw.service.impl.RedisClient;
import com.shaw.util.HttpRequestUtil;
import com.shaw.util.HttpResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by shaw  on 2016/12/20 0020.
 * ip验证拦截器
 */
public class IpPassportInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private RedisClient redisClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            IpPassport ipPassport = ((HandlerMethod) handler).getMethodAnnotation(IpPassport.class);
            if (ipPassport == null || ipPassport.validate() == false) {
                return true;
            } else {
                String ip = HttpRequestUtil.getIpAddr(request);
                if (redisClient.sismember(CacheKey.WHITE_LIST_IP, ip)) {
                    return true;
                } else {
                    HttpResponseUtil.writeCode(response, ResponseCode.IP_WRONG);
                    return false;
                }
            }
        }
        return true;
    }
}
