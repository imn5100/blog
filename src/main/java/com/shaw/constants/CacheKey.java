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
    public static final Long SEARCH_EXPIRETIME = 30L;
}
