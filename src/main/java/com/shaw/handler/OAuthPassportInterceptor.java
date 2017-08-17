package com.shaw.handler;

import com.alibaba.fastjson.JSONObject;
import com.shaw.annotation.OAuthPassport;
import com.shaw.constants.Constants;
import com.shaw.util.HttpResponseUtil;
import com.shaw.util.PropertiesUtil;
import com.shaw.util.StringUtil;
import com.shaw.util.ThreadPoolManager;
import com.shaw.vo.GithubUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by shaw  on 2016/12/20 0020.
 * ip验证拦截器
 */
public class OAuthPassportInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private RestTemplate restTemplate;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String GITHUB_CLIENT_ID = PropertiesUtil.getConfiguration().getString("gitHub.client_id");
    public static final String GITHUB_CLIENT_SECRET = PropertiesUtil.getConfiguration().getString("gitHub.client_secret");
    public static final String GITHUB_TOKEN_URL = "https://github.com/login/oauth/access_token";
    public static final String GITHUB_USER_URL = "https://api.github.com/user";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            OAuthPassport oAuthPassport = ((HandlerMethod) handler).getMethodAnnotation(OAuthPassport.class);
            if (oAuthPassport == null || !oAuthPassport.validate()) {
                return true;
            } else {
                GithubUser githubUser = (GithubUser) request.getSession().getAttribute(Constants.OAUTH_USER);
                if (githubUser == null || !githubUser.valid()) {
                    String code = request.getParameter("code");
                    String state = request.getParameter("state");
                    if (StringUtil.isNotEmpty(code) && StringUtil.isNotEmpty(state)) {
                        try {
                            String token = getToken(code, state);
                            if (StringUtil.isNotEmpty(token)) {
                                githubUser = getUser(token);
                                if (githubUser != null && githubUser.valid()) {
                                    request.getSession().setAttribute(Constants.OAUTH_USER, githubUser);
                                    ThreadPoolManager.INSTANCE.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            //todo saveOrUpdate User
                                        }
                                    });
                                }else {
                                    HttpResponseUtil.redirect(response,"/404");
                                    return false;
                                }
                            } else {
                                HttpResponseUtil.redirect(response,"/404");
                                return false;
                            }
                        } catch (Exception e) {
                            logger.error("OAuth Login Fail:", e);
                            HttpResponseUtil.redirect(response,"/404");
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private String getToken(String code, String state) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("client_id", GITHUB_CLIENT_ID);
        params.add("client_secret", GITHUB_CLIENT_SECRET);
        params.add("code", code);
        if (StringUtil.isNotEmpty(state)) {
            params.add("state", state);
        }
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(GITHUB_TOKEN_URL, HttpMethod.POST, requestEntity, String.class);
        if (responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
            JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
            return jsonObject.getString("access_token");
        } else {
            if (responseEntity != null)
                logger.warn(responseEntity.toString());
            else
                logger.warn("Get Access_Token Response is NULL");
            return null;
        }
    }

    private GithubUser getUser(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        if (StringUtil.isNotEmpty(token)) {
            headers.add("Authorization", "token " + token);
            HttpEntity<?> userRequestEntity = new HttpEntity<>(headers);
            ResponseEntity<String> userResponse = restTemplate.exchange(GITHUB_USER_URL, HttpMethod.GET, userRequestEntity, String.class);
            if (userResponse != null && userResponse.getStatusCode() == HttpStatus.OK && userResponse.getBody() != null) {
                GithubUser githubUser = JSONObject.parseObject(userResponse.getBody(), GithubUser.class);
                if (githubUser != null && githubUser.valid()) {
                    githubUser.setMoreInfo(userResponse.getBody());
                    return githubUser;
                }
            }
        }
        return null;
    }
}
