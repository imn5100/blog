package com.shaw.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shaw.entity.Blogger;
import com.shaw.mapper.BloggerMapper;
import com.shaw.service.BloggerService;

/**
 * 博主Service实现类
 * 
 * @author Administrator
 *
 */
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
