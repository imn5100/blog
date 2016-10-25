package com.shaw.controller;

import com.shaw.bo.Blogger;
import com.shaw.constants.CacheKey;
import com.shaw.constants.ResponseCode;
import com.shaw.service.BloggerService;
import com.shaw.service.impl.RedisClient;
import com.shaw.util.HttpResponseUtil;
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
     * 用户登录
     */
    @RequestMapping("/login")
    @ResponseBody
    public String login(HttpSession session, HttpServletResponse response, String username, String password,
                        String vcode) throws Exception {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(vcode)) {
            HttpResponseUtil.write(response, ResponseCode.PARAM_NULL.getCode());
        }
        String key = String.format(CacheKey.CODES_KEY, session.getId());
        String code = (String) redisClient.get(key);
        if (StringUtils.isBlank(code) || !vcode.equalsIgnoreCase(code)) {
            HttpResponseUtil.write(response, ResponseCode.CODES_WRONG.getCode());
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
                logger.info("login success username:" + username + "->md5(password):" + password);
                HttpResponseUtil.write(response, ResponseCode.SUCCESS.getCode());
            } else {
                HttpResponseUtil.write(response, ResponseCode.LOGIN_WRONG.getCode());
            }
        } catch (AuthenticationException e) {
            HttpResponseUtil.write(response, ResponseCode.LOGIN_WRONG.getCode());
        }
        return null;
    }

    /**
     * 博主信息
     */
    @RequestMapping("/aboutBlogger")
    public ModelAndView aboutMe() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("mainPage", "foreground/blogger/info.jsp");
        mav.addObject("pageTitle", "Bloger");
        mav.setViewName("mainTemp");
        mav.addObject("aboutBloggerActive", true);
        return mav;
    }
}
