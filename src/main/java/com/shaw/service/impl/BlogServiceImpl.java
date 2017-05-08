package com.shaw.service.impl;

import com.shaw.bo.Blog;
import com.shaw.mapper.BlogMapper;
import com.shaw.service.BlogService;
import com.shaw.util.BoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogMapper blogMapper;

    @Resource(name = "stringRedisTemplate")
    private RedisTemplate<String, String> stringRedisTemplate;

    @Override
    public List<Blog> countList() {
        return blogMapper.countList();
    }

    @Override
    public List<Blog> list(Map<String, Object> map) {
        return blogMapper.list(map);
    }

    @Override
    public Long getTotal(Map<String, Object> map) {
        return blogMapper.getTotal(map);
    }

    /***
     * 关于点击数获取，只在需要点击数的时候才从 redis 获取，list等操作不需要点击数（可能以后需要）
     */
    @Override
    public Blog findById(Integer id) {
        Blog blog = blogMapper.findById(id);
        BoUtils.setClickHitFromRedis(blog, stringRedisTemplate);
        return blog;
    }

    @Override
    public Integer update(Blog blog) {
        //每次更新blog的时候将 blog点击量更新到 数据库
        BoUtils.setClickHitFromRedis(blog, stringRedisTemplate);
        return blogMapper.update(blog);
    }

    @Override
    public Blog getLastBlog(Integer id) {
        return blogMapper.getLastBlog(id);
    }

    @Override
    public Blog getNextBlog(Integer id) {
        return blogMapper.getNextBlog(id);
    }

    @Override
    public Integer add(Blog blog) {
        return blogMapper.add(blog);
    }

    @Override
    public Integer delete(Integer id) {
        return blogMapper.delete(id);
    }

    @Override
    public Integer getBlogByTypeId(Integer typeId) {
        return blogMapper.getBlogByTypeId(typeId);
    }

    @Override
    public List<Blog> listSimple(Map<String, Object> map) {
        return blogMapper.listSimple(map);
    }

    @Override
    public Integer updateBatchForClickHit(List<Blog> list) {
        return blogMapper.updateBatchForClickHit(list);
    }

    @Override
    public Integer updateBatchForSummary(List<Blog> list) {
        return blogMapper.updateBatchForSummary(list);
    }


}
