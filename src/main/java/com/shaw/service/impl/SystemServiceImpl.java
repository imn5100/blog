package com.shaw.service.impl;

import com.shaw.bo.Blog;
import com.shaw.bo.BlogType;
import com.shaw.bo.Blogger;
import com.shaw.bo.Link;
import com.shaw.constants.CacheKey;
import com.shaw.mapper.BlogMapper;
import com.shaw.mapper.BlogTypeMapper;
import com.shaw.mapper.BloggerMapper;
import com.shaw.mapper.LinkMapper;
import com.shaw.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.util.List;

/**
 * Created by Administrator on 2016/8/29 0029.
 */
@Service("systemService")
public class SystemServiceImpl implements SystemService {
    @Autowired
    private BloggerMapper bloggerMapper;

    @Autowired
    private BlogTypeMapper blogTypeMapper;

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private LinkMapper linkMapper;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public void initBlogData(ServletContext application) {
        try {
            Blogger blogger = bloggerMapper.find(); // 查询博主信息
            blogger.setPassword(null);
            application.setAttribute(CacheKey.BLOGGER, blogger);

            List<BlogType> blogTypeCountList = blogTypeMapper.countList(); // 查询博客类别以及博客的数量
            application.setAttribute(CacheKey.BLOG_TYPE_LIST, blogTypeCountList);
            List<Blog> blogCountList = blogMapper.countList(); // 根据日期分组查询博客
            application.setAttribute(CacheKey.BLOG_COUNT_LIST, blogCountList);

            List<Link> linkList = linkMapper.list(null); // 获取所有友情链接
            application.setAttribute(CacheKey.LINK_LIST, linkList);
            logger.info("initBlogData success");
        } catch (Exception e) {
            logger.error("initBlogData Error--Exception msg:" + e.getMessage());
            throw e;
        }
    }
}
