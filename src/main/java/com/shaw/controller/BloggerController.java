package com.shaw.controller;

import com.shaw.bo.Blogger;
import com.shaw.constants.CacheKey;
import com.shaw.constants.ResponseCode;
import com.shaw.service.BloggerService;
import com.shaw.service.impl.RedisClient;
import com.shaw.util.HttpResponseUtil;
import com.shaw.util.HttpRequestUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Set;

/**
 * 站长相关
 **/
@Controller
@RequestMapping("/blogger")
public class BloggerController {

    @Autowired
    private RedisClient redisClient;
    @Autowired
    private BloggerService bloggerService;
    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /**
     * 用户登录
     */
    @RequestMapping("/login")
    @ResponseBody
    public String login(HttpSession session, HttpServletResponse response, String username, String password,
                        String vcode) throws Exception {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(vcode)) {
            HttpResponseUtil.writeJsonStr(response, ResponseCode.PARAM_NULL.getCode());
        }
        String key = String.format(CacheKey.CODES_KEY, session.getId());
        String code = (String) redisClient.get(key);
        if (StringUtils.isBlank(code) || !vcode.equalsIgnoreCase(code)) {
            HttpResponseUtil.writeJsonStr(response, ResponseCode.CODES_WRONG.getCode());
            return null;
        } else {
            redisClient.del(key);
        }
        try {
            Blogger blogger = bloggerService.getByUserName(username);
            if (blogger != null && blogger.getPassword().equals(password)) {
                Subject subject = SecurityUtils.getSubject();
                UsernamePasswordToken token = new UsernamePasswordToken(username, password);
                subject.login(token); // 登录验证
                logger.info("login success username:" + username);
                HttpResponseUtil.writeJsonStr(response, ResponseCode.SUCCESS.getCode());
            } else {
                HttpResponseUtil.writeJsonStr(response, ResponseCode.LOGIN_WRONG.getCode());
            }
        } catch (AuthenticationException e) {
            HttpResponseUtil.writeJsonStr(response, ResponseCode.LOGIN_WRONG.getCode());
        }
        return null;
    }

    @RequestMapping("/script/login")
    @ResponseBody
    public void scriptLogin(HttpServletRequest request, HttpServletResponse response, String username, String password) throws Exception {
        String ip = HttpRequestUtil.getIpAddr(request);
        if (redisClient.sismember(CacheKey.WHITE_LIST_IP, ip)) {
            try {
                Blogger blogger = bloggerService.getByUserName(username);
                if (blogger != null && blogger.getPassword().equals(password)) {
                    Subject subject = SecurityUtils.getSubject();
                    UsernamePasswordToken token = new UsernamePasswordToken(username, password);
                    subject.login(token); // 登录验证
                    logger.info("script login success username:" + username);
                    HttpResponseUtil.writeJsonStr(response, ResponseCode.SUCCESS.getCode());
                } else {
                    HttpResponseUtil.writeCode(response, ResponseCode.LOGIN_WRONG);
                }
            } catch (AuthenticationException e) {
                HttpResponseUtil.writeCode(response, ResponseCode.LOGIN_WRONG);
            }
        } else {
            HttpResponseUtil.writeCode(response, ResponseCode.IP_WRONG);
        }
    }

    /**
     * 博主信息
     */
    @RequestMapping("/about")
    public ModelAndView aboutMe() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("mainPage", "/WEB-INF/foreground/blogger/info.jsp");
        mav.addObject("pageTitle", "Master");
        mav.setViewName("WEB-INF/mainPage");
        mav.addObject("aboutBloggerActive", true);
        return mav;
    }
}
