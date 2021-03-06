package com.shaw.handler;

import com.alibaba.fastjson.JSONObject;
import com.shaw.annotation.OAuthPassport;
import com.shaw.bo.Visitor;
import com.shaw.constants.Constants;
import com.shaw.service.VisitorService;
import com.shaw.util.HttpResponseUtil;
import com.shaw.util.StringUtil;
import com.shaw.vo.GithubUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * github oauth回调拦截器。拦截github回调，获取有效用户，设置用户登录状态
 */
public class OAuthPassportInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private VisitorService visitorService;

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${gitHub.client_id}")
    private String GITHUB_CLIENT_ID;
    @Value("${gitHub.client_secret}")
    private String GITHUB_CLIENT_SECRET;
    private static final String GITHUB_TOKEN_URL = "https://github.com/login/oauth/access_token";
    private static final String GITHUB_USER_URL = "https://api.github.com/user";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            OAuthPassport oAuthPassport = ((HandlerMethod) handler).getMethodAnnotation(OAuthPassport.class);
            if (oAuthPassport == null || !oAuthPassport.validate()) {
                return true;
            } else {
                Visitor visitor = (Visitor) request.getSession().getAttribute(Constants.OAUTH_USER);
                if (visitor == null) {
                    String code = request.getParameter("code");
                    String state = request.getParameter("state");
                    if (StringUtil.isNotEmpty(code) && StringUtil.isNotEmpty(state)) {
                        try {
                            String token = getToken(code, state);
                            if (StringUtil.isNotEmpty(token)) {
                                GithubUser githubUser = getUser(token);
                                if (githubUser != null && githubUser.valid()) {
                                    //更新或插入访客信息
                                    visitor = visitorService.updateOrInsertByAccountAndFrom(githubUser.toVisitor());
                                    if (visitor != null) {
                                        request.getSession().setAttribute(Constants.OAUTH_USER, visitor);
                                        return true;
                                    }
                                }
                            }
                        } catch (Exception e) {
                            logger.error("OAuth Login Fail:", e);
                        }
                        HttpResponseUtil.redirect(response, "/404");
                        return false;
                    }
                }
            }
        }
        return true;
    }
    //通过回调的code和state获取访问github的token
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
    //通过token获取github用户信息
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
