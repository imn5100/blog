package com.shaw.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.storage.model.FileInfo;
import com.shaw.WebFileInfoVo;
import com.shaw.constants.Constants;
import com.shaw.util.HttpResponseUtil;
import com.shaw.util.StringUtil;
import com.shaw.util.qiniu.QiNiuUtils;
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
    /**
     * 分页查询博客类别信息
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
        List<FileInfo> fileInfoList = QiNiuUtils.listFile(qiniuFileQuery);
        List<WebFileInfoVo> list = WebFileInfoVo.convertList(fileInfoList);
        if (list == null) {
            list = new ArrayList<>();
        } else {
            if (fileInfoList.size() > 0)
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
                filename = file.getOriginalFilename().split("\\.")[1];
            }
            String key = QiNiuUtils.upload(file.getBytes(), StringUtil.filterSpChar(filename));
            if (StringUtil.isNotEmpty(key)) {
                String fileUrl = Constants.QINIU_BASE_URL + key;
                result.put("success", true);
                result.put("url", fileUrl);
                result.put("filename", filename);
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
            if (QiNiuUtils.batchDelete(null, idsStr)) {
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
