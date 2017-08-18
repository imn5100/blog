package com.shaw.service.impl;

import com.shaw.bo.Discuss;
import com.shaw.mapper.DiscussMapper;
import com.shaw.service.DiscussService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussServiceImpl implements DiscussService {
    @Autowired
    private DiscussMapper discussMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return discussMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Discuss record) {
        return discussMapper.insert(record);
    }

    @Override
    public int insertSelective(Discuss record) {
        return discussMapper.insertSelective(record);
    }

    @Override
    public Discuss selectByPrimaryKey(Integer id) {
        return discussMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Discuss record) {
        return discussMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(Discuss record) {
        return discussMapper.updateByPrimaryKeyWithBLOBs(record);
    }

    @Override
    public int updateByPrimaryKey(Discuss record) {
        return discussMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<Discuss> getListByBlogId(Integer blogId) {
        if (blogId == null || blogId == 0) {
            return null;
        }
        return discussMapper.getListByBlogId(blogId);
    }
}
