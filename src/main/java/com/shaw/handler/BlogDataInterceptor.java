package com.shaw.handler;

import com.shaw.constants.CacheKey;
import com.shaw.service.SystemService;
import com.shaw.service.impl.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by shaw   on 2016/12/20 0020.
 */
public class BlogDataInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private SystemService systemService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ServletContext application = RequestContextUtils.findWebApplicationContext(request).getServletContext();
        Object applicationTimestamp = application.getAttribute(CacheKey.SYSTEM_REFRESH_TIME);
        Object redisTimestamp = redisClient.get(CacheKey.SYSTEM_REFRESH_TIME);
        //刷新条件：当前服务器上下文的刷新时间戳小于缓存中的刷新时间戳时（即为当前服务器的刷新时间晚于上传手动刷新博客缓存的时间），进行刷新
        if ((applicationTimestamp == null && redisTimestamp != null) ||
                (applicationTimestamp != null && redisTimestamp != null && (Long) applicationTimestamp < (Long) redisTimestamp)) {
            //刷新当前服务器上下文缓存
            systemService.initBlogData(application);
            //刷新完成后同步时间戳
            application.setAttribute(CacheKey.SYSTEM_REFRESH_TIME, redisTimestamp);
        }
        return super.preHandle(request, response, handler);
    }
}
