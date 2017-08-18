package com.shaw.mapper;

import com.shaw.bo.Visitor;
import org.apache.ibatis.annotations.Param;

public interface VisitorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Visitor record);

    int insertSelective(Visitor record);

    Visitor selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Visitor record);

    int updateByPrimaryKeyWithBLOBs(Visitor record);

    int updateByPrimaryKey(Visitor record);

    Visitor selectByAccountAndFrom(@Param("account") String account,@Param("oauthFrom") int oauthFrom);
}