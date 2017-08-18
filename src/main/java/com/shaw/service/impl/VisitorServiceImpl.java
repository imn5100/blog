package com.shaw.service.impl;

import com.shaw.bo.Visitor;
import com.shaw.mapper.VisitorMapper;
import com.shaw.service.VisitorService;
import com.shaw.util.StringUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisitorServiceImpl implements VisitorService {
    @Autowired
    private VisitorMapper visitorMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return visitorMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Visitor record) {
        record.setCreateTime(System.currentTimeMillis());
        record.setUpdateTime(System.currentTimeMillis());
        return visitorMapper.insert(record);
    }

    @Override
    public int insertSelective(Visitor record) {
        record.setCreateTime(System.currentTimeMillis());
        record.setUpdateTime(System.currentTimeMillis());
        return visitorMapper.insertSelective(record);
    }

    @Override
    public Visitor selectByPrimaryKey(Integer id) {
        return visitorMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Visitor record) {
        record.setUpdateTime(System.currentTimeMillis());
        return visitorMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(Visitor record) {
        record.setUpdateTime(System.currentTimeMillis());
        return visitorMapper.updateByPrimaryKeyWithBLOBs(record);
    }

    @Override
    public int updateByPrimaryKey(Visitor record) {
        record.setUpdateTime(System.currentTimeMillis());
        return visitorMapper.updateByPrimaryKey(record);
    }

    @Override
    public Visitor selectByAccountAndFrom(String account, Integer oauthFrom) {
        if (StringUtil.isEmpty(account) || oauthFrom == null || oauthFrom == 0) {
            return null;
        }
        return visitorMapper.selectByAccountAndFrom(account, oauthFrom);
    }

    @Override
    public Visitor updateOrInsertByAccountAndFrom(Visitor visitor) {
        if (visitor != null) {
            Visitor dbVisitor = this.selectByAccountAndFrom(visitor.getAccount(), visitor.getOauthFrom());
            if (dbVisitor != null) {
                //update
                visitor.setId(dbVisitor.getId());
                if (ObjectUtils.notEqual(dbVisitor.getMoreInfo(), visitor.getMoreInfo())) {
                    updateByPrimaryKeySelective(visitor);
                }
                return visitor;
            } else {
                //insert
                insert(visitor);
                return visitor;
            }
        }
        return null;
    }
}
