package com.shaw.controller;

import com.shaw.annotation.OAuthPassport;
import com.shaw.bo.Blog;
import com.shaw.bo.Visitor;
import com.shaw.constants.CacheKey;
import com.shaw.constants.Constants;
import com.shaw.constants.ResponseCode;
import com.shaw.lucene.BlogIndex;
import com.shaw.service.BlogService;
import com.shaw.service.DiscussService;
import com.shaw.service.impl.RedisClient;
import com.shaw.util.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 博客内容相关控制器
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

    @Autowired
    private DiscussService discussService;

    /**
     * 跳转 博客详情页
     */
    @RequestMapping("/{id}")
    @OAuthPassport
    public ModelAndView details(@PathVariable("id") String encodeId, HttpServletRequest request) throws Exception {
        int id;
        ModelAndView mav = new ModelAndView();
        if (NumberUtils.is(encodeId)) {
            id = NumberUtils.parseIntQuietly(encodeId);
            if (id != 0) {
                mav.setViewName("redirect:/blog/" + CodecUtils.getEncodeId(id) + ".html");
            } else {
                mav.setViewName("redirect:/");
            }
            return mav;
        } else {
            id = CodecUtils.getDecodeId(encodeId);
        }
        if (id == 0) {
            mav.setViewName("redirect:/");
            return mav;
        }
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
        mav.addObject("lastBlog", blogService.getLastBlog(id));
        mav.addObject("nextBlog", blogService.getNextBlog(id));
        mav.addObject("rootSite", PropertiesUtil.getConfiguration().getString(Constants.ROOT_SITE_KEY, Constants.DEFAULT_SITE));
        mav.addObject("mainPage", "/WEB-INF/foreground/blog/view.jsp");
        mav.addObject("pageTitle", blog.getTitle());
        mav.setViewName("WEB-INF/template");
        return mav;
    }

    /**
     * 搜索博客
     */
    @RequestMapping("/search")
    public ModelAndView search(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                               @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                               HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView();
        keyword = StringUtil.filterSpChar(keyword);
        if (StringUtils.isBlank(keyword)) {
            mav.setViewName("redirect:/");
            return mav;
        }
        mav.addObject("mainPage", "/WEB-INF/foreground/blog/result.jsp");
        List<Blog> blogList = blogIndex.searchBlog(keyword);
        int toIndex = blogList.size() >= page * Constants.PAGE_SIZE ? (page) * Constants.PAGE_SIZE
                : blogList.size();
        int fromIndex = (page - 1) * Constants.PAGE_SIZE;
        List<Blog> blogList2 = new ArrayList<Blog>();
        // ArrayList subList 返回的类型为 SubList
        // 。jsp页面访问SubList集合时可能报错。这里直接使用Arraylist 返回集合
        if (fromIndex < blogList.size()) {
            blogList2.addAll(blogList.subList((page - 1) * Constants.PAGE_SIZE, toIndex));
        }
        mav.addObject("blogList", blogList2);
        mav.addObject("pageCode", PageUtil.genUpAndDownPageCode((page), blogList.size(), keyword, Constants.PAGE_SIZE,
                request.getServletContext().getContextPath()));
        mav.addObject("keyword", keyword);
        mav.addObject("rootSite", PropertiesUtil.getConfiguration().getString(Constants.ROOT_SITE_KEY, Constants.DEFAULT_SITE));
        mav.addObject("resultTotal", blogList.size());
        mav.addObject("pageTitle", Constants.PAGE_TITLE + "-" + keyword);
        mav.setViewName("WEB-INF/template");
        return mav;
    }

    @RequestMapping(value = "/discussList", method = RequestMethod.GET)
    public void getDiscuss(@RequestParam(value = "blogId") String encodeId, HttpServletResponse response) throws Exception {
        int blogId = CodecUtils.getDecodeId(encodeId);
        if (blogId != 0) {
            HttpResponseUtil.writeUseData(response, discussService.getCommentListByBlogId(blogId), ResponseCode.SUCCESS);
        } else {
            HttpResponseUtil.writeCode(response, ResponseCode.PARAM_NOT_FORMAT);
        }
    }


    @RequestMapping(value = "/submitDiscuss", method = RequestMethod.POST)
    public void submitDiscuss(@RequestParam(value = "blogId") String encodeId, @RequestParam(value = "content") String content, HttpSession session, HttpServletResponse response) throws Exception {
        Visitor visitor = (Visitor) session.getAttribute(Constants.OAUTH_USER);
        if (visitor != null) {
            int blogId = CodecUtils.getDecodeId(encodeId);
            if (blogId != 0 && 0 != visitor.getId() && StringUtil.isNotEmpty(content)) {
                if (discussService.submitDiscuss(blogId, visitor.getId(), content) > 0) {
                    HttpResponseUtil.writeCode(response, ResponseCode.SUCCESS);
                } else {
                    HttpResponseUtil.writeCode(response, ResponseCode.FAIL);
                }
            } else {
                HttpResponseUtil.writeCode(response, ResponseCode.PARAM_NOT_FORMAT);
            }
        } else {
            HttpResponseUtil.writeCode(response, ResponseCode.NOT_LOGIN);
        }
        return;
    }

}
