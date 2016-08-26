package com.shaw.service.impl;

import com.shaw.entity.BlogType;
import com.shaw.mapper.BlogTypeMapper;
import com.shaw.service.BlogTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 博客类型Service实现类
 * @author Administrator
 *
 */
@Service("blogTypeService")
public class BlogTypeServiceImpl implements BlogTypeService{

	@Resource
	private BlogTypeMapper blogTypeMapper;
	
	public List<BlogType> countList() {
		return blogTypeMapper.countList();
	}

	public List<BlogType> list(Map<String, Object> map) {
		return blogTypeMapper.list(map);
	}

	public Long getTotal(Map<String, Object> map) {
		return blogTypeMapper.getTotal(map);
	}

	public Integer add(BlogType blogType) {
		return blogTypeMapper.add(blogType);
	}

	public Integer update(BlogType blogType) {
		return blogTypeMapper.update(blogType);
	}

	public Integer delete(Integer id) {
		return blogTypeMapper.delete(id);
	}

}
