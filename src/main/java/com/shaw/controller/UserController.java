package com.shaw.controller;

import com.alibaba.fastjson.JSONObject;
import com.shaw.constants.Constants;
import com.shaw.service.impl.RedisClient;
import com.shaw.util.DesUtils;
import com.shaw.util.PropertiesUtil;
import com.shaw.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
    public static final String GITHUB_CLIENT_ID = PropertiesUtil.getConfiguration().getString("gitHub.client_id");
    public static final String GITHUB_CLIENT_SECRET = PropertiesUtil.getConfiguration().getString("gitHub.client_secret");
    public static final String DEFAULT_SCOPE = "user";
    public static final String GITHUB_AUTHORIZE_URL = "http://github.com/login/oauth/authorize";
    public static final String GITHUB_TOKEN_URL = "https://github.com/login/oauth/access_token";
    public static final String GITHUB_USER_URL = "https://api.github.com/user";

    @Autowired
    private RedisClient redisClient;
    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/fromGithub")
    public ModelAndView loginByGithub(@RequestParam(name = "redirect") String redirect, HttpSession session) throws Exception {
        ModelAndView mav = new ModelAndView();
        String gitHubUser = (String) session.getAttribute("gitHubUser");
//        String userToken = (String) session.getAttribute("token");
        if (StringUtil.isNotEmpty(gitHubUser)) {
            System.out.println(gitHubUser);
            mav.setViewName("redirect:/");
            return mav;
        }
        String redirectUri = GITHUB_AUTHORIZE_URL + "?" +
                "client_id=" + GITHUB_CLIENT_ID + "&" +
                "scope=" + DEFAULT_SCOPE + "&" +
                "redirect_uri=" + redirect +
                "state=" + DesUtils.getDefaultInstance().encrypt(redirect) + "&";
        mav.setViewName("redirect:" + redirectUri);
        return mav;
    }

    @RequestMapping("/loginFromGithub")
    public ModelAndView loginFromGithub(@RequestParam(name = "code") String code, @RequestParam(name = "state", required = false) String state, HttpSession session) throws Exception {
        ModelAndView mav = new ModelAndView();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        //创建请求参数
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("client_id", GITHUB_CLIENT_ID);
        params.add("client_secret", GITHUB_CLIENT_SECRET);
        params.add("code", code);
        if (StringUtil.isNotEmpty(state)) {
            params.add("state", state);
        }
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(GITHUB_TOKEN_URL, HttpMethod.POST, requestEntity, String.class);
        System.out.println(requestEntity);
        System.out.println(responseEntity);
        if (responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
            JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
            String accessToken = jsonObject.getString("access_token");
            if (StringUtil.isNotEmpty(accessToken)) {
                headers.add("Authorization", "token " + accessToken);
                HttpEntity<?> userRequestEntity = new HttpEntity<>(headers);
                ResponseEntity<String> userResponse = restTemplate.exchange(GITHUB_USER_URL, HttpMethod.GET, userRequestEntity, String.class);
                if (userResponse != null && userResponse.getStatusCode() == HttpStatus.OK && userResponse.getBody() != null) {
                    session.setAttribute("userToken", accessToken);
                    session.setAttribute("gitHubUser", userResponse.getBody());
                    mav.setViewName("redirect:/");
                    return mav;
                }
            }
        }
        mav.setViewName("redirect:/404");
        return mav;

    }


}
