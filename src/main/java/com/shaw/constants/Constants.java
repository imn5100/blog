package com.shaw.constants;

import com.shaw.util.PropertiesUtil;

public class Constants {
    public static final Integer PAGE_SIZE = 12;
    public static final String PAGE_TITLE = "ShawのSecret Base";
    public static final String LOG_EXCEPTION = "LogException";
    public static final String ROOT_SITE_KEY = "root_site";
    public static final String QINIU_URL_KEY = "qiniu_url";
    public static final String QINIU_KEY_KEY = "qiniu_key";
    public static final String QINIU_SERCET_KEY = "qiniu_secret";
    public static final String QINIU_BUCKET_NAME_KEY = "qiniu_bucketname";
    public static final String SMMS_UPLOAD_URL_KEY = "smms_url_upload";
    public static final String DEFAULT_SITE = "https://shawblog.me";
    public static final String DEFAULT_WEB_LOGS_PATH = "/usr/myfile/logs/html/";
    public static final String QINIU_BASE_URL = PropertiesUtil.getConfiguration().getString(Constants.QINIU_URL_KEY);
    public static final Integer HTTP_READ_TIMEOUT = 60 * 1000;
    public static final Integer HTTP_CONNECT_TIMEOUT = 10 * 1000;
    public static final Short MSG_REUSE = 1;
    public static final Short MSG_START = 2;
    public static final Short MSG_OVER = 3;
    public static final Short MSG_FAIL = 4;
    public static final Integer SESSION_TIME = 1800;
}
