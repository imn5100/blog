package com.shaw.constants;

import com.shaw.util.PropertiesUtil;

public class Constants {
    public static final Integer PAGE_SIZE = 7;
    public static final String PAGE_TITLE = "ShawのSecret Base";
    public static final String LOG_EXCEPTION = "LogException";
    public static final String ROOT_SITE_KEY = "root_site";
    public static final String QINIU_URL_KEY = "qiniu_url";
    public static final String QINIU_KEY_KEY = "qiniu_key";
    public static final String QINIU_SERCET_KEY = "qiniu_secret";
    public static final String QINIU_BUCKET_NAME_KEY = "qiniu_bucketname";
    public static final String DEFAULT_SITE = "http://shawblog.me";
    public static final String DEFAULT_WEB_LOGS_PATH = "/usr/myfile/logs/html/";
    public static final String QINIU_BASE_URL = PropertiesUtil.getConfiguration().getString(Constants.QINIU_URL_KEY);
}
