package com.shaw.service;

import com.shaw.bo.Blog;
import com.shaw.vo.BlogQuery;

import java.util.List;
import java.util.Map;


public interface BlogService {
    List<Blog> countList();

    List<Blog> list(BlogQuery query);

    Long getTotal(BlogQuery query);

    Blog findById(Integer id);

    Integer update(Blog blog);

    Blog getLastBlog(Integer id);

    Blog getNextBlog(Integer id);

    Integer add(Blog blog);

    Integer delete(Integer id);

    Integer getBlogByTypeId(Integer typeId);

    List<Blog> listSimple(BlogQuery query);

    Integer updateBatchForClickHit(List<Blog> list);

    Integer updateBatchForSummary(List<Blog> list);
}
