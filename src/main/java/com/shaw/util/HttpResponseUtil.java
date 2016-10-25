package com.shaw.util;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


public class HttpResponseUtil {

    public static void write(HttpServletResponse response, Object o) throws Exception {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(o.toString());
        out.flush();
        out.close();
    }
}
