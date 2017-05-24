package com.shaw.controller;

import com.shaw.annotation.IpPassport;
import com.shaw.bo.Blogger;
import com.shaw.constants.CacheKey;
import com.shaw.constants.ResponseCode;
import com.shaw.service.BloggerService;
import com.shaw.service.impl.RedisClient;
import com.shaw.util.CodesImgUtil;
import com.shaw.util.HttpResponseUtil;
import com.shaw.util.StringUtil;
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
     * 登录
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
    @IpPassport
    public void scriptLogin(HttpServletRequest request, HttpServletResponse response, String username, String password) throws Exception {
        if (StringUtil.isEmpty(username) || StringUtil.isEmpty(password)) {
            HttpResponseUtil.writeCode(response, ResponseCode.PARAM_NULL);
            return;
        }
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
    }

    /**
     * 博主信息
     */
    @RequestMapping("/about")
    public ModelAndView aboutMe() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("mainPage", "/WEB-INF/foreground/blogger/info.jsp");
        mav.addObject("pageTitle", "Music");
        mav.setViewName("WEB-INF/mainPage");
        mav.addObject("aboutBloggerActive", true);
        return mav;
    }

    /**
     * 获取验证码接口
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
