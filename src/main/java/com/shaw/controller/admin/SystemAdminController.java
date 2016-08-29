package com.shaw.controller.admin;

import com.shaw.service.SystemService;
import com.shaw.util.ResponseUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/admin/system")
public class SystemAdminController {

    @Autowired
    private SystemService systemService;

    /**
     * 刷新系统缓存
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/refreshSystem")
    public String refreshSystem(HttpServletResponse response, HttpServletRequest request) throws Exception {
        ServletContext application = RequestContextUtils.getWebApplicationContext(request).getServletContext();
        systemService.initBlogData(application);
        JSONObject result = new JSONObject();
        result.put("success", true);
        ResponseUtil.write(response, result);
        return null;
    }
}
