package com.shaw.service.impl;

import com.shaw.bo.Blogger;
import com.shaw.mapper.BloggerMapper;
import com.shaw.service.BloggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bloggerService")
public class BloggerServiceImpl implements BloggerService {
    @Autowired
    private BloggerMapper bloggerMapper;

    public Blogger find() {
        return bloggerMapper.find();
    }

    public Blogger getByUserName(String userName) {
        return bloggerMapper.getByUserName(userName);
    }

    public Integer update(Blogger blogger) {
        return bloggerMapper.update(blogger);
    }

}
