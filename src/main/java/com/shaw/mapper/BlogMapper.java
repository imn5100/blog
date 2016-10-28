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

    /***
     * 优化 getLastBlog 和 getNextBlog ，使用时只需要id和 title 所以只取 id 和title 其他字段为空
     */
    Blog getLastBlog(Integer id);

    Blog getNextBlog(Integer id);


    Integer add(Blog blog);

    Integer delete(Integer id);

    Integer getBlogByTypeId(Integer typeId);

    List<Blog> listSimple(Map<String, Object> map);

    /**
     * 批量更新操作，clickHit
     **/
    Integer updateBatchForClickHit(List<Blog> list);

    /**
     * 批量更新 summary
     */
    Integer updateBatchForSummary(List<Blog> list);
}
