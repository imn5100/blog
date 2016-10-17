package com.shaw.controller;

import com.shaw.constants.CacheKey;
import com.shaw.entity.Comment;
import com.shaw.service.CommentService;
import com.shaw.service.impl.RedisClient;
import com.shaw.util.ResponseUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private RedisClient redisClient;

    /**
     * 添加或者修改评论
     *
     * @param comment
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/save")
    public void save(Comment comment, @RequestParam("imageCode") String imageCode, HttpServletRequest request,
                     HttpServletResponse response, HttpSession session) throws Exception {
        String key = String.format(CacheKey.CODES_KEY, session.getId());
        String vcode = (String) redisClient.get(key);
        JSONObject result = new JSONObject();
        int resultTotal = 0;
        if (StringUtils.isBlank(imageCode) || !imageCode.equalsIgnoreCase(vcode)) {
            result.put("success", false);
            result.put("errorInfo", "验证码填写错误！");
        } else {
            redisClient.del(key);
            //nginx 代理下 将用户ID 设置在  x-real-ip中 否者获取的ip都是本地ip
            String userIp = request.getHeader("X-Real-IP");
//            String userIp = request.getRemoteAddr(); // 获取用户IP
            comment.setUserIp(userIp);
            if (comment.getId() == null) {
                resultTotal = commentService.add(comment);
            } else {

            }
            if (resultTotal > 0) {
                result.put("success", true);
            } else {
                result.put("success", false);
            }
        }
        ResponseUtil.write(response, result);
    }

}
