package com.shaw.controller;

import com.shaw.bo.Visitor;
import com.shaw.constants.Constants;
import com.shaw.util.DesUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
    @Value("${gitHub.client_id}")
    private String GITHUB_CLIENT_ID;
    private static final String DEFAULT_SCOPE = "";
    private static final String GITHUB_AUTHORIZE_URL = "http://github.com/login/oauth/authorize";

    @RequestMapping("/fromGithub")
    public ModelAndView loginByGithub(@RequestParam(name = "redirect") String redirect, HttpSession session) throws Exception {
        ModelAndView mav = new ModelAndView();
        Visitor gitHubUser = (Visitor) session.getAttribute(Constants.OAUTH_USER);
        if (gitHubUser != null) {
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
