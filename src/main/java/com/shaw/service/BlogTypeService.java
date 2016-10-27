package com.shaw.service;

import com.shaw.bo.BlogType;

import java.util.List;
import java.util.Map;


public interface BlogTypeService {

    List<BlogType> countList();

    List<BlogType> list(Map<String, Object> map);

    Long getTotal(Map<String, Object> map);

    Integer add(BlogType blogType);

    Integer update(BlogType blogType);

    Integer delete(Integer id);
}
