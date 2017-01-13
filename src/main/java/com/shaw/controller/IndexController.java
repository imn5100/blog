package com.shaw.controller;

import com.shaw.bo.Blog;
import com.shaw.bo.TaskUser;
import com.shaw.constants.CacheKey;
import com.shaw.constants.Constants;
import com.shaw.constants.ResponseCode;
import com.shaw.service.BlogService;
import com.shaw.service.TaskUserService;
import com.shaw.util.*;
import com.shaw.vo.RemoteTaskPermission;
import org.apache.commons.lang3.StringUtils;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主页跳转控制器
 */
@Controller
@RequestMapping("/")
public class IndexController {

    @Resource
    private BlogService blogService;
    @Resource
    private TaskUserService taskUserService;


    /**
     * 主页，获取blog List信息显示
     */
    @RequestMapping("/index")
    public ModelAndView index(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "typeId", required = false) Integer typeId, @RequestParam(value = "releaseDateStr", required = false) String releaseDateStr, HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView();
        if (StringUtil.isEmpty(page)) {
            page = "1";
        }
        PageBean pageBean = new PageBean(Integer.parseInt(page), Constants.PAGE_SIZE);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("start", pageBean.getStart());
        map.put("size", pageBean.getPageSize());
        if (typeId != null)
            map.put("typeId", typeId);
        if (StringUtil.isNotEmpty(releaseDateStr)) {
            releaseDateStr = StringUtil.filterSpChar(releaseDateStr);
            map.put("releaseDateStr", releaseDateStr);
        }
        List<Blog> blogList = blogService.list(map);
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
        mav.addObject("pageCode", PageUtil.genPagination(request.getContextPath() + "/index.html", blogService.getTotal(map), Integer.parseInt(page), Constants.PAGE_SIZE, param.toString()));
        mav.addObject("mainPage", "/WEB-INF/foreground/blog/list.jsp");
        mav.addObject("indexActive", true);
        mav.addObject("pageTitle", Constants.PAGE_TITLE);
        mav.setViewName("WEB-INF/mainPage");
        return mav;
    }

    /**
     * 关于本站跳转
     */
    @RequestMapping("/about")
    public ModelAndView download() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("mainPage", "/WEB-INF/foreground/system/aboutSite.jsp");
        mav.addObject("pageTitle", Constants.PAGE_TITLE);
        mav.setViewName("WEB-INF/mainPage");
        mav.addObject("aboutActive", true);
        return mav;
    }

    /**
     * 后台登陆页面跳转
     */
    @RequestMapping("/login")
    public ModelAndView loginPage() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("WEB-INF/login");
        return mav;
    }

    @RequestMapping("/rainyRoom")
    public ModelAndView rainyRoom() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("WEB-INF/foreground/laboratory/rainy");
        return mav;
    }


    public static final String LOGIN_SUCCESS_FLAG = "loginSuccess";

    @RequestMapping("/remoteTask")
    public ModelAndView remoteTask(String ak, String as, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("WEB-INF/foreground/laboratory/remoteTask");
        TaskUser taskUser = (TaskUser) request.getSession().getAttribute(CacheKey.TASK_USER_AUTH);
        request.setAttribute(LOGIN_SUCCESS_FLAG, false);
        if (taskUser != null) {
            request.setAttribute(LOGIN_SUCCESS_FLAG, true);
        } else {
            if (!StringUtils.isEmpty(ak) && !StringUtils.isEmpty(as) && ak.length() == 32 && as.length() == 32) {
                taskUser = taskUserService.selectByPrimaryKey(ak);
                if (taskUser != null && taskUser.getAppsecret().equals(as)) {
                    taskUser.setActiveTime(System.currentTimeMillis());
                    //  0000 0000   0位为1，下载权限 1位为1 python脚本执行权限
                    List<RemoteTaskPermission> list = new ArrayList<>();
                    if ((taskUser.getPermissions() & 0x1) == 0x1) {
                        list.add(RemoteTaskPermission.DOWNLOAD);
                    }
                    if ((taskUser.getPermissions() & 0x2) == 0x2) {
                        list.add(RemoteTaskPermission.PYTHON);
                    }
                    taskUser.setRemoteTaskPermissionList(list);
                    taskUserService.updateByPrimaryKeySelective(taskUser);
                    taskUser.setShowActiveTime(TimeUtils.getFormatTime(taskUser.getActiveTime()));
                    request.getSession().setAttribute(CacheKey.TASK_USER_AUTH, taskUser);
                    request.setAttribute(LOGIN_SUCCESS_FLAG, true);
                }
            }
        }
        return mav;
    }

    @RequestMapping("/remoteTask/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getSession().removeAttribute(CacheKey.TASK_USER_AUTH);
        request.removeAttribute(LOGIN_SUCCESS_FLAG);
        HttpResponseUtil.writeCode(response, ResponseCode.SUCCESS);
    }

}
