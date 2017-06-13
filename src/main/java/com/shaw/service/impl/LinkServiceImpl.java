package com.shaw.service.impl;

import com.shaw.bo.Link;
import com.shaw.mapper.LinkMapper;
import com.shaw.service.LinkService;
import com.shaw.util.PageBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class LinkServiceImpl implements LinkService{

	@Resource
	private LinkMapper linkMapper;
	
	public int add(Link link) {
		return linkMapper.add(link);
	}

	public int update(Link link) {
		return linkMapper.update(link);
	}

	public List<Link> list(PageBean pageBean) {
		return linkMapper.list(pageBean);
	}

	public Long getTotal() {
		return linkMapper.getTotal();
	}

	public Integer delete(Integer id) {
		return linkMapper.delete(id);
	}

}
