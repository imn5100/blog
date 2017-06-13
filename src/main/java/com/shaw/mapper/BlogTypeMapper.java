package com.shaw.mapper;

import com.shaw.bo.BlogType;
import com.shaw.util.PageBean;

import java.util.List;
import java.util.Map;

public interface BlogTypeMapper {

    List<BlogType> countList();

    BlogType findById(Integer id);

    List<BlogType> list(PageBean pageBean);

    Long getTotal();

    Integer add(BlogType blogType);

    Integer update(BlogType blogType);

    Integer delete(Integer id);
}
