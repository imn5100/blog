package com.shaw.service.impl;

import com.shaw.bo.BlogType;
import com.shaw.mapper.BlogTypeMapper;
import com.shaw.service.BlogTypeService;
import com.shaw.util.PageBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class BlogTypeServiceImpl implements BlogTypeService {

    @Resource
    private BlogTypeMapper blogTypeMapper;

    public List<BlogType> countList() {
        return blogTypeMapper.countList();
    }

    public List<BlogType> list(PageBean pageBean) {
        return blogTypeMapper.list(pageBean);
    }

    public Long getTotal() {
        return blogTypeMapper.getTotal();
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
