package com.shaw.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.shaw.bo.BlogType;
import com.shaw.bo.UploadFile;
import com.shaw.service.UploadFileService;
import com.shaw.util.HttpResponseUtil;
import com.shaw.util.PageBean;
import com.shaw.util.StringUtil;
import com.shaw.util.qiniu.QiNiuUtils;
import com.shaw.vo.WebFileInfoVo;
import com.shaw.vo.WebFileQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shaw on 2016/11/15 0015.
 */
@Controller
@RequestMapping("/admin/webFile")
public class WebFileAdminController {
    @Autowired
    private UploadFileService uploadFileService;

    /**
     * 外链文件管理 七牛 SMMS
     */
    @RequestMapping("/listWebFile")
    public void listWebFile(WebFileQuery query, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        if (query != null) {
            if (StringUtil.isEmpty(query.getFilename())) {
                query.setFilename(null);
            }
            if (StringUtil.isEmpty(query.getMimetype())) {
                query.setMimetype(null);
            }
            List<UploadFile> list = uploadFileService.queryList(query);
            Integer count = uploadFileService.countList(query);
            result.put("success", true);
            result.put("rows", list);
            result.put("total", count);
        }
        HttpResponseUtil.write(response, result);
    }

    /**
     * 外链文件管理 七牛 SMMS.更新信息
     */
    @RequestMapping("/updateWebFile")
    public void update(WebFileQuery query, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        if (query != null) {
            List<UploadFile> list = uploadFileService.queryList(query);
            Integer count = uploadFileService.countList(query);
            result.put("success", true);
            result.put("rows", list);
            result.put("total", count);
        }
        HttpResponseUtil.write(response, result);
    }


    /**
     * 列出七牛文件
     * easyUI 格式
     */
    @RequestMapping("/list")
    public String list(@RequestParam(value = "prefix", required = false) String prefix, @RequestParam(value = "marker", required = false, defaultValue = "") String marker, HttpServletResponse response) throws Exception {
        QiNiuUtils.QiniuFileQuery qiniuFileQuery = new QiNiuUtils.QiniuFileQuery();
        JSONObject result = new JSONObject();
        if (StringUtil.isNotEmpty(marker)) {
            qiniuFileQuery.setMarker(marker);
        }
        if (StringUtil.isNotEmpty(prefix)) {
            qiniuFileQuery.setPrefix(prefix);
        }
        List<WebFileInfoVo> list = uploadFileService.listQiniuWebFile(qiniuFileQuery);
        if (list == null) {
            list = new ArrayList<>();
        } else {
            if (list.size() > 0)
                result.put("marker", list.get(list.size() - 1).getKey());
        }
        result.put("rows", list);
        result.put("total", list.size());
        HttpResponseUtil.write(response, result);
        return null;
    }

    @RequestMapping("/upload")
    public void upload(@RequestParam(value = "filename", required = false) String filename, @RequestParam("file") MultipartFile file, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        if (file != null && !file.isEmpty()) {
            if (StringUtil.isEmpty(filename)) {
                filename = StringUtil.getFileName(file.getOriginalFilename()).toLowerCase();
            }
            UploadFile uploadFile = uploadFileService.uploadToQiniu(file, filename.trim());
            if (uploadFile != null) {
                result.put("success", true);
                result.put("url", uploadFile.getUrl());
                result.put("filename", filename);
                HttpResponseUtil.write(response, result);
            } else {
                result.put("success", false);
                HttpResponseUtil.write(response, result);
            }
        }
    }

    @RequestMapping("/SMMSUpload")
    public void uploadToSMMS(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        if (file != null && !file.isEmpty()) {
            UploadFile uploadFile = uploadFileService.uploadToSMMS(file);
            if (uploadFile != null) {
                result.put("success", true);
                result.put("uploadFile", uploadFile);
                HttpResponseUtil.write(response, result);
            } else {
                result.put("success", false);
                HttpResponseUtil.write(response, result);
            }
        }
    }

    @RequestMapping("/delete")
    public void delete(@RequestParam(value = "ids") String ids, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        String[] idsStr = ids.split(",");
        if (idsStr.length > 0) {
            if (uploadFileService.batchDeleteQiniuFile(idsStr)) {
                result.put("success", true);
                HttpResponseUtil.write(response, result);
            } else {
                result.put("success", false);
                HttpResponseUtil.write(response, result);
            }
        } else {
            result.put("success", false);
            result.put("msg", "删除key传参错误");
            HttpResponseUtil.write(response, result);
        }
    }
}
