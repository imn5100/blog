package com.shaw.controller.admin;


import com.alibaba.fastjson.JSONObject;
import com.shaw.bo.Blog;
import com.shaw.constants.Constants;
import com.shaw.lucene.BlogIndex;
import com.shaw.service.BlogService;
import com.shaw.util.HttpResponseUtil;
import com.shaw.util.PageBean;
import com.shaw.util.StringUtil;
import com.shaw.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author imn5100
 */
@Controller
@RequestMapping("/admin/blog")
public class BlogAdminController {

    @Resource
    private BlogService blogService;

    @Autowired
    private BlogIndex blogIndex;

    /**
     * 添加或者修改博客信息
     */
    @RequestMapping("/save")
    public String save(Blog blog, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        if (blog.getContent().length() > Constants.MAX_WORDS) {
            result.put("success", false);
            result.put("msg", "文章内容超过Text限制,你可能需要改变表字段了,或是缩减文章");
            HttpResponseUtil.writeJsonStr(response, result);
            return null;
        }
        // 操作的记录条数
        int resultTotal;
        if (blog.getId() == null) {
            blog.setReleaseDate(new Date());
            resultTotal = blogService.add(blog);
            // 添加博客索引
            blogIndex.addIndex(blog);
        } else {
            blog.setReleaseDate(blogService.findById(blog.getId()).getReleaseDate());
            resultTotal = blogService.update(blog);
            // 更新博客索引
            blogIndex.updateIndex(blog);
        }
        if (resultTotal > 0) {
            result.put("success", true);
        } else {
            result.put("success", false);
        }
        HttpResponseUtil.writeJsonStr(response, result);
        return null;
    }

    /**
     * 分页查询博客信息
     */
    @RequestMapping("/list")
    public String list(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "rows", required = false) String rows, Blog s_blog, HttpServletResponse response) throws Exception {
        BlogQuery query = new BlogQuery(Integer.parseInt(page), Integer.parseInt(rows));
        query.setTitle(StringUtil.formatLike(s_blog.getTitle()));
        List<Blog> blogList = blogService.listSimple(query);
        Long total = blogService.getTotal(query);
        JSONObject result = new JSONObject();
        result.put("rows", blogList);
        result.put("total", total);
        HttpResponseUtil.writeJsonStr(response, result.toJSONString());
        return null;
    }

    /**
     * 删除博客信息
     */
    @RequestMapping("/delete")
    public String delete(@RequestParam(value = "ids") String ids, HttpServletResponse response) throws Exception {
        String[] idsStr = ids.split(",");
        for (int i = 0; i < idsStr.length; i++) {
            blogService.delete(Integer.parseInt(idsStr[i]));
            // 删除对应博客的索引
            blogIndex.deleteIndex(idsStr[i]);
        }
        JSONObject result = new JSONObject();
        result.put("success", true);
        HttpResponseUtil.writeJsonStr(response, result);
        return null;
    }

    /**
     * 通过ID查找实体
     */
    @RequestMapping("/findById")
    public String findById(@RequestParam(value = "id") String id, HttpServletResponse response) throws Exception {
        Blog blog = blogService.findById(Integer.parseInt(id));
        HttpResponseUtil.writeJsonStr(response, JSONObject.toJSONString(blog));
        return null;
    }


}
