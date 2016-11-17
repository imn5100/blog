package com.shaw.test;

import com.shaw.bo.Blog;
import com.shaw.bo.UploadFile;
import com.shaw.mapper.UploadFileMapper;
import com.shaw.service.BlogService;
import com.shaw.service.UploadFileService;
import org.jsoup.Jsoup;
import org.junit.*;
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
}
