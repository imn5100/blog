package com.shaw.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.shaw.bo.UploadFile;
import com.shaw.service.UploadFileService;
import com.shaw.util.HttpResponseUtil;
import com.shaw.util.StringUtil;
import com.shaw.vo.WebFileQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by shaw on 2016/11/17 0017.
 */
@Controller
@RequestMapping("/admin/uploadFile")
public class UploadFileAdminController {
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
        HttpResponseUtil.writeJsonStr(response, result);
    }

    /**
     * 外链文件管理 七牛 SMMS.更新信息
     */
    @RequestMapping("/setValid")
    public void setValid(String ids, Integer valid, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        List<Integer> idList = StringUtil.parseToIntList(ids);
        if (idList.size() == 0) {
            result.put("success", true);
            HttpResponseUtil.writeJsonStr(response, result);
            return;
        } else {
            Integer count = uploadFileService.updateValid(idList, (valid == null || valid != 2) ? false : true);
            result.put("count", count);
            result.put("success", true);
            HttpResponseUtil.writeJsonStr(response, result);
        }
    }

    @RequestMapping("/batchDelete")
    public void batchDelete(String ids, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        List<Integer> idList = StringUtil.parseToIntList(ids);
        if (idList.size() == 0) {
            result.put("success", true);
            HttpResponseUtil.writeJsonStr(response, result);
            return;
        } else {
            Integer count = uploadFileService.batchDelete(idList);
            result.put("count", count);
            result.put("success", true);
            HttpResponseUtil.writeJsonStr(response, result);
        }
    }

}
