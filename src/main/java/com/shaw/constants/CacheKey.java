package com.shaw.constants;

public class CacheKey {
    /**
     * 服务器初始化时的 存入全局上下文，application中
     * 管理员可以通过刷新系统缓存接口刷新
     */
    public static final String BLOGGER = "blogger";
    public static final String BLOG_TYPE_LIST = "blogTypeCountList";
    public static final String BLOG_COUNT_LIST = "blogCountList";
    public static final String LINK_LIST = "linkList";

    /***
     * 验证码key %s 用sessionId代替
     */
    public static final String CODES_KEY = "codes_%s";
    /**
     * 验证码过期时间3min
     */
    public static final Long CODES_EXPIRE = 60 * 3L;
    /**
     * 博客点击量key
     **/
    public static final String BLOG_CLICK_KEY = "ClickCount:BlogClick_%s";
    /****
     * 搜索缓存过期时间 秒，避免没频繁请求搜索导致服务器崩溃。目前设置为30秒内所有请求都是走缓存，30秒后走lucence
     * BLOG内容更新不频繁下，设置1小时的缓存都可以。
     */
    public static final Long SEARCH_EXPIRETIME = 60 * 5L;
    /**
     * 缓存web日志文件分析html key
     **/
    public static final String WEB_LOGS_HTML_KEY = "WebLogs:FileName_%s";
    /**
     * 缓存3天
     */
    public static final Long WEB_LOGS_HTML_EXPIRE = 60 * 60 * 24 * 3L;
    /**
     * web日志分析html 文件列表,避免频繁IO读取 磁盘
     */
    public static final String WEB_LOGS_NAME_LIST_KEY = "WebLogs:FileNameList";
    /**
     * 1小时失效 ==  最多每小时查看日志文件读取记录
     **/
    public static final Long WEB_LOGS_NAME_LIST_EXPIRE = 60 * 60L;

    /**
     * 远程任务消息白名单缓存
     */
    public static final String WHITE_LIST_IP = "remoteMsg:White_list";

    /**
     * 刷新系统缓存时间戳
     */
    public static final String SYSTEM_REFRESH_TIME = "system:refresh_time";
    public static final Long SYSTEM_REFRESH_TIME_EXPIRE = 60 * 60 * 2L;

    /**
     * 任务用户登录 key
     */
    public static final String TASK_USER_AUTH = "task_user";

    /**
     * 首页背景高度缓存key
     */
    public static final String BACKGROUND_ASPECT_RATIO = "system:background_aspect_ratio";


}
