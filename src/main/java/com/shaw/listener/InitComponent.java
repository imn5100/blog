package com.shaw.listener;

import com.shaw.service.SystemService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 容器启动完成监听器，启动完成容器后，缓存系统关键数据
 */
@Component
public class InitComponent implements ServletContextListener, ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        InitComponent.applicationContext = applicationContext;
    }

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext application = servletContextEvent.getServletContext();
        SystemService systemService = (SystemService) applicationContext.getBean("systemService");
        systemService.initBlogData(application);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
