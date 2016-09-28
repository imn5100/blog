package com.shaw.controller;

import com.shaw.constants.CacheKey;
import com.shaw.constants.Constants;
import com.shaw.entity.Blog;
import com.shaw.entity.Comment;
import com.shaw.lucene.BlogIndex;
import com.shaw.service.BlogService;
import com.shaw.service.CommentService;
import com.shaw.service.impl.RedisClient;
import com.shaw.util.CodesImgUtil;
import com.shaw.util.ResponseUtil;
import com.shaw.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

@Controller
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogIndex blogIndex;

    /**
     * 请求博客详细信息
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/articles/{id}")
    public ModelAndView details(@PathVariable("id") Integer id, HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView();
        Blog blog = blogService.findById(id);
        String keyWords = blog.getKeyWord();
        if (StringUtil.isNotEmpty(keyWords)) {
            String arr[] = keyWords.split(" ");
            mav.addObject("keyWords", StringUtil.filterWhite(Arrays.asList(arr)));
        } else {
            mav.addObject("keyWords", null);
        }
        mav.addObject("blog", blog);
        blog.setClickHit(blog.getClickHit() + 1); // 博客点击次数加1
        blogService.update(blog);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("blogId", blog.getId());
        map.put("state", 1); // 查询审核通过的评论
        List<Comment> list = commentService.list(map);
        blog.setReplyHit(list.size());
        mav.addObject("commentList", list);
        mav.addObject("pageCode", this.genUpAndDownPageCode(blogService.getLastBlog(id), blogService.getNextBlog(id),
                request.getServletContext().getContextPath()));
        mav.addObject("mainPage", "foreground/blog/view.jsp");
        mav.addObject("pageTitle", blog.getTitle());
        mav.setViewName("mainTemp");
        return mav;
    }

    /**
     * 根据关键字查询相关博客信息
     *
     * @param q
     * @return
     * @throws Exception
     */
    @RequestMapping("/q")
    public ModelAndView search(@RequestParam(value = "q", required = false, defaultValue = "") String q,
                               @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                               HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView();
        if (StringUtils.isBlank(q)) {
            mav.setViewName("redirect:/index.html");
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
        mav.addObject("pageCode", this.genUpAndDownPageCode((page), blogList.size(), q, Constants.PAGE_SIZE,
                request.getServletContext().getContextPath()));
        mav.addObject("q", q);
        mav.addObject("resultTotal", blogList.size());
        mav.addObject("pageTitle", "搜索关键字'" + q + "'JavaBlog Result Page");
        mav.setViewName("mainTemp");
        return mav;
    }


    @Autowired
    private RedisClient redisClient;

    /**
     * 集群环境
     * 评论验证码
     * 这种模式一直刷新，容易导致redis 溢出
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/codesImgV2")
    public void getCodesV2(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //设置codesId用于获取验证码,过期时间和redis相同
        String codesId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        ResponseUtil.addCookie(response, CacheKey.CODES_COOKIE_KEY, codesId, CacheKey.CODES_EXPIRE);

        //获取验证码，并response回写验证码图片
        String codes = CodesImgUtil.getCodesImg(response, request, Constants.VCODE_VERSION_2);

        //验证码存入 redis并设置1 min过期。
        String key = String.format(CacheKey.CODES_KEY, codesId);
        redisClient.set(key, codes);
        redisClient.expire(key, CacheKey.CODES_EXPIRE * 1L);
        return;
    }

    /**
     * 非集群环境验证码
     * 或配置nginx 完成session共享 可使用此验证码功能
     */
    @RequestMapping("/codesImg")
    public void getCodes(HttpServletRequest request, HttpServletResponse response) throws Exception {
        CodesImgUtil.getCodesImg(response, request, Constants.VCODE_VERSION_1);
        return;
    }

    /**
     * 获取下一篇博客和下一篇博客代码
     *
     * @param lastBlog
     * @param nextBlog
     * @return
     */
    private String genUpAndDownPageCode(Blog lastBlog, Blog nextBlog, String projectContext) {
        StringBuffer pageCode = new StringBuffer();
        if (lastBlog == null || lastBlog.getId() == null) {
            pageCode.append("<p>上一篇：没有了</p>");
        } else {
            pageCode.append("<p>上一篇：<a href='" + projectContext + "/blog/articles/" + lastBlog.getId() + ".html'>"
                    + lastBlog.getTitle() + "</a></p>");
        }
        if (nextBlog == null || nextBlog.getId() == null) {
            pageCode.append("<p>下一篇：没有了</p>");
        } else {
            pageCode.append("<p>下一篇：<a href='" + projectContext + "/blog/articles/" + nextBlog.getId() + ".html'>"
                    + nextBlog.getTitle() + "</a></p>");
        }
        return pageCode.toString();
    }

    /**
     * 获取上一页，下一页代码 查询博客用到
     *
     * @param page           当前页
     * @param totalNum       总记录数
     * @param q              查询关键字
     * @param pageSize       每页大小
     * @param projectContext
     * @return
     */
    private String genUpAndDownPageCode(Integer page, Integer totalNum, String q, Integer pageSize,
                                        String projectContext) {
        long totalPage = totalNum % pageSize == 0 ? totalNum / pageSize : totalNum / pageSize + 1;
        StringBuffer pageCode = new StringBuffer();
        if (totalPage == 0) {
            return "";
        } else {
            pageCode.append("<nav>");
            pageCode.append("<ul class='pager' >");
            if (page > 1) {
                pageCode.append("<li><a href='" + projectContext + "/blog/q.html?page=" + (page - 1) + "&q=" + q
                        + "'>上一页</a></li>");
            } else {
                pageCode.append("<li class='disabled'><a href='#'>上一页</a></li>");
            }
            if (page < totalPage) {
                pageCode.append("<li><a href='" + projectContext + "/blog/q.html?page=" + (page + 1) + "&q=" + q
                        + "'>下一页</a></li>");
            } else {
                pageCode.append("<li class='disabled'><a href='#'>下一页</a></li>");
            }
            pageCode.append("</ul>");
            pageCode.append("</nav>");
        }
        return pageCode.toString();
    }
}
