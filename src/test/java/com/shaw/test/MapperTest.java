package com.shaw.test;

import com.shaw.bo.Blog;
import com.shaw.mapper.UploadFileMapper;
import com.shaw.service.BlogService;
import org.jsoup.Jsoup;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * Created by imn5100 on 2016/8/26 0026.
 */
public class MapperTest extends SpringTestCase {

    @Autowired
    BlogService blogService;
    @Autowired
    UploadFileMapper uploadFileMapper;

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

    @org.junit.Test
    public void testBatchDelete() {
        List<String> keys = Arrays.asList(new String[]{"misaki", "sakura"});
        uploadFileMapper.deleteQiniuByKey(keys);
    }

    @org.junit.Test
    public void testCache() throws InterruptedException {
        Blog blog = blogService.findById(72);
        System.out.println(blog.getBlogCount());
        blog.setBlogCount(blog.getBlogCount() + 1);
        //暂停10s查看缓存是否写入
        //ttl blog_72
        Thread.sleep(1000 * 10);
        blogService.update(blog);
    }

}
