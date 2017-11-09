package com.shaw.test;

import com.shaw.bo.Blog;
import com.shaw.mapper.UploadFileMapper;
import com.shaw.service.BlogService;
import org.jsoup.Jsoup;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
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
        //get by id set cache
        Blog blog = blogService.findById(72);
        Blog blog2 = blogService.findById(112);
        Blog blog3 = blogService.findById(113);

        System.out.println(blog.getBlogCount());
        System.out.println(blog2.getBlogCount());
        System.out.println(blog3.getBlogCount());

        //see cache status
        Thread.sleep(1000 * 10);

        blog.setBlogCount(blog.getBlogCount() + 1);
        blog2.setBlogCount(blog2.getBlogCount() + 1);
        blog3.setBlogCount(blog3.getBlogCount() + 1);

        List<Blog> updateList = new ArrayList<>();
        updateList.add(blog);
        updateList.add(blog2);
        updateList.add(blog3);
        //update blog list delete cache
        blogService.updateBatchForClickHit(updateList);


    }

}
