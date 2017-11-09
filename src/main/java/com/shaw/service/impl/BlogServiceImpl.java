package com.shaw.service.impl;

import com.shaw.annotation.DeleteCache;
import com.shaw.annotation.SetCache;
import com.shaw.aop.CacheKeyType;
import com.shaw.bo.Blog;
import com.shaw.mapper.BlogMapper;
import com.shaw.service.BlogService;
import com.shaw.util.BoUtils;
import com.shaw.vo.BlogQuery;
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
    public List<Blog> list(BlogQuery query) {
        return blogMapper.list(query);
    }

    @Override
    public Long getTotal(BlogQuery query) {
        return blogMapper.getTotal(query);
    }

    /***
     * 关于点击数获取，只在需要点击数的时候才从 redis 获取，list等操作不需要点击数（可能以后需要）
     */
    @Override
    @SetCache(keyType = CacheKeyType.SpEl, key = "'blog_'+#arg0")
    public Blog findById(Integer id) {
        Blog blog = blogMapper.findById(id);
        BoUtils.setClickHitFromRedis(blog, stringRedisTemplate);
        return blog;
    }

    @Override
    @DeleteCache(keyType = CacheKeyType.SpEl, key = "'blog_'+#arg0.getId()")
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
    @DeleteCache(keyType = CacheKeyType.SpEl, key = "'blog_'+#arg0")
    public Integer delete(Integer id) {
        return blogMapper.delete(id);
    }

    @Override
    public Integer getBlogByTypeId(Integer typeId) {
        return blogMapper.getBlogByTypeId(typeId);
    }

    @Override
    public List<Blog> listSimple(BlogQuery query) {
        return blogMapper.listSimple(query);
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
