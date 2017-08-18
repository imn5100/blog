package com.shaw.test;

import com.shaw.bo.Blog;
import com.shaw.bo.Visitor;
import com.shaw.mapper.DiscussMapper;
import com.shaw.mapper.VisitorMapper;
import com.shaw.service.BlogService;
import org.jsoup.Jsoup;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by imn5100 on 2016/8/26 0026.
 */
public class ServiceTest extends SpringTestCase {

    @Autowired
    BlogService blogService;

    @Autowired
    private VisitorMapper visitorMapper;

    @Autowired
    private DiscussMapper discussMapper;

    /**
     * 调整数据，将summary 由简介改为 无html的
     */
    @org.junit.Test
    public void resetSummary() throws Exception {
        List<Blog> blogs = blogService.list(null);
        for (Blog blog : blogs) {
            String text = Jsoup.parse(blog.getContent()).text();
            if (text.length() > 300)
                blog.setSummary(text.substring(0, 300));
            else {
                blog.setSummary(text);
            }
            blogService.update(blog);
        }
    }


    @Test
    public void testMapper() {
        Visitor visitor = visitorMapper.selectByAccountAndFrom("imn5100", 1);
        System.out.println(visitor.getAccount());
        System.out.println(visitor.getAvatarUrl());
        System.out.println(visitor.getOauthFrom());
        System.out.println(visitor.getThirdId());
    }
}
