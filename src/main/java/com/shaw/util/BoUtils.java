package com.shaw.util;

import com.shaw.bo.Blog;
import com.shaw.constants.CacheKey;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by Administrator on 2016/10/28 0028.
 */
public class BoUtils {

    /**
     * 从缓存 获取点击数据 更新 blog, 返回值表示 blog是否改变
     */
    public static Boolean setClickHitFromRedis(Blog blog, RedisTemplate<String, String> redisTemplate) {
        if (blog != null && redisTemplate != null) {
            String clickHit = redisTemplate.opsForValue().get(String.format(CacheKey.BLOG_CLICK_KEY, blog.getId()));
            if (StringUtils.isNotBlank(clickHit)) {
                try {
                    int intClickHit = Integer.valueOf(clickHit);
                    if (blog.getClickHit() == null || blog.getClickHit() < intClickHit) {
                        blog.setClickHit(intClickHit);
                        return true;
                    }
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            return false;
        }
        return false;
    }
}
