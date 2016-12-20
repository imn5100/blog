package com.shaw.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.shaw.bo.Blog;
import com.shaw.constants.CacheKey;
import com.shaw.constants.Constants;
import com.shaw.lucene.BlogIndex;
import com.shaw.service.BlogService;
import com.shaw.service.SystemService;
import com.shaw.service.impl.RedisClient;
import com.shaw.util.HttpResponseUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.lang.StringUtils;
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
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    @Autowired
    private RedisClient redisClient;
    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /**
     * 刷新系统缓存
     */
    @RequestMapping("/refreshSystem")
    public String refreshSystem(HttpServletResponse response, HttpServletRequest request) throws Exception {
        ServletContext application = RequestContextUtils.findWebApplicationContext(request).getServletContext();
        Long timestamp = new Long(System.currentTimeMillis());
        redisClient.set(CacheKey.SYSTEM_REFRESH_TIME, timestamp);
        systemService.initBlogData(application);
        application.setAttribute(CacheKey.SYSTEM_REFRESH_TIME, timestamp);
        JSONObject result = new JSONObject();
        result.put("success", true);
        logger.info("refresh System success");
        HttpResponseUtil.writeJsonStr(response, result);
        return null;
    }

    /***
     * 当索引出问题时，需要重写索引 重写索引
     */
    @RequestMapping("refreshIndex")
    public String refreshLuceneIndex(HttpServletResponse response) throws Exception {
        IndexWriter writer = blogIndex.getWriter();
        writer.deleteAll();
        logger.info("delete All lucene index success");
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
        HttpResponseUtil.writeJsonStr(response, result);
        return null;
    }

    /**
     * 重置blog简述长度
     */
    @RequestMapping("resetSummary")
    public String resetSummary(HttpServletResponse response, @RequestParam("length") Integer length) throws Exception {
        List<Blog> blogs = blogService.list(null);
        if (length == null || length <= 0 || length >= 400) {
            JSONObject result = new JSONObject();
            result.put("success", false);
            logger.warn("resetSummary fail,warming length:" + length);
            HttpResponseUtil.writeJsonStr(response, result);
            return null;
        }
        for (Blog blog : blogs) {
            String text = Jsoup.parse(blog.getContent()).text();
            if (text.length() > length)
                blog.setSummary(text.substring(0, length));
            else {
                blog.setSummary(text);
            }
        }
        //使用批量更新
        blogService.updateBatchForSummary(blogs);
        JSONObject result = new JSONObject();
        result.put("success", true);
        logger.info("resetSummary success");
        HttpResponseUtil.writeJsonStr(response, result);
        return null;
    }

    /**
     * 得到web日志分析报表html文件列表
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("getWebLogHtmlList")
    public String getWebLogHtmlList(HttpServletResponse response) throws Exception {
        List<String> filename = new ArrayList<String>();
        JSONObject result = new JSONObject();
        result.put("success", true);
        //查看是否存在缓存，如果存在，返回缓存内容
        if (redisClient.exists(CacheKey.WEB_LOGS_NAME_LIST_KEY)) {
            filename = (List<String>) redisClient.get(CacheKey.WEB_LOGS_NAME_LIST_KEY);
            if (CollectionUtils.isNotEmpty(filename)) {
                result.put("data", filename);
                HttpResponseUtil.writeJsonStr(response, result);
                return null;
            }
        }
        //执行到此，说明缓存无法取到filename列表，开始IO读取文件夹获取filename，获取和存入缓存
        //指定html报表 文件夹
        File htmlPath = new File(Constants.DEFAULT_WEB_LOGS_PATH);
        //构建过滤器，过滤非html文件
        FilenameFilter filter = FileFilterUtils.suffixFileFilter(".html");
        //判断文件夹是否存在
        if (htmlPath.exists() && htmlPath.isDirectory()) {
            //获取文件夹下所有文件名
            File[] accessFiles = htmlPath.listFiles(filter);
            Arrays.sort(accessFiles);
            if (accessFiles.length > 0) {
                for (File accessFile : accessFiles) {
                    filename.add(accessFile.getName());
                }
            }
        } else {
            logger.info("can't find dir:" + Constants.DEFAULT_WEB_LOGS_PATH);
            return null;
        }
        //结果存入redis 并返回
        redisClient.set(CacheKey.WEB_LOGS_NAME_LIST_KEY, filename);
        redisClient.expire(CacheKey.WEB_LOGS_NAME_LIST_KEY, CacheKey.WEB_LOGS_NAME_LIST_EXPIRE);
        result.put("data", filename);
        HttpResponseUtil.writeJsonStr(response, result);
        return null;
    }

    /**
     * 得到web日志分析报表html文件详情
     */
    @RequestMapping("getWebLogHtml")
    public String getWebLogHtml(HttpServletResponse response, @RequestParam("filename") String filename) throws Exception {
        if (StringUtils.isBlank(filename)) {
            logger.info("null filename!!");
            return null;
        }
        String key = String.format(CacheKey.WEB_LOGS_HTML_KEY, filename);
        if (redisClient.exists(key)) {  //是否缓存，缓存存在则直接返回缓存。
            String data = (String) redisClient.get(key);
            if (StringUtils.isNotBlank(data)) {
                HttpResponseUtil.writeJsonStr(response, data);
                return null;
            }
        }
        //若查找缓存失败 读取文件并写入缓存
        String data = readWebLogFile(filename);
        if (StringUtils.isNotBlank(data)) {
            redisClient.set(key, data);
            redisClient.expire(key, CacheKey.WEB_LOGS_HTML_EXPIRE);
            HttpResponseUtil.writeJsonStr(response, data);
        } else {
            HttpResponseUtil.writeJsonStr(response, "File not exists!");
        }
        return null;
    }

    /**
     * 使用org.apache.commons.io.FileUtils;读取报表html 返回，写入response
     */
    private String readWebLogFile(String filename) throws IOException {
        File file = new File(Constants.DEFAULT_WEB_LOGS_PATH + filename);
        if (file.exists()) {
            String data = FileUtils.readFileToString(file, "UTF-8");
            return data;
        } else {
            return null;
        }
    }
}
