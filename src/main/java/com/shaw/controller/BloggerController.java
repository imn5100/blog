package com.shaw.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shaw.util.ResponseUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/blogger")
public class BloggerController {

    /**
     * 用户登录
     *
     * @param request
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public String login(HttpServletRequest request, HttpServletResponse response, String username, String password) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token); // 登录验证
            ResponseUtil.write(response, "200");
        } catch (AuthenticationException e) {
            ResponseUtil.write(response, "999");
        }
        return null;
    }

    /**
     * 查找博主信息
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/aboutBlogger")
    public ModelAndView aboutMe() throws Exception {
        ModelAndView mav = new ModelAndView();
        //保证属于一致性。不刷新 否则会出现 博主页面和其他页面右侧博主信息不一致情况。统一到 System全局刷新 显示。
//		mav.addObject("blogger",bloggerService.find());
        mav.addObject("mainPage", "foreground/blogger/info.jsp");
        mav.addObject("pageTitle", "Bloger");
        mav.setViewName("mainTemp");
        return mav;
    }
}
