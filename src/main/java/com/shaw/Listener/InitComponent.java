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
 * 初始化组件 把博主信息 根据博客类别分类信息 根据日期归档分类信息 存放到application中，用以提供页面请求性能
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
