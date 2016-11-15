package com.shaw.util.qiniu;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.shaw.util.PropertiesUtil;
import com.shaw.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by shaw on 2016/11/15 0015.
 */
public class QiNiuUtils {
    private static final String ACCESS_KEY = PropertiesUtil.getConfiguration().getString("qiniu_key");
    private static final String SECRET_KEY = PropertiesUtil.getConfiguration().getString("qiniu_secret");
    private static final Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    private static final String bucketname = "images";
    private static Zone z = Zone.zone0();
    private static Configuration c = new Configuration(z);
    private static UploadManager uploadManager = new UploadManager(c);

    private static Logger logger = LoggerFactory.getLogger(QiNiuUtils.class);

    private static String getUpToken() {
        return auth.uploadToken(bucketname);
    }

    public static String upload(File file, String filename) throws IOException {
        try {
            String fileKey = TimeUtils.getMSTime();
            if (filename != null && filename.length() > 0 && !filename.trim().equals("")) {
                fileKey = fileKey + filename;
            }
            Response res = uploadManager.put(file, fileKey, getUpToken());
            JSONObject responseObj = JSONObject.parseObject(res.bodyString());
            logger.info("Upload File To Qiniu Success Response:" + res.bodyString());
            return responseObj.getString("key");
        } catch (QiniuException e) {
            Response r = e.response;
            logger.error("Upload File To Qiniu Fail Response:" + r.toString());
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println(upload(new File("E:/imageDownLoad/52992935_p0.jpg"), "bg3"));
    }
}
