package com.shaw.service;

import com.shaw.bo.Visitor;

public interface VisitorService {
    int deleteByPrimaryKey(Integer id);

    int insert(Visitor record);

    int insertSelective(Visitor record);

    Visitor selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Visitor record);

    int updateByPrimaryKeyWithBLOBs(Visitor record);

    int updateByPrimaryKey(Visitor record);

    Visitor selectByAccountAndFrom(String account, Integer oauthFrom);

    Visitor updateOrInsertByAccountAndFrom(Visitor visitor);
}