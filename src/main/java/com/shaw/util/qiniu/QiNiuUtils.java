package com.shaw.util.qiniu;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;
import com.qiniu.util.Auth;
import com.shaw.constants.Constants;
import com.shaw.util.PropertiesUtil;
import com.shaw.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by shaw on 2016/11/15 0015.
 */
public class QiNiuUtils {
    private static final String ACCESS_KEY = PropertiesUtil.getConfiguration().getString(Constants.QINIU_KEY_KEY);
    private static final String SECRET_KEY = PropertiesUtil.getConfiguration().getString(Constants.QINIU_SERCET_KEY);
    private static final Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    private static final String BUCKET_NAME = PropertiesUtil.getConfiguration().getString(Constants.QINIU_BUCKET_NAME_KEY);
    private static Zone z = Zone.zone0();
    private static Configuration c = new Configuration(z);
    private static Logger logger = LoggerFactory.getLogger(QiNiuUtils.class);

    private static UploadManager uploadManager = new UploadManager(c);
    private static BucketManager bucketManager = new BucketManager(auth, c);
    private static BucketManager.Batch operations = new BucketManager.Batch();

    private static String getUpToken() {
        return auth.uploadToken(BUCKET_NAME);
    }

    /**
     * 用于处理 Ueditor中 StorageManager . saveTmpFile方法。将本地保存文件转化为保持至七牛
     */
    public static String uploadForUeditor(File file, String ueditorSavePath) throws IOException {
        // 获取文件名,文件名在config.json中配置
        int start = ueditorSavePath.lastIndexOf("/");
        //文件名+后缀
        String filename = ueditorSavePath.substring(start + 1);
        //这里需要处理一下访问路径，图片为前缀 + 路径(webapps开始) +文件名访问。保证存储和访问url相同
        logger.info("Ueditor Upload file to qiniu filename:" + file.getName());
        QiNiuUtils.upload(file, "static/userImages/" + filename);
        return null;
    }

    public static String upload(File file, String filename) throws IOException {
        try {
            Response res = uploadManager.put(file, filename, getUpToken());
            String bodyString = res == null ? null : res.bodyString();
            logger.info("Upload File To Qiniu Success Response:" + bodyString);
            return bodyString;
        } catch (QiniuException e) {
            Response r = e.response;
            logger.error("Upload File To Qiniu Fail Response:" + r.toString());
            return null;
        }
    }

    public static String upload(byte[] file, String filename) throws IOException {
        try {
            String fileKey = String.valueOf(System.currentTimeMillis()) + StringUtil.randomNumbers(3);
            //以后缀的形式 设置key ，七牛可以通过前缀 查询文件
            if (filename != null && filename.length() > 0 && !filename.trim().equals("")) {
                fileKey = fileKey + filename;
            }
            Response res = uploadManager.put(file, fileKey, getUpToken());
            String bodyString = res == null ? null : res.bodyString();
            logger.info("Upload File To Qiniu Success Response:" + bodyString);
            return bodyString;
        } catch (QiniuException e) {
            Response r = e.response;
            logger.error("Upload File To Qiniu Fail Response:" + r.toString());
            return null;
        }
    }

    public static FileInfo getQiniuFileInfo(String key, String bucket) {
        if (bucket == null) {
            bucket = QiNiuUtils.BUCKET_NAME;
        }
        try {
            FileInfo info = bucketManager.stat(bucket, key);
            return info;
        } catch (QiniuException e) {
            logger.error("Get Qiniu file  Fail Response:" + e.response);
            return null;
        }
    }

    public static boolean deleteFile(String key, String bucket) {
        if (bucket == null) {
            bucket = QiNiuUtils.BUCKET_NAME;
        }
        try {
            bucketManager.delete(bucket, key);
            return true;
        } catch (QiniuException e) {
            logger.error("delete Qiniu file  Fail Response:" + e.response);
            return false;
        }
    }

    public static List<FileInfo> listFile(QiniuFileQuery query) {
        try {
            FileListing fileListing = bucketManager.listFiles(query.getBucket(), query.getPrefix(), query.getMarker(), query.getLimit(), query.getDelimiter());
            if (fileListing != null && fileListing.items.length > 0) {
                return Arrays.asList(fileListing.items);
            }
            return null;
        } catch (QiniuException e) {
            logger.error("List Qiniu file  Fail Response:" + e.response);
            return null;
        }
    }

    public static boolean batchDelete(String bucket, String... keys) {
        if (StringUtil.isEmpty(bucket)) {
            bucket = BUCKET_NAME;
        }
        try {
            Response response = bucketManager.batch(operations.delete(bucket, keys));
            logger.info("batch delete file keys:" + keys + " response:" + response.bodyString());
            return true;
        } catch (QiniuException e) {
            logger.error("List Qiniu file  Fail Response:" + e.response);
            return false;
        }
    }

    public static class QiniuFileQuery {
        private String bucket = QiNiuUtils.BUCKET_NAME;
        private String prefix = null;
        private String marker = null;
        private Integer limit = 10;
        private String delimiter = null;

        public String getBucket() {
            return bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getMarker() {
            return marker;
        }

        public void setMarker(String marker) {
            this.marker = marker;
        }

        public Integer getLimit() {
            return limit;
        }

        public void setLimit(Integer limit) {
            this.limit = limit;
        }

        public String getDelimiter() {
            return delimiter;
        }

        public void setDelimiter(String delimiter) {
            this.delimiter = delimiter;
        }
    }
}
