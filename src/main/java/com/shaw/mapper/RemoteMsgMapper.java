package com.shaw.mapper;

import com.shaw.bo.RemoteMsg;

public interface RemoteMsgMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RemoteMsg record);

    int insertSelective(RemoteMsg record);

    RemoteMsg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RemoteMsg record);

    int updateByPrimaryKey(RemoteMsg record);
}