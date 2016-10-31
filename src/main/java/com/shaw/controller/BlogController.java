package com.shaw.controller;

import com.shaw.bo.Blog;
import com.shaw.constants.CacheKey;
import com.shaw.constants.Constants;
import com.shaw.lucene.BlogIndex;
import com.shaw.service.BlogService;
import com.shaw.service.impl.RedisClient;
import com.shaw.util.CodesImgUtil;
import com.shaw.util.PageUtil;
import com.shaw.util.PropertiesUtil;
import com.shaw.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 博客相关
 */
@Controller
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private BlogIndex blogIndex;

    @Autowired
    private RedisClient redisClient;

    /**
     * 博客详情
     */
    @RequestMapping("/articles/{id}")
    public ModelAndView details(@PathVariable("id") Integer id, HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView();
        Blog blog = blogService.findById(id);
        if (blog == null) {
            mav.setViewName("redirect:/");
            return mav;
        }
        String keyWords = blog.getKeyWord();
        if (StringUtil.isNotEmpty(keyWords)) {
            String arr[] = keyWords.split(" ");
            mav.addObject("keyWords", StringUtil.filterWhite(Arrays.asList(arr)));
        } else {
            mav.addObject("keyWords", null);
        }
        mav.addObject("blog", blog);
        blog.setClickHit(blog.getClickHit() + 1); // 博客点击次数加1
        //避免频繁更新数据库 使用缓存存储blog 点击量
//        blogService.update(blog);
        String key = String.format(CacheKey.BLOG_CLICK_KEY, blog.getId());
        redisClient.incr(key);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("blogId", blog.getId());
        map.put("state", 1); // 查询审核通过的评论
        mav.addObject("pageCode", PageUtil.genUpAndDownPageCode(blogService.getLastBlog(id), blogService.getNextBlog(id),
                request.getServletContext().getContextPath()));
        mav.addObject("rootSite", PropertiesUtil.getConfiguration().getString(Constants.ROOT_SITE_KEY, Constants.DEFAULT_SITE));
        mav.addObject("mainPage", "foreground/blog/view.jsp");
        mav.addObject("sideNotLoad", true);
        mav.addObject("pageTitle", blog.getTitle());
        mav.setViewName("mainTemp");
        return mav;
    }

    /**
     * *搜索入口
     */
    @RequestMapping("/q")
    public ModelAndView search(@RequestParam(value = "q", required = false, defaultValue = "") String q,
                               @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                               HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView();
        if (StringUtils.isBlank(q)) {
            mav.setViewName("redirect:/");
            return mav;
        }
        mav.addObject("mainPage", "foreground/blog/result.jsp");
        List<Blog> blogList = blogIndex.searchBlog(q.trim());
        Integer toIndex = blogList.size() >= page * Constants.PAGE_SIZE ? (page) * Constants.PAGE_SIZE
                : blogList.size();
        List<Blog> blogList2 = new ArrayList<Blog>();
        // ArrayList subList 返回的类型为 SubList
        // 。jsp页面访问SubList集合时可能报错。这里直接使用Arraylist 返回集合
        blogList2.addAll(blogList.subList(((page) - 1) * Constants.PAGE_SIZE, toIndex));
        mav.addObject("blogList", blogList2);
        mav.addObject("pageCode", PageUtil.genUpAndDownPageCode((page), blogList.size(), q, Constants.PAGE_SIZE,
                request.getServletContext().getContextPath()));
        mav.addObject("q", q);
        mav.addObject("resultTotal", blogList.size());
        mav.addObject("pageTitle", Constants.PAGE_TITLE + " 搜索'" + q + "'");
        mav.setViewName("mainTemp");
        return mav;
    }

    /**
     * 获取验证码接口，
     * 分布式服务时需要 spring-session 支持
     */
    @RequestMapping("/codesImg")
    public void getCodes(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String codes = CodesImgUtil.getCodesImg(response, request);
        String sessionId = request.getSession().getId();
        String key = String.format(CacheKey.CODES_KEY, sessionId);
        redisClient.set(key, codes);
        redisClient.expire(key, CacheKey.CODES_EXPIRE);
        return;
    }

}
