package com.shaw.mapper;

import com.shaw.bo.Blog;

import java.util.List;
import java.util.Map;

public interface BlogMapper {

    List<Blog> countList();

    List<Blog> list(Map<String, Object> map);

    Long getTotal(Map<String, Object> map);

    Blog findById(Integer id);

    Integer update(Blog blog);

    Blog getLastBlog(Integer id);

    Blog getNextBlog(Integer id);

    Integer add(Blog blog);

    Integer delete(Integer id);

    Integer getBlogByTypeId(Integer typeId);

    List<Blog> listSimple(Map<String, Object> map);

    Integer updateBatchForClickHit(List<Blog> list);

    Integer updateBatchForSummary(List<Blog> list);
}
