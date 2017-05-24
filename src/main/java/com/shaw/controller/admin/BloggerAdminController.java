package com.shaw.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.shaw.bo.Blogger;
import com.shaw.bo.UploadFile;
import com.shaw.service.BloggerService;
import com.shaw.service.UploadFileService;
import com.shaw.util.HttpResponseUtil;
import com.shaw.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/admin/blogger")
public class BloggerAdminController {

    @Resource
    private BloggerService bloggerService;
    @Autowired
    private UploadFileService uploadFileService;
    private Logger logger = LoggerFactory.getLogger(BloggerAdminController.class);

    /**
     * 修改博主信息
     */
    @RequestMapping("/save")
    public String save(@RequestParam("imageFile") MultipartFile imageFile, @RequestParam("imageUrl") String imageUrl, @RequestParam("backgroundUrl") String backgroundUrl, Blogger blogger, HttpServletResponse response) throws Exception {
        if (imageFile != null && !imageFile.isEmpty()) {
            //将头像上传至 七牛，并获取返回的url
//            UploadFile uploadFile =  uploadFileService.uploadToQiniu(imageFile, StringUtil.getFileName(imageFile.getOriginalFilename()));
            //将头像上传至smms 图库，返回URL
            UploadFile uploadFile = uploadFileService.uploadToSMMS(imageFile);
            blogger.setImageName(uploadFile.getUrl());
        } else {
            if (StringUtil.isNotEmpty(imageUrl)) {
                blogger.setImageName(imageUrl.trim());
            }
        }
        if (backgroundUrl!=null) {
            if (imageUrl.startsWith("https://"))
                blogger.setBackgroundImage(backgroundUrl);
            else
                blogger.setBackgroundImage("");
        }
        int resultTotal = bloggerService.update(blogger);
        JSONObject result = new JSONObject();
        if (resultTotal > 0) {
            result.put("imageUrl", blogger.getImageName());
            result.put("success", true);
        } else {
            result.put("success", false);
        }
        logger.info("update blogger success");
        HttpResponseUtil.writeJsonStr(response, result);
        return null;
    }

    /**
     * 查询博主信息
     */
    @RequestMapping("/find")
    public String find(HttpServletResponse response) throws Exception {
        Blogger blogger = bloggerService.find();
        HttpResponseUtil.writeJsonStr(response, JSONObject.toJSONString(blogger));
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
        HttpResponseUtil.writeJsonStr(response, result);
        return null;
    }

    /**
     * 注销
     */
    @RequestMapping("/logout")
    public String logout() throws Exception {
        SecurityUtils.getSubject().logout();
        logger.info("blogger logout");
        return "redirect:/";
    }
}
