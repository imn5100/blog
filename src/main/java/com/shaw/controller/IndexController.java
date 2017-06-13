package com.shaw.controller;

import com.shaw.bo.Blog;
import com.shaw.bo.Blogger;
import com.shaw.constants.Constants;
import com.shaw.service.BlogService;
import com.shaw.util.PageBean;
import com.shaw.util.PageUtil;
import com.shaw.util.StringUtil;
import com.shaw.util.TimeUtils;
import com.shaw.vo.BlogQuery;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主页跳转控制器
 * 包含 顶部菜单连接跳转
 */
@Controller
@RequestMapping("/")
public class IndexController {

    @Resource
    private BlogService blogService;


    /**
     * 主页，获取blog List信息显示
     */
    @RequestMapping("/index")
    public ModelAndView index(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "typeId", required = false) Integer typeId, @RequestParam(value = "releaseDateStr", required = false) String releaseDateStr, HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView();
        if (StringUtil.isEmpty(page)) {
            page = "1";
        }
        BlogQuery query = new BlogQuery(Integer.parseInt(page), Constants.PAGE_SIZE);
        if (typeId != null)
            query.setTypeId(typeId);
        if (StringUtil.isNotEmpty(releaseDateStr)) {
            releaseDateStr = StringUtil.filterSpChar(releaseDateStr);
            query.setReleaseDateStr(releaseDateStr);
        }
        query.setNoContent(true);
        List<Blog> blogList = blogService.list(query);
        for (Blog blog : blogList) {
            List<String> imagesList = blog.getImagesList();
            String blogInfo = blog.getContent();
            blog.setReleaseDateStr(TimeUtils.getTime(blog.getReleaseDate(), "yyyy年MM月dd日"));
            blog.setSummary(StringUtil.replaceStr(blog.getSummary()));
            Document doc = Jsoup.parse(blogInfo);
            //查找图片元素
            Elements jpgs = doc.select("img");
            //当有图片时，加载一张图片作为预览
            if (jpgs.size() > 0) {
                Element jpg = jpgs.get(0);
                imagesList.add(jpg.attr("src"));
            }
        }
        mav.addObject("blogList", blogList);
        StringBuffer param = new StringBuffer(); // 查询参数
        if (typeId != null) {
            param.append("typeId=" + typeId + "&");
        }
        if (StringUtil.isNotEmpty(releaseDateStr)) {
            param.append("releaseDateStr=" + releaseDateStr + "&");
        }
        mav.addObject("pageCode", PageUtil.genPagination(request.getContextPath(), blogService.getTotal(query), Integer.parseInt(page), Constants.PAGE_SIZE, param.toString()));
        mav.addObject("mainPage", "/WEB-INF/foreground/blog/list.jsp");
        mav.addObject("indexActive", true);
        mav.addObject("pageTitle", Constants.PAGE_TITLE);
        mav.setViewName("WEB-INF/index");
        return mav;
    }

    /**
     * 关于本站跳转
     */
    @RequestMapping("/about")
    public ModelAndView aboutSite() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("mainPage", "/WEB-INF/foreground/system/aboutSite.jsp");
        mav.addObject("pageTitle", Constants.PAGE_TITLE);
        mav.setViewName("WEB-INF/template");
        mav.addObject("aboutActive", true);
        return mav;
    }

    /**
     * 后台登陆页面跳转
     */
    @RequestMapping("/login")
    public ModelAndView loginPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Blogger blogger = (Blogger) request.getSession().getAttribute("currentUser");
        if (blogger != null && blogger.getId() != null) {
            response.sendRedirect("/admin/main.jsp");
            return null;
        } else {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("WEB-INF/login");
            return mav;
        }
    }

    /**
     * rainy room 跳转
     */
    @RequestMapping("/rainyRoom")
    public ModelAndView rainyRoom() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("WEB-INF/foreground/laboratory/rainy");
        return mav;
    }
}
