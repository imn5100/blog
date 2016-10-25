package com.shaw.test;

import java.util.List;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;

import com.shaw.bo.Blog;
import com.shaw.service.BlogService;

/**
 * Created by imn5100 on 2016/8/26 0026.
 */
public class MapperTest extends SpringTestCase {

    @Autowired
    BlogService blogService;

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
}
