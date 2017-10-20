package com.shaw.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.shaw.bo.Link;
import com.shaw.service.LinkService;
import com.shaw.util.HttpResponseUtil;
import com.shaw.util.PageBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author imn5100
 */
@Controller
@RequestMapping("/admin/link")
public class LinkAdminController {

    @Resource
    private LinkService linkService;

    /**
     * 分页查询链接
     */
    @RequestMapping("/list")
    public String list(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "rows", required = false) String rows, HttpServletResponse response) throws Exception {
        PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
        Map<String, Object> map = new HashMap<String, Object>();
        List<Link> linkList = linkService.list(pageBean);
        Long total = linkService.getTotal();
        JSONObject result = new JSONObject();
        result.put("rows", linkList);
        result.put("total", total);
        HttpResponseUtil.writeJsonStr(response, result);
        return null;
    }

    /**
     * 添加或者修改链接
     */
    @RequestMapping("/save")
    public String save(Link link, HttpServletResponse response) throws Exception {
        // 操作的记录条数
        int resultTotal;
        if (link.getId() == null) {
            resultTotal = linkService.add(link);
        } else {
            resultTotal = linkService.update(link);
        }
        JSONObject result = new JSONObject();
        if (resultTotal > 0) {
            result.put("success", true);
        } else {
            result.put("success", false);
        }
        HttpResponseUtil.writeJsonStr(response, result);
        return null;
    }

    /**
     * 删除链接
     */
    @RequestMapping("/delete")
    public String delete(@RequestParam(value = "ids") String ids, HttpServletResponse response) throws Exception {
        String[] idsStr = ids.split(",");
        for (int i = 0; i < idsStr.length; i++) {
            linkService.delete(Integer.parseInt(idsStr[i]));
        }
        JSONObject result = new JSONObject();
        result.put("success", true);
        HttpResponseUtil.writeJsonStr(response, result);
        return null;
    }
}
