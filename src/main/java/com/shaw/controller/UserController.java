package com.shaw.controller;

import com.shaw.constants.Constants;
import com.shaw.util.DesUtils;
import com.shaw.util.PropertiesUtil;
import com.shaw.vo.GithubUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
    public static final String GITHUB_CLIENT_ID = PropertiesUtil.getConfiguration().getString("gitHub.client_id");
    public static final String DEFAULT_SCOPE = "";
    public static final String GITHUB_AUTHORIZE_URL = "http://github.com/login/oauth/authorize";

    @RequestMapping("/fromGithub")
    public ModelAndView loginByGithub(@RequestParam(name = "redirect") String redirect, HttpSession session) throws Exception {
        ModelAndView mav = new ModelAndView();
        GithubUser gitHubUser = (GithubUser) session.getAttribute(Constants.OAUTH_USER);
        if (gitHubUser != null && gitHubUser.valid()) {
            mav.setViewName("redirect:/");
        } else {
            String redirectUri = GITHUB_AUTHORIZE_URL + "?" +
                    "client_id=" + GITHUB_CLIENT_ID + "&" +
                    "scope=" + DEFAULT_SCOPE + "&" +
                    "redirect_uri=" + redirect + "&" +
                    "state=" + DesUtils.getDefaultInstance().encrypt(redirect) + "&";
            mav.setViewName("redirect:" + redirectUri);
        }
        return mav;
    }

}
