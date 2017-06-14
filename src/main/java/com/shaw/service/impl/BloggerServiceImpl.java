package com.shaw.service.impl;

import com.shaw.bo.Blogger;
import com.shaw.constants.CacheKey;
import com.shaw.mapper.BloggerMapper;
import com.shaw.service.BloggerService;
import com.shaw.util.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BloggerServiceImpl implements BloggerService {
    @Autowired
    private BloggerMapper bloggerMapper;
    @Autowired
    private RedisClient redisClient;

    public Blogger find() {
        Blogger blogger = bloggerMapper.find();
        if (blogger != null) {
            Double aspectRatio = (Double) redisClient.get(CacheKey.BACKGROUND_ASPECT_RATIO);
            if (aspectRatio != null && aspectRatio > 0) {
                blogger.setAspectRatio(aspectRatio);
            }
        }
        return blogger;
    }

    public Blogger getByUserName(String userName) {
        return bloggerMapper.getByUserName(userName);
    }

    public Integer update(Blogger blogger) {
        return bloggerMapper.update(blogger);
    }

}
