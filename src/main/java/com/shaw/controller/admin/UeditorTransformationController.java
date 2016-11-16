//package com.shaw.controller.admin;
//
//import com.baidu.ueditor.ActionEnter;
//import com.shaw.util.qiniu.QiNiuUtils;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartHttpServletRequest;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by shaw on 2016/11/15 0015.
// */
//@Controller
//@RequestMapping("/admin/ueditor")
//public class UeditorTransformationController {
//
//    @RequestMapping(value = "/config")
//    public void config(HttpServletRequest request, HttpServletResponse response) throws Exception{
//        response.setContentType("application/json");
//        String rootPath = request.getSession()
//                .getServletContext().getRealPath("/");
//        try {
//            String exec = new ActionEnter(request, rootPath).exec();
//            PrintWriter writer = response.getWriter();
//            writer.write(exec);
//            writer.flush();
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @ResponseBody
//    @RequestMapping(value = "uploadfile")
//    public Map uploadFile(@RequestParam(value = "upfile", required = false) MultipartFile[] upfile,HttpServletRequest request ) throws Exception {
//        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
//        multipartRequest.getFiles("upfile");
//        Map<String, String> map = new HashMap<String, String>();
//        if (upfile != null && upfile.length > 0) {
//            //循环获取file数组中得文件
//            for (int i = 0; i < upfile.length; i++) {
//                MultipartFile file = upfile[i];
//                String fileName = file.getOriginalFilename();
//                byte[] fileByte = file.getBytes();
//                try {
//                    QiNiuUtils.upload(fileByte, fileName);
//                    map.put("url", fileName);
//                    map.put("name", fileName);
//                    map.put("state", "SUCCESS");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    map.put("state", "上传失败!");
//                }
//            }
//        }
//        return map;
//    }
//}
