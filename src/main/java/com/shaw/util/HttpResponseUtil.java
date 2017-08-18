package com.shaw.util;

import com.alibaba.fastjson.JSONObject;
import com.shaw.constants.ResponseCode;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


public class HttpResponseUtil {
    /***
     * 直接写入字符串或json对象
     */
    public static void writeJsonStr(HttpServletResponse response, Object o) throws Exception {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(o.toString());
        out.flush();
        out.close();
    }

    /***
     * 写入code和msg
     * 一般用于错误提示
     */
    public static void writeCode(HttpServletResponse response, ResponseCode code) throws Exception {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code.getCode());
        jsonObject.put("msg", code.getMsg());
        if (code == ResponseCode.SUCCESS) {
            jsonObject.put("success", true);
        }
        out.print(jsonObject.toString());
        out.flush();
        out.close();
    }

    /**
     * 写入数据Json，并返回code
     */
    public static void writeUseData(HttpServletResponse response, Object o, ResponseCode code) throws Exception {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        if (o != null) {
            jsonObject.put("data", o);
        }
        if (code == ResponseCode.SUCCESS) {
            jsonObject.put("success", true);
        }
        jsonObject.put("code", code.getCode());
        jsonObject.put("msg", code.getMsg());
        out.print(jsonObject.toString());
        out.flush();
        out.close();
    }

    public static void redirect(HttpServletResponse response, String target) {
        try {

            response.sendRedirect(StringUtil.emptyToDefault(target, "/"));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
