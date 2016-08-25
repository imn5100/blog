package com.shaw.service.impl;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.shaw.constants.CacheKey;
import com.shaw.entity.Blog;
import com.shaw.entity.BlogType;
import com.shaw.entity.Blogger;
import com.shaw.entity.Link;
import com.shaw.service.BlogService;
import com.shaw.service.BlogTypeService;
import com.shaw.service.BloggerService;
import com.shaw.service.LinkService;

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
		BloggerService bloggerService = (BloggerService) applicationContext.getBean("bloggerService");
		Blogger blogger = bloggerService.find(); // 查询博主信息
		blogger.setPassword(null);
		application.setAttribute(CacheKey.BLOGGER, blogger);

		BlogTypeService blogTypeService = (BlogTypeService) applicationContext.getBean("blogTypeService");
		List<BlogType> blogTypeCountList = blogTypeService.countList(); // 查询博客类别以及博客的数量
		application.setAttribute(CacheKey.BLOG_TYPE_LIST, blogTypeCountList);

		BlogService blogService = (BlogService) applicationContext.getBean("blogService");
		List<Blog> blogCountList = blogService.countList(); // 根据日期分组查询博客
		application.setAttribute(CacheKey.BLOG_COUNT_LIST, blogCountList);

		LinkService linkService = (LinkService) applicationContext.getBean("linkService");
		List<Link> linkList = linkService.list(null); // 查询所有的友情链接信息
		application.setAttribute(CacheKey.LINK_LIST, linkList);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}
}
