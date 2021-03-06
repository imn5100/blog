package com.shaw.service.impl;

import com.shaw.bo.Blog;
import com.shaw.bo.BlogType;
import com.shaw.bo.Blogger;
import com.shaw.bo.Link;
import com.shaw.constants.CacheKey;
import com.shaw.service.*;
import com.shaw.util.BoUtils;
import com.shaw.vo.BlogQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

@Service("systemService")
public class SystemServiceImpl implements SystemService {
    @Autowired
    private BloggerService bloggerService;

    @Autowired
    private BlogTypeService blogTypeService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private LinkService linkService;

    @Resource(name = "stringRedisTemplate")
    private RedisTemplate<String, String> stringRedisTemplate;

    Logger logger = LoggerFactory.getLogger(SystemServiceImpl.class);

    /**
     * 刷新系统缓存。
     */
    public void initBlogData(ServletContext application) {
        try {
            Blogger blogger = bloggerService.find(); // 查询博主信息
            blogger.setPassword(null);
            application.setAttribute(CacheKey.BLOGGER, blogger);
            List<BlogType> blogTypeCountList = blogTypeService.countList(); // 查询博客类别以及博客的数量
            application.setAttribute(CacheKey.BLOG_TYPE_LIST, blogTypeCountList);
            List<Blog> blogCountList = blogService.countList(); // 根据日期分组查询博客
            application.setAttribute(CacheKey.BLOG_COUNT_LIST, blogCountList);
            List<Link> linkList = linkService.list(null); // 获取所有友情链接
            application.setAttribute(CacheKey.LINK_LIST, linkList);
            BlogQuery query = new BlogQuery();
            query.setNoContent(true);
            List<Blog> blogs = blogService.list(query);
            int count = 0;
            List<Blog> updateBlog = new ArrayList<Blog>();
            for (Blog blog : blogs) {
                if (BoUtils.setClickHitFromRedis(blog, stringRedisTemplate)) {
                    updateBlog.add(blog);
                    count++;
                }
            }
            if (count > 0)
                blogService.updateBatchForClickHit(updateBlog);
            logger.info("initBlogData success");
        } catch (Exception e) {
            logger.error("initBlogData Error--Exception msg:" + e.getMessage());
            throw e;
        }
    }
}
