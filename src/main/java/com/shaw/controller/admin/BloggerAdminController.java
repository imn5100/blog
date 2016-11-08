package com.shaw.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.shaw.bo.Blogger;
import com.shaw.service.BloggerService;
import com.shaw.util.HttpResponseUtil;
import com.shaw.util.TimeUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

@Controller
@RequestMapping("/admin/blogger")
public class BloggerAdminController {

    @Resource
    private BloggerService bloggerService;
    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /**
     * 修改博主信息
     */
    @RequestMapping("/save")
    public String save(@RequestParam("imageFile") MultipartFile imageFile, Blogger blogger, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!imageFile.isEmpty()) {
            String filePath = request.getServletContext().getRealPath("/");
            String imageName = TimeUtils.getCurrentDateStr() + "." + imageFile.getOriginalFilename().split("\\.")[1];
            imageFile.transferTo(new File(filePath + "/static/userImages/" + imageName));
            blogger.setImageName(imageName);
        }
        int resultTotal = bloggerService.update(blogger);
        JSONObject result = new JSONObject();
        if (resultTotal > 0) {
            result.put("success", true);
        } else {
            result.put("success", false);
        }
        logger.info("update blogger success");
        HttpResponseUtil.write(response, result);
        return null;
    }

    /**
     * 查询博主信息
     */
    @RequestMapping("/find")
    public String find(HttpServletResponse response) throws Exception {
        Blogger blogger = bloggerService.find();
        HttpResponseUtil.write(response, JSONObject.toJSONString(blogger));
        return null;
    }

    /**
     * 修改博主密码
     */
    @RequestMapping("/modifyPassword")
    public String modifyPassword(String newPassword, HttpServletResponse response) throws Exception {
        Blogger blogger = new Blogger();
        blogger.setPassword(newPassword);
        int resultTotal = bloggerService.update(blogger);
        JSONObject result = new JSONObject();
        if (resultTotal > 0) {
            result.put("success", true);
        } else {
            result.put("success", false);
        }
        logger.info("modify password success,md5 newPassword:" + newPassword);
        HttpResponseUtil.write(response, result);
        return null;
    }

    /**
     * 注销
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/logout")
    public String logout() throws Exception {
        SecurityUtils.getSubject().logout();
        logger.info("blogger logout");
        return "redirect:/";
    }
}
