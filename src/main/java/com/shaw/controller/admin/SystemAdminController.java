package com.shaw.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.shaw.bo.Blog;
import com.shaw.lucene.BlogIndex;
import com.shaw.service.BlogService;
import com.shaw.service.SystemService;
import com.shaw.util.HttpResponseUtil;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/admin/system")
public class SystemAdminController {

    @Autowired
    private SystemService systemService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private BlogIndex blogIndex;
    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /**
     * 刷新系统缓存
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/refreshSystem")
    public String refreshSystem(HttpServletResponse response, HttpServletRequest request) throws Exception {
        ServletContext application = RequestContextUtils.getWebApplicationContext(request).getServletContext();
        systemService.initBlogData(application);
        JSONObject result = new JSONObject();
        result.put("success", true);
        logger.info("refresh System success");
        HttpResponseUtil.write(response, result);
        return null;
    }

    /***
     * 当索引出问题时，需要重写索引 重写索引
     */
    @RequestMapping("refreshIndex")
    public String refreshLuceneIndex(HttpServletResponse response) throws Exception {
        IndexWriter writer = blogIndex.getWriter();
        writer.deleteAll();
        logger.info("deleteAll lucene index success");
        List<Blog> blogs = blogService.list(null);
        for (Blog blog : blogs) {
            Document doc = new Document();
            doc.add(new StringField("id", String.valueOf(blog.getId()), Field.Store.YES));
            doc.add(new TextField("title", blog.getTitle(), Field.Store.YES));
            doc.add(new TextField("content", Jsoup.parse(blog.getContent()).text(), Field.Store.YES));
            doc.add(new LongField("time", blog.getReleaseDate().getTime(), BlogIndex.TIME_TYPE));
            writer.addDocument(doc);
        }
        writer.close();
        JSONObject result = new JSONObject();
        result.put("success", true);
        logger.info("refresh lucene index success");
        HttpResponseUtil.write(response, result);
        return null;
    }

    @RequestMapping("resetSummary")
    public String resetSummary(HttpServletResponse response, @RequestParam("length") Integer length) throws Exception {
        List<Blog> blogs = blogService.list(null);
        if (length == null || length <= 0 || length >= 400) {
            JSONObject result = new JSONObject();
            result.put("success", false);
            logger.warn("resetSummary fail,warming length:" + length);
            HttpResponseUtil.write(response, result);
            return null;
        }
        for (Blog blog : blogs) {
            String text = Jsoup.parse(blog.getContent()).text();
            if (text.length() > length)
                blog.setSummary(text.substring(0, length));
            else {
                blog.setSummary(text);
            }
            blogService.update(blog);
        }
        JSONObject result = new JSONObject();
        result.put("success", true);
        logger.info("resetSummary success");
        HttpResponseUtil.write(response, result);
        return null;
    }
}
