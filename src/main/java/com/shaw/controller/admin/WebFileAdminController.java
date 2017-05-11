package com.shaw.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.shaw.bo.UploadFile;
import com.shaw.service.UploadFileService;
import com.shaw.util.HttpResponseUtil;
import com.shaw.util.StringUtil;
import com.shaw.util.qiniu.QiNiuUtils;
import com.shaw.vo.WebFileInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaw on 2016/11/15 0015.
 */
@Controller
@RequestMapping("/admin/webFile")
public class WebFileAdminController {
    @Autowired
    private UploadFileService uploadFileService;

    /**
     * 列出七牛文件 （从七牛http获取的数据）
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
        HttpResponseUtil.writeJsonStr(response, result);
        return null;
    }

    /**
     * 将文件上传至七牛服务器
     */
    @RequestMapping("/upload")
    public void upload(@RequestParam(value = "filename", required = false) String filename, @RequestParam("file") MultipartFile file, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        if (file != null && !file.isEmpty()) {
            if (StringUtil.isEmpty(filename)) {
                filename = file.getOriginalFilename();
            }
            UploadFile uploadFile = uploadFileService.uploadToQiniu(file, filename.trim());
            if (uploadFile != null) {
                result.put("success", true);
                result.put("url", uploadFile.getUrl());
                result.put("filename", filename);
                HttpResponseUtil.writeJsonStr(response, result);
            } else {
                result.put("success", false);
                HttpResponseUtil.writeJsonStr(response, result);
            }
        }
    }

    /**
     * 将文件上传至SMMS服务器
     */
    @RequestMapping("/SMMSUpload")
    public void uploadToSMMS(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        if (file != null && !file.isEmpty()) {
            UploadFile uploadFile = uploadFileService.uploadToSMMS(file);
            if (uploadFile != null) {
                result.put("success", true);
                result.put("uploadFile", uploadFile);
                HttpResponseUtil.writeJsonStr(response, result);
            } else {
                result.put("success", false);
                HttpResponseUtil.writeJsonStr(response, result);
            }
        }
    }

    /**
     * 删除七牛文件
     * SMMS 的文件直接请求delete url就能完成不需要接口
     */
    @RequestMapping("/delete")
    public void delete(@RequestParam(value = "ids") String ids, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        String[] idsStr = ids.split(",");
        if (idsStr.length > 0) {
            if (uploadFileService.batchDeleteQiniuFile(idsStr)) {
                result.put("success", true);
                HttpResponseUtil.writeJsonStr(response, result);
            } else {
                result.put("success", false);
                HttpResponseUtil.writeJsonStr(response, result);
            }
        } else {
            result.put("success", false);
            result.put("msg", "删除key传参错误");
            HttpResponseUtil.writeJsonStr(response, result);
        }
    }
}
