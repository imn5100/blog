package com.shaw.util;

import com.shaw.bo.Blog;
import com.shaw.constants.CacheKey;
import com.shaw.service.impl.RedisClient;

/**
 * Created by Administrator on 2016/10/28 0028.
 */
public class BoUtils {

    /**
     * 从缓存 获取点击数据 更新 blog, 返回值表示 blog是否改变
     */
    public static Boolean updateClickHit(Blog blog, RedisClient redisClient) {
        if (blog != null && redisClient != null) {
            Object clickHit = redisClient.get(String.format(CacheKey.BLOG_CLICK_KEY, blog.getId()));
            if (clickHit != null) {
                try {
                    blog.setClickHit(Integer.valueOf(clickHit.toString()));
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            return false;
        }
        return false;
    }
}
